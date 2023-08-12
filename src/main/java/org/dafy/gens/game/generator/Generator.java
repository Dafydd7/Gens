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
    private int price;
    private int delay;
    private int tier;
    private String islandUUID;

    public void dropItemNaturally(){
        if(genItem == null || genLocation.getWorld() == null) return;
        genLocation.getWorld().dropItemNaturally(genLocation,dropItem);
    }
}
