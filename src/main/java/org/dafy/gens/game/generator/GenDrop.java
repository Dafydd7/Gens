package org.dafy.gens.game.generator;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class GenDrop {
    private ItemStack dropItem;
    private String dropName;
    private List<String> dropLore;
    private double price;
    private int tier;
    public GenDrop setDropName(String name){
        this.dropName = name;
        return this;
    }
    public GenDrop setDropItem(ItemStack item){
        this.dropItem = item;
        return this;
    }
    public GenDrop setDropLore(List<String> lore){
        this.dropLore = lore;
        return this;
    }
    public GenDrop setDropPrice(double price){
        this.price = price;
        return this;
    }
    public GenDrop setDropTier(int tier){
        this.tier = tier;
        return this;
    }
}
