package org.dafy.gens.Game.Generator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dafy.gens.Gens;

import java.util.Collections;
import java.util.HashMap;

public class GenUpgrade implements Listener {
    private final GenManager genManager;
    private final HashMap<Player, Generator> generatorTempMap = new HashMap<>();
    private static final String UPGRADE_INVENTORY_TITLE = "Upgrade Generator";
    private static final int UPGRADE_INVENTORY_SIZE = 36;
    private static final ItemStack CLOSE_ITEM = new ItemStack(Material.BARRIER);
    private final ItemStack removeItem;


    public GenUpgrade(Gens plugin) {
        this.genManager = plugin.getGenManager();
        removeItem = createRemoveItem();
    }

    public void open(Player player, Generator generator) {
        ItemStack generatorGenItem = generator.getGenItem();
        Inventory upgradeInventory = Bukkit.createInventory(player, UPGRADE_INVENTORY_SIZE, UPGRADE_INVENTORY_TITLE);
        upgradeInventory.setItem(13, generatorGenItem);
        upgradeInventory.setItem(30, removeItem);
        upgradeInventory.setItem(32, CLOSE_ITEM);
        player.openInventory(upgradeInventory);
        generatorTempMap.put(player, generator);
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String viewTitle = e.getView().getTitle();
        Inventory clickedInventory = e.getClickedInventory();
        ItemStack clickedItem = e.getCurrentItem();

        if (!UPGRADE_INVENTORY_TITLE.equals(viewTitle)
                || clickedInventory == null || clickedInventory.getType() == InventoryType.PLAYER || clickedItem == null
                || clickedItem.getType() == Material.AIR) return;

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        Generator generator = generatorTempMap.get(player);
        if (generator == null) return;

        if(clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        if (clickedItem.equals(removeItem)) {
            genManager.removeGenerator(generator, player);
            e.setCancelled(true);
        } else if (genManager.isMaxTier(generator.getTier())) {
            player.sendMessage("Generator is at max tier.");
            return;
        }else if (clickedItem.isSimilar(generator.getGenItem())) {
            genManager.updateGenerator(generator, player);
        }
        generatorTempMap.remove(player);
        player.closeInventory();
    }
    public final ItemStack createRemoveItem() {
        ItemStack removeItem = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = removeItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Remove Generator");
        meta.setLore(Collections.singletonList(ChatColor.RED + "Click here to remove this generator"));
        removeItem.setItemMeta(meta);
        return removeItem;
    }
}