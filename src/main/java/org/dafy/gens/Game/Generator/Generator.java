package org.dafy.gens.Game.Generator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.user.User;

@Getter
@Setter
@Data
public class Generator {
    private  Location genLocation;
    private String genName;
    private ItemStack genItem;
    private ItemStack dropItem;
    private User owner;
    private int delay;
    private int tier;

    public void dropItemNaturally(){
        if(genItem == null || genLocation.getWorld() == null) return;
        genLocation.getWorld().dropItemNaturally(genLocation,dropItem);
    }
}
