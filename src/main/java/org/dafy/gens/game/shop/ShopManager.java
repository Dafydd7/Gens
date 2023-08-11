package org.dafy.gens.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;

import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShopManager {
    private final GenManager genManager;
    private final HashMap<ItemStack,Integer> sellableItems = new HashMap<>();
    public ShopManager(Gens plugin){
        genManager = plugin.getGenManager();
    }

    public boolean containsSellItem(ItemStack sellItem){
        return sellableItems.keySet().stream().anyMatch(itemStack -> itemStack.isSimilar(sellItem));
    }

    public int getSellPrice(ItemStack itemStack){
        Optional<Integer> priceOptional = sellableItems.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isSimilar(itemStack))
                .map(Map.Entry::getValue)
                .findFirst();
        return priceOptional.orElse(0);
    }
    public  void addSellableItem(ItemStack itemStack,int price) {
        sellableItems.put(itemStack,price);
    }
    public  void clearSellableItems() {
        sellableItems.clear();
    }

    public Inventory openShopGUI(Player player){
        Inventory inventory = Bukkit.createInventory(player,Math.max(9,genManager.genCount()),"Generator Shop");
        if(!genManager.isEmpty()) {
            for (Generator generator : genManager.getGenerators()) {
                inventory.addItem(generator.getGenItem());
            }
        }
        return inventory;
    }
}
