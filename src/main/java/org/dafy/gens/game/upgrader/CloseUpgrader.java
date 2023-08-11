package org.dafy.gens.game.upgrader;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.dafy.gens.Gens;

public class CloseUpgrader implements Listener {
    private final UpgradeManager upgradeManager;
    public CloseUpgrader(Gens plugin){
        this.upgradeManager = plugin.getUpgradeManager();
    }
    @EventHandler
    public void onUpgradeClose(InventoryCloseEvent e){
        String viewTitle = e.getView().getTitle();
        Inventory clickedInventory = e.getInventory();

        if (!UpgradeManager.UPGRADE_INVENTORY_TITLE.equals(viewTitle) || clickedInventory.getType() == InventoryType.PLAYER) return;
        upgradeManager.removeShopPlayer((Player) e.getPlayer());
    }
}
