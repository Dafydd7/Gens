package org.dafy.gens.game.spawner;

import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.Generator;

import javax.swing.plaf.SpinnerUI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpawnerManager {
    private List<Generator> activeGenerators = new CopyOnWriteArrayList<>();

    public SpawnerManager(Gens plugin){

    }

    public List<Generator> getActiveGenerators(){
        return activeGenerators;
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
