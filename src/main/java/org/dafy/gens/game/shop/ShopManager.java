package org.dafy.gens.game.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.economy.GenEconomy;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.Generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShopManager {
    private final GeneratorManager generatorManager;
    private final GensEvent gensEvent;
    private final HashMap<ItemStack,Double> sellableItems = new HashMap<>();
    public ShopManager(Gens plugin){
        generatorManager = plugin.getGeneratorManager();
        gensEvent = plugin.getGensEvent();
    }

    public boolean containsSellItem(ItemStack sellItem){
        return sellableItems.keySet().stream().anyMatch(itemStack -> itemStack.isSimilar(sellItem));
    }

    public double getSellPrice(ItemStack itemStack){
        Optional<Double> priceOptional = sellableItems.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isSimilar(itemStack))
                .map(Map.Entry::getValue)
                .findFirst();
        return priceOptional.orElse(0.0);
    }
    public  void addSellableItem(ItemStack itemStack,double price) {
        sellableItems.put(itemStack,price);
    }
    public  void clearSellableItems() {
        sellableItems.clear();
    }

    public void openShopGUI(Player player){
        int genCount = generatorManager.genCount();
        int inventorySize = (genCount / 9 + 1) * 9; // Round up to the nearest multiple of 9
        Inventory inventory = Bukkit.createInventory(player, inventorySize, "Generator Shop");
        if(generatorManager.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Error: No gens have been created.\nPlease contact an admin.");
            return;
        }
        for (Generator generator : generatorManager.getGenerators()) {
            inventory.addItem(generator.getGeneratorItem());
        }
        player.openInventory(inventory);
    }
    public void sellAllItems(Player player,Inventory inventory){
        ItemStack[] items = inventory.getContents();
        double sellAmount = 0;
        for (ItemStack item: items) {
            if(item == null || item.getType().isAir()) continue;
            if(!containsSellItem(item)) {
                continue;
            }
            sellAmount += getSellPrice(item) * item.getAmount();
            inventory.remove(item);
        }
        if(sellAmount == 0){
            player.sendMessage("No sellable item(s) were found.");
            return;
        }
        double doubledAmount = sellAmount * 2;
        if (gensEvent.isSellMode()) {
            GenEconomy.getEconomy().depositPlayer(player, doubledAmount);
            player.sendMessage(ChatColor.GREEN + "+$" + doubledAmount);
        } else {
            GenEconomy.getEconomy().depositPlayer(player, sellAmount);
            player.sendMessage(ChatColor.GREEN + "+$" + sellAmount);
        }
    }
}
