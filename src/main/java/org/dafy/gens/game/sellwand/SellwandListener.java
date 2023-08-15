package org.dafy.gens.game.sellwand;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.block.BlockManager;
import org.dafy.gens.game.shop.ShopManager;

public class SellwandListener implements Listener {
    private final BlockManager blockManager;
    private final ShopManager shopManager;

    public SellwandListener(Gens plugin) {
        this.blockManager = plugin.getBlockManager();
        this.shopManager = plugin.getShopManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        //Get the players item in hand.
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) return;
        if(itemInHand == null || itemInHand.getType().equals(Material.AIR)) return;
        Block clickedBlock = e.getClickedBlock();
        //Return early, if block was null,or not a chest.
        if(clickedBlock == null || clickedBlock.getType() != Material.CHEST) return;
        //If the item in  hand is not a "sell-wand" then return early.
        if (itemInHand.getType() != Material.STICK && !blockManager.hasItemPersistentData(itemInHand, "SellWand")) return;
        if(e.isCancelled()) return; // Check to see if bento box or any other plugin has cancelled event
        //Get the "chest"
        InventoryHolder inventory = (InventoryHolder) clickedBlock.getState();
        //Get the island at the players' location, if no island exists, return null.
        shopManager.sellAllItems(player,inventory.getInventory());
        e.setCancelled(true); //Finally, cancel the player from opening the chest.
    }
}
