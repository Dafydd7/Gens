package org.dafy.gens.game.spawner;

import org.bukkit.scheduler.BukkitRunnable;
import org.dafy.gens.Gens;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.game.generator.Generator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ItemSpawner extends BukkitRunnable {
    private final GensEvent gensEvent;
    private final SpawnerManager spawnerManager;
    public ItemSpawner(Gens plugin) {
        spawnerManager = plugin.getSpawnerManager();
        gensEvent = plugin.getGensEvent();
    }
    @Override
    public void run() {
        for (Generator generator : spawnerManager.getActiveGenerators()) {
            //Double check that the generator still exists.
            if(!generator.getGenLocation().getBlock().getType().isBlock()){
                spawnerManager.removeActiveGenerator(generator);
                continue;
            }
            gensEvent.getModeAndDrop(generator);
        }
    }

}
