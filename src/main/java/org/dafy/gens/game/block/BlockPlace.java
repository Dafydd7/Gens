package org.dafy.gens.game.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;

public class BlockPlace implements Listener {
    private final Gens plugin;
    private final BlockManager blockManager;
    public BlockPlace(Gens plugin){
        this.plugin = plugin;
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemStack generatorItem = e.getPlayer().getInventory().getItemInMainHand();

        // Check if the item in hand is a block
        if (!generatorItem.getType().isBlock()) return;

        // Check to see whether the itemStack is a generatorItem
        if (!blockManager.hasItemPersistentData(generatorItem, "GeneratorItem")) return;

        // Create the user object from cache, and check the players' limit
        User user = plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
        if (user.getGenLimit() <= 0) {
            e.getPlayer().sendMessage("You have reached your gen limit.");
            e.setCancelled(true);
            return;
        }
        //Set the tier based off the nbtData.
        int tier = blockManager.getItemTier(generatorItem,"GeneratorItem");
        //Create the generator based off the tier.
        Generator generator = plugin.getGenManager().createGenerator(e.getBlock().getLocation(),tier);
        if(generator == null) {
            e.getPlayer().sendMessage("[Gens] ERROR: Cannot place null generator!");
            e.setCancelled(true);
            return;
        }
        // Set the persistent data container for this block.
        blockManager.addBlockPersistentData(e.getBlock(), "Generator", tier);
        // Adds +1 to the gensPlaced, and genLimit amount.
        user.addPlaced();
        //Add the generator object to the users list.
        user.addGenerator(generator);
        //Finally, add the generator to the spawnerList, to enable itemSpawning.
        plugin.getItemSpawner().addActiveGenerator(generator);
    }

}