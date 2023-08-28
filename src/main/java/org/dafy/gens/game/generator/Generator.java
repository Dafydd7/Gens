package org.dafy.gens.game.generator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@Data
public class Generator {
    private static final int GEN_RADIUS = 25;
    private Location genLocation;
    private Player genOwner;
    private ItemStack genItem;
    private ItemStack dropItem;
    private double price;
    private int delay;
    private int tier;
    private String islandUUID;

    public void minusDelay(){
        delay -= 1;
    }

    public boolean isPlayerNearby() {
        if(genOwner == null) return false;
        return genOwner.getWorld().equals(genLocation.getWorld()) && genOwner.getLocation().distance(this.genLocation) <= GEN_RADIUS;
    }
}
