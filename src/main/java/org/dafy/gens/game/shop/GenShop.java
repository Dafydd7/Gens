package org.dafy.gens.game.shop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.game.economy.GenEconomy;

public class GenShop implements Listener {
    private final GenManager genManager;
    private final Economy economy;
    public GenShop(Gens plugin){
        this.genManager = plugin.getGenManager();
        this.economy = GenEconomy.getEconomy();
    }
    @EventHandler
    public void onShopClick(InventoryClickEvent e){
        if(!e.isLeftClick()) {
            e.setCancelled(true);
            return;
        }
        String viewTitle = e.getView().getTitle();
        Inventory clickedInventory = e.getClickedInventory();
        ItemStack clickedItem = e.getCurrentItem();

        if (!"Generator Shop".equals(viewTitle)
                || clickedInventory == null || clickedInventory.getType() == InventoryType.PLAYER || clickedItem == null
                || clickedItem.getType() == Material.AIR) return;

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        if((player.getInventory().firstEmpty() == -1)) return;

        for (Generator generator: genManager.getGenerators()) {
            if(clickedItem.isSimilar(generator.getGenItem())) {
                if(!economy.has(player,generator.getPrice())) return;
                player.getInventory().addItem(generator.getGenItem());
                economy.withdrawPlayer(player, generator.getPrice());
            }
        }
    }
}
