package org.dafy.gens.game.upgrader;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.economy.GenEconomy;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;

public class GenUpgrader implements Listener {
    private final UpgradeManager upgradeManager;
    private final GenManager genManager;
    private final Economy economy;
    public GenUpgrader(Gens plugin){
        this.upgradeManager = plugin.getUpgradeManager();
        this.genManager = plugin.getGenManager();
        this.economy = GenEconomy.getEconomy();
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.isLeftClick()) return;
        String viewTitle = e.getView().getTitle();
        Inventory clickedInventory = e.getClickedInventory();
        ItemStack clickedItem = e.getCurrentItem();

        if (!UpgradeManager.UPGRADE_INVENTORY_TITLE.equals(viewTitle)
                || clickedInventory == null || clickedInventory.getType() == InventoryType.PLAYER || clickedItem == null
                || clickedItem.getType() == Material.AIR) return;

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        Generator generator = upgradeManager.getShopPlayer(player);
        if (generator == null) return;

        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(upgradeManager.getRemoveItem())) {
            genManager.removeGenerator(generator, player.getUniqueId());
            player.closeInventory();
            return;
        }
        if (genManager.isMaxTier(generator.getTier())) {
            player.sendMessage("Generator is at max tier.");
            player.closeInventory();
            return;
        }

        int newTier = generator.getTier() +1 ;
        int genPrice = genManager.getPrice(newTier);

        if (clickedItem.isSimilar(generator.getGenItem()) && economy.has(player,genPrice)) {
            economy.withdrawPlayer(player,genPrice);
            player.sendMessage(ChatColor.GREEN + "Upgraded generator to tier "+ newTier +" for -$" + genPrice + ".");
            genManager.updateGenerator(generator, player);
        } else{
            player.sendMessage(ChatColor.RED + "You need $" + (genPrice - economy.getBalance(player))  + " in order to buy this.");
        }
        player.closeInventory();
    }
}

