package org.dafy.gens.game.shop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.block.BlockManager;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.game.economy.GenEconomy;

public class GenShop implements Listener {
    private final GenManager genManager;
    private final BlockManager blockManager;
    private final Economy economy;
    public GenShop(Gens plugin){
        this.genManager = plugin.getGenManager();
        this.blockManager = plugin.getBlockManager();
        this.economy = GenEconomy.getEconomy();
    }
    @EventHandler
    public void onShopClick(InventoryClickEvent e){
        Inventory clickedInventory = e.getClickedInventory();
        ItemStack clickedItem = e.getCurrentItem();
        //Shop check
        if(!e.getView().getTitle().equals("Generator Shop")) return;
        if (clickedInventory == null || clickedItem == null
                || clickedItem.getType() == Material.AIR) return;
        //Cancel click event
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        //Return early if players inventory is full.
        if((player.getInventory().firstEmpty() == -1)) return;
        //Check for generator matches
        for (Generator generator: genManager.getGenerators()) {
            if(clickedItem.isSimilar(generator.getGenItem())) {
                if(!economy.has(player,generator.getPrice())) return;
                int tier = generator.getTier();
                ItemStack genItem = generator.getGenItem();
                blockManager.addItemPersistentData(genItem,"GeneratorItem",tier);
                player.getInventory().addItem(genItem);
                economy.withdrawPlayer(player, generator.getPrice());
            }
        }
    }
}
