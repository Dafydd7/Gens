package org.dafy.gens.game.generator.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemBuilder {
        ItemStack buildItem();
        Material getMaterial(String materialName);
        void setName(String name);
        void setLore(List<String> lore);
        void setPrice(int price);
        void setTier(int tier);
}
