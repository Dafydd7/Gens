package org.dafy.gens.game.spawner;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.dafy.gens.Gens;
import org.dafy.gens.game.block.BlockManager;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.game.generator.Generator;


public class ItemSpawner extends BukkitRunnable {
    private final GensEvent gensEvent;
    private final SpawnerManager spawnerManager;
    private final BlockManager blockManager;
    public ItemSpawner(Gens plugin) {
        blockManager = plugin.getBlockManager();
        spawnerManager = plugin.getSpawnerManager();
        gensEvent = plugin.getGensEvent();
    }

    @Override
    public void run() {
        for (Generator generator : spawnerManager.getActiveGenerators()) {
            Location location = generator.getGenLocation();
            if(location.getWorld() == null) continue; // Skip over the gen if the world is null.
            if(!generator.isPlayerNearby()) continue; //Skip the gen if the player is not near their gens.
            //Double check that the generator still exists.
            if(!blockManager.hasBlockPersistentData(location.getBlock(), "Generator") || location.getBlock().getType().isAir()){
                spawnerManager.removeActiveGenerator(generator);
                continue;
            }
            gensEvent.getModeAndDrop(generator);
        }
    }

}
