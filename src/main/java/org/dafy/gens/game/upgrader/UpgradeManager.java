package org.dafy.gens.game.upgrader;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.dafy.gens.game.generator.Generator;

import java.util.HashMap;

public class UpgradeManager implements Listener {
   private final HashMap<Player, Generator> generatorShopMap = new HashMap<>();

    public Generator getShopPlayer(Player player){
        return generatorShopMap.get(player);
    }
    public void addShopPlayer(Player player,Generator generator){
        generatorShopMap.put(player,generator);
    }
    public void removeShopPlayer(Player player){
        generatorShopMap.remove(player);
    }


//    public static Inventory UPGRADE(Player player, Generator generator) {
//        ItemStack generatorGenItem = generator.getGenItem();
//        Inventory upgradeInventory = Bukkit.createInventory(player, UPGRADE_INVENTORY_SIZE, UPGRADE_INVENTORY_TITLE);
//        upgradeInventory.setItem(13, generatorGenItem);
//        upgradeInventory.setItem(30, getRemoveItem());
//        upgradeInventory.setItem(32, CLOSE_ITEM);
//        player.openInventory(upgradeInventory);
//        return upgradeInventory;
//    }

}