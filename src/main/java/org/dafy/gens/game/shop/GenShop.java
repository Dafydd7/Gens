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
import org.dafy.gens.game.managers.BlockManager;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.game.economy.GenEconomy;

public class GenShop implements Listener {
    private final GeneratorManager generatorManager;
    private final BlockManager blockManager;
    private final Economy economy;
    public GenShop(Gens plugin){
        this.generatorManager = plugin.getGeneratorManager();
        this.blockManager = plugin.getBlockManager();
        this.economy = GenEconomy.getEconomy();
    }
    @EventHandler
    public void onShopClick(InventoryClickEvent e){
        Inventory clickedInventory = e.getClickedInventory();
        ItemStack clickedItem = e.getCurrentItem();
        //Shop check
        if(!e.getView().getTitle().equals("Generator Shop")) return;
        if (clickedInventory == null || clickedItem == null || clickedItem.getType() == Material.AIR) return;
        //Cancel click event
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        //Return early if players inventory is full.
        if((player.getInventory().firstEmpty() == -1)) return;
        //Check for generator matches
        for (Generator generator: generatorManager.getGenerators()) {
            if(clickedItem.isSimilar(generator.getGeneratorItem())) {
                if(!economy.has(player,generator.getPrice())) return;
                int tier = generator.getTier();
                ItemStack genItem = generator.getGeneratorItem();
                blockManager.addItemPersistentData(genItem,"GeneratorItem",tier);
                player.getInventory().addItem(genItem);
                economy.withdrawPlayer(player, generator.getPrice());
            }
        }
    }
}
