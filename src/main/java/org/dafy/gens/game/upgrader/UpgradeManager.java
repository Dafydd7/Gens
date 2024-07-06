package org.dafy.gens.game.upgrader;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.dafy.gens.game.generator.Generator;

import java.util.HashMap;

public class UpgradeManager implements Listener {
   private final HashMap<Player, Generator> generatorShopMap = new HashMap<>();

    public Generator getShopPlayer(Player player){
        return generatorShopMap.get(player);
    }
    public void addShopPlayer(Player player,Generator generator){
        generatorShopMap.put(player,generator);
    }
    public void removeShopPlayer(Player player){
        generatorShopMap.remove(player);
    }

}