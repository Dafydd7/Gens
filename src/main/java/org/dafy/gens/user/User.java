package org.dafy.gens.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.dafy.gens.game.generator.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
public class User {
    private final UUID uuid;
    private Player player;
    private int gensPlaced = 0;
    private int genLimit = 40;
    private List<Generator> generators = new ArrayList<>();

   public void incrementGensPlaced(){
       genLimit -= 1;
       gensPlaced += 1;
   }
    public void decrementGensPlaced(){
        genLimit += 1;
        gensPlaced -= 1;
    }

    public void addGenerator(Generator generator) {
        generators.add(generator);
        incrementGensPlaced();
    }

    public void removeGenerator(Generator generator){
        generators.remove(generator);
        decrementGensPlaced();
    }
    public void addAndRemove(Generator oldGenerator,Generator newGenerator){
        removeGenerator(oldGenerator);
        addGenerator(newGenerator);
    }
}
