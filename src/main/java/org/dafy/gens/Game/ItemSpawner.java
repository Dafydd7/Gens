package org.dafy.gens.Game;

import org.bukkit.scheduler.BukkitRunnable;
import org.dafy.gens.Game.Generator.Generator;
import org.dafy.gens.Gens;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class ItemSpawner extends BukkitRunnable {

    private List<Generator> activeGenerators = new CopyOnWriteArrayList<>();
    public ItemSpawner() {

    }
    @Override
    public void run() {
        for (Generator generator : activeGenerators) {
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
