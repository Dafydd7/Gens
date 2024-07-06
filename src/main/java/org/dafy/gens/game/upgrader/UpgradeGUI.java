package org.dafy.gens.game.upgrader;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dafy.gens.Gens;
import org.dafy.gens.game.economy.GenEconomy;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.Generator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class UpgradeGUI implements InventoryHolder, Listener {
    private final Inventory inventory;
    private final UpgradeManager upgradeManager;
    private final GeneratorManager generatorManager;
    private final Economy economy;

    public UpgradeGUI(Gens plugin) {
        this.upgradeManager = plugin.getUpgradeManager();
        this.generatorManager = plugin.getGeneratorManager();
        this.economy = GenEconomy.getEconomy();
        inventory = Bukkit.createInventory(this, 36, "Upgrade GUI");
        inventory.setItem(32,createGuiItem(Material.BARRIER, "§c§lClose GUI", "§8ɢᴇɴᴇʀᴀᴛᴏʀ","", "§eClick to close this GUI."));
        inventory.setItem(30,createGuiItem(Material.RED_WOOL, "§c§lRemove generator","§8ɢᴇɴᴇʀᴀᴛᴏʀ","", "§eClick to remove this generator."));
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack createGuiItem(Material material, String name, String...lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(Player player, Generator generator) {
        final double genPrice = getUpgradePrice(generator);
        inventory.setItem(13,createGuiItem(Material.AMETHYST_SHARD, "§a§lUpgrade Generator", "§8ɢᴇɴᴇʀᴀᴛᴏʀ","","§8Price: §a$§f" + genPrice, "§eClick to upgrade your generator!") );
        player.openInventory(inventory);
        upgradeManager.addShopPlayer(player,generator);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.isCancelled()) return;
        if (e.getInventory().getHolder() != this) return;
        if (e.getClick().equals(ClickType.NUMBER_KEY)) {
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        ItemStack clickedItem = e.getCurrentItem();
        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        Player player = (Player) e.getWhoClicked();
        Generator generator = upgradeManager.getShopPlayer(player);
        if (generator == null) return;
        //This corresponds to the slots that were initialised in UpgradeGUI.class
        switch (e.getRawSlot()){
            case 30:
                generatorManager.removeGenerator(generator, player.getUniqueId());
                player.closeInventory();
                return;
            case 32:
                player.closeInventory();
                return;
            case 13:
                if (generatorManager.isMaxTier(generator.getTier())) {
                    player.sendMessage("Generator is at max tier.");
                    player.closeInventory();
                    return;
                }
              final double genPrice = getUpgradePrice(generator);
                if (!economy.has(player,genPrice)) {
                    player.sendMessage(ChatColor.RED + "You need $" + (genPrice - economy.getBalance(player))  + " in order to buy this.");
                    return;
                }
                economy.withdrawPlayer(player, genPrice);
                player.sendMessage(ChatColor.GREEN + "Upgraded generator to tier " + (generator.getTier() + 1) + " for $" + genPrice + ".");
                generatorManager.updateGenerator(generator, player);
                player.closeInventory();
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() != this) return;
        upgradeManager.removeShopPlayer((Player) e.getPlayer());
    }
    private double getUpgradePrice(Generator generator){
        int oldTier = generator.getTier();
        int newTier = oldTier + 1;
        return generatorManager.getPrice(newTier) - generatorManager.getPrice(oldTier);
    }
}

