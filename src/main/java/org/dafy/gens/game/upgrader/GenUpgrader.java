package org.dafy.gens.game.upgrader;

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

public class GenUpgrader implements Listener {
    private final UpgradeManager upgradeManager;
    private final GenManager genManager;
    public GenUpgrader(Gens plugin){
        this.upgradeManager = plugin.getUpgradeManager();
        this.genManager = plugin.getGenManager();
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
        }else if (clickedItem.equals(upgradeManager.getRemoveItem())) {
            genManager.removeGenerator(generator, player);
            e.setCancelled(true);
        } else if (genManager.isMaxTier(generator.getTier())) {
            player.sendMessage("Generator is at max tier.");
        } else if (clickedItem.isSimilar(generator.getGenItem())) {
            genManager.updateGenerator(generator, player);
        }
        upgradeManager.removeShopPlayer(player);
        player.closeInventory();
    }
}

