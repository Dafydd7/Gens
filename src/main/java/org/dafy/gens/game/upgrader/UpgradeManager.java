package org.dafy.gens.game.upgrader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.Generator;

import java.util.Collections;
import java.util.HashMap;

public class UpgradeManager implements Listener {
    public static final String UPGRADE_INVENTORY_TITLE = "Upgrade Generator";
    private static final ItemStack CLOSE_ITEM = new ItemStack(Material.BARRIER);
    private static final int UPGRADE_INVENTORY_SIZE = 36;
    private final ItemStack removeItem;
    private final HashMap<Player, Generator> generatorShopMap = new HashMap<>();


    public UpgradeManager() {
        removeItem = getRemoveItem();
    }

    public Generator getShopPlayer(Player player){
        return generatorShopMap.get(player);
    }
    public void removeShopPlayer(Player player){
        generatorShopMap.remove(player);
    }

    public void open(Player player, Generator generator) {
        ItemStack generatorGenItem = generator.getGenItem();
        Inventory upgradeInventory = Bukkit.createInventory(player, UPGRADE_INVENTORY_SIZE, UPGRADE_INVENTORY_TITLE);
        upgradeInventory.setItem(13, generatorGenItem);
        upgradeInventory.setItem(30, removeItem);
        upgradeInventory.setItem(32, CLOSE_ITEM);
        player.openInventory(upgradeInventory);
        generatorShopMap.put(player, generator);
    }

    public ItemStack getRemoveItem(){
        ItemStack removeItem = new ItemStack(Material.RED_WOOL);
        ItemMeta meta = removeItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "Remove Generator");
        meta.setLore(Collections.singletonList(ChatColor.RED + "Click here to remove this generator"));
        removeItem.setItemMeta(meta);
        return removeItem;
    }
}