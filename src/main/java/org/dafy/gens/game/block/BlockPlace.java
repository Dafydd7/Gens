package org.dafy.gens.game.block;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
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
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        // Prevents the user from going any further, as block place has been cancelled.
        if(e.isCancelled()) {
            e.setCancelled(true);
            player.sendMessage("Cannot place here");
            return;
        }
        //Get the itemInMainHand.
        ItemStack generatorItem = player.getInventory().getItemInMainHand();

        // Check to see whether the itemStack is a generatorItem.
        if (!blockManager.hasItemPersistentData(generatorItem, "GeneratorItem")) return;

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
        Generator generator = plugin.getGenManager().createGenerator(e.getBlock().getLocation(), tier);

        if(generator == null) {
            e.getPlayer().sendMessage("[Gens] ERROR: Cannot place null generator!");
            e.setCancelled(true);
            return;
        }

        world.bentobox.bentobox.api.user.User islandUser = world.bentobox.bentobox.api.user.User.getInstance(player);

        World playerWorld = player.getWorld();
        Island island = islandsManager.getIsland(playerWorld, islandUser);

        generator.setIslandUUID(island.getUniqueId());
        // Set the persistent data container for this block.
        blockManager.addBlockPersistentData(e.getBlock(), "Generator", tier);
        // Adds +1 to the gensPlaced, and genLimit amount.
        user.addPlaced();
        //Add the generator object to the users list.
        user.addGenerator(generator);
        //Finally, add the generator to the spawnerList, to enable itemSpawning.
        plugin.getSpawnerManager().addActiveGenerator(generator);
    }

}