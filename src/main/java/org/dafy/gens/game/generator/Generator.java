package org.dafy.gens.game.generator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import world.bentobox.bentobox.database.objects.Island;

@Getter
@Setter
@Data
public class Generator {

    private Island island;
    private Location genLocation;
    private ItemStack genItem;
    private ItemStack dropItem;
    private int price;
    private int delay;
    private int tier;

    public void dropItemNaturally(){
        if(genItem == null || genLocation.getWorld() == null) return;
        genLocation.getWorld().dropItemNaturally(genLocation,dropItem);
    }

    public String getIslandName() {
        if(island == null) return "N/A";
        return island.getName();
    }
}
