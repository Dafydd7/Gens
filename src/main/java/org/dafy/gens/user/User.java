package org.dafy.gens.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dafy.gens.game.generator.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
public class User {
    private final UUID uuid;
    private int gensPlaced = 0;
    private int genLimit = 40;
    private List<Generator> generators = new ArrayList<>();


   public void addPlaced(){
       genLimit -= 1;
       gensPlaced += 1;
   }
    public void removePlaced(){
        genLimit += 1;
        gensPlaced -= 1;
    }

    public void addGenerator(Generator generator) {


        generators.add(generator);
    }
    public void removeGenerator(Generator generator){
        generators.remove(generator);
    }
    public void addAndRemove(Generator oldGenerator,Generator newGenerator){
        generators.remove(oldGenerator);
        generators.add(newGenerator);
    }
}
