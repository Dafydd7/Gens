package org.dafy.gens.game.spawner;

import lombok.Getter;
import org.dafy.gens.game.generator.Generator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class SpawnerManager {
    private List<Generator> activeGenerators = new CopyOnWriteArrayList<>();
    public SpawnerManager(){
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
