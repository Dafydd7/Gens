package org.dafy.gens.game.generator;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@Getter
@Setter
public class GenDrop {
    private ItemStack dropItem;
    private String dropName;
    private List<String> dropLore;
    private double price;
    private int tier;
}
