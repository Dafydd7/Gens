package org.dafy.gens.game.sellwand;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.dafy.gens.Gens;
import org.dafy.gens.game.block.BlockManager;
import org.dafy.gens.game.shop.ShopManager;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.FlagListener;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.bentobox.managers.FlagsManager;
import world.bentobox.bentobox.managers.IslandsManager;

public class Sellwand extends FlagListener implements Listener {
    private final BlockManager blockManager;
    private final ShopManager shopManager;

    public Sellwand(Gens plugin){
        this.blockManager = plugin.getBlockManager();
        this.shopManager = plugin.getShopManager();
        BentoBox bentoBox = JavaPlugin.getPlugin(BentoBox.class);
    }
    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent e) {
//        if(e.getRightClicked().getType().equals(EntityType.CH))
//        Inventory inventory = e.getInventory();
//        if(inventory.getType() != InventoryType.CHEST) return;
          Player player = (Player) e.getPlayer();
//        ItemStack itemInHand = player.getInventory().getItemInMainHand();
//        if (itemInHand.getType() != Material.STICK && !blockManager.hasItemPersistentData(itemInHand, "SellWand")) return;
//        world.bentobox.bentobox.api.user.User islandUser = world.bentobox.bentobox.api.user.User.getInstance(player);
        //TODO fix me - this is just a debug
        if(!this.checkIsland(e,player,e.getRightClicked().getLocation(), Flags.CHEST)) return;
        player.sendMessage("HELLO WORLD");
        e.setCancelled(true);
        //shopManager.sellAllItems(player,inventory);
    }
}
