package org.dafy.gens.user;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.dafy.gens.Game.Generator.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
public class User {
    private final UUID uuid;
    private int gensPlaced;
    private int genLimit;
    private List<Generator> generators = new ArrayList<>();


   public void addPlaced(int amount){
       genLimit -= 1;
       gensPlaced += 1;
   }
    public void removePlaced(int amount){
        genLimit += 1;
        gensPlaced -= 1;
    }

    public void addGenerator(Generator generator){
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
