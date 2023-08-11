package org.dafy.gens.game;

import org.bukkit.scheduler.BukkitRunnable;
import org.dafy.gens.game.generator.Generator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ItemSpawner extends BukkitRunnable {

    private List<Generator> activeGenerators = new CopyOnWriteArrayList<>();
    public ItemSpawner() {

    }
    @Override
    public void run() {
        for (Generator generator : activeGenerators) {
            //Double check that the generator still exists.
            if(!generator.getGenLocation().getBlock().getType().isBlock()){
                removeActiveGenerator(generator);
                continue;
            }
            generator.dropItemNaturally();
        }
    }

    public synchronized void addActiveGenerator(Generator generator) {
        activeGenerators.add(generator);
    }
    public synchronized void removeActiveGenerator(Generator generator) {
        activeGenerators.remove(generator);
    }
    public synchronized void addAndRemoveGen(Generator oldGenerator,Generator newGenerator) {
        activeGenerators.remove(oldGenerator);
        activeGenerators.add(newGenerator);
    }
}
