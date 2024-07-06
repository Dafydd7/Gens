package org.dafy.gens.game.generator;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Data
public class Generator {
    private static final int GEN_RADIUS = 25;
    private Location generatorLocation;
    private ItemStack generatorItem;
    private ItemStack dropItem;
    private double price;
    private int generatorDelay;
    private int tier;
    private String islandUUID;

    public Generator setGeneratorItem(ItemStack item){
        generatorItem = item;
        return this;
    }
    public Generator setGeneratorTier(int tier){
        this.tier = tier;
        return this;
    }
    public Generator setGeneratorDelay(int delay){
        this.generatorDelay = delay;
        return this;
    }
    public Generator setGeneratorPrice(double price){
        this.price = price;
        return this;
    }

    public Generator setTier(int tier){
        this.tier = tier;
        return this;
    }
    public Generator setGeneratorLocation(Location location){
        this.generatorLocation = location;
        return this;
    }
    public Generator setDropItem(ItemStack item) {
        this.dropItem = item;
        return this;
    }

    public void decrementDelay(){
        generatorDelay -= 1;
    }

    public boolean isPlayerNearby(Player player) {
        return player.getWorld().equals(generatorLocation.getWorld()) && player.getLocation().distance(this.generatorLocation) <= GEN_RADIUS;
    }
}
