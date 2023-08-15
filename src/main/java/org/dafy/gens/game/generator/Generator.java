package org.dafy.gens.game.generator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;


@Getter
@Setter
@Data
public class Generator {
    private Location genLocation;
    private ItemStack genItem;
    private ItemStack dropItem;
    private double price;
    private int delay;
    private int tier;
    private String islandUUID;

    public void minusDelay(){
        delay -= 1;
    }
}
