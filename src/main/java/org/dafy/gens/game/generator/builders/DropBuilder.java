package org.dafy.gens.game.generator.builders;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dafy.gens.game.generator.GenDrop;

import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class DropBuilder implements ItemBuilder{
    private final Material dropMaterial;
    private String dropName;
    private List<String> dropLore;
    private int price;
    private int tier;

    public DropBuilder(ConfigurationSection section) {
        this.dropMaterial = getMaterial(section.getString("material", "stone"));
        setName(section.getString("display.name"));
        setLore(section.getStringList("display.lore"));
        setPrice(section.getInt("price"));
        setTier(section.getInt("tier"));
    }

    public GenDrop build() {
        GenDrop generatorDrop = new GenDrop();
        generatorDrop.setDropItem(buildItem());
        generatorDrop.setDropName(dropName);
        generatorDrop.setDropLore(dropLore);
        generatorDrop.setTier(tier);
        generatorDrop.setPrice(price);
        return generatorDrop;
    }

    @Override
    public ItemStack buildItem() {
        ItemStack item = new ItemStack(dropMaterial);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        if (dropName != null) meta.setDisplayName(dropName);
        if (dropLore != null) meta.setLore(dropLore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void setName(String name) {
        if (name != null)
            this.dropName = ChatColor.translateAlternateColorCodes('&', name);
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore != null) {
            ListIterator<String> iterator = lore.listIterator();
            while (iterator.hasNext()) {
                iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next()));
            }
            this.dropLore = lore;
        }
    }
    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public Material getMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return Material.STONE;
        }
    }
}
