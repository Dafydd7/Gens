package org.dafy.gens.game.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.dafy.gens.game.managers.BlockManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.managers.IslandsManager;

public class BlockPlace implements Listener {
    private final Gens plugin;
    private final BlockManager blockManager;
    private final IslandsManager islandsManager;

    public BlockPlace(Gens plugin){
        this.plugin = plugin;
        this.blockManager = plugin.getBlockManager();
        BentoBox bentoBox = JavaPlugin.getPlugin(BentoBox.class);
        islandsManager = bentoBox.getIslandsManager();
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack generatorItem = player.getInventory().getItemInMainHand();
        // Check to see whether the itemStack is a generatorItem.
        if (!blockManager.hasItemPersistentData(generatorItem, "GeneratorItem")) return;
        //Check to see if the player tried to place the gen on an island.
        if(islandsManager.getIslandAt(player.getLocation()).isEmpty()){
            player.sendMessage("You can only place generators on islands.");
            e.setCancelled(true);
            return;
        }
        // Create the user object from cache, and check the players' limit.
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user.getGenLimit() <= 0) {
            e.getPlayer().sendMessage("You have reached your gen limit.");
            e.setCancelled(true);
            return;
        }
        //Set the tier based off the nbtData.
        int tier = blockManager.getItemTier(generatorItem,"GeneratorItem");
        //Create the generator based off the tier.
        Generator generator = plugin.getGeneratorManager().createGenerator(e.getBlock().getLocation(), tier);
        //Return early if gen is null
        if(generator == null) {
            e.getPlayer().sendMessage("[Gens] ERROR: Cannot place null generator!");
            e.setCancelled(true);
            return;
        }
        //Set the island id & owner for the generator.
        generator.setIslandUUID(islandsManager.getIslandAt(player.getLocation()).get().getUniqueId());
        //generator.setGenOwner(player);
        // Set the persistent data container for this block.
        blockManager.addBlockPersistentData(e.getBlock(), "Generator", tier);
        // Increments/Decrements the users gens placed/limit, and caches the generator to the user.
        user.addGenerator(generator);
    }
}