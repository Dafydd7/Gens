package org.dafy.gens.game.generator.builders;

import org.bukkit.Bukkit;
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
    private double price;
    private int tier;

    public DropBuilder(ConfigurationSection section) {
        setName(section.getString("display.name"));
        this.dropMaterial = getMaterial(section.getString("material", "stone"));
        setPrice(section.getDouble("price"));
        setLore(section.getStringList("display.lore"));
        setTier(section.getInt("tier"));
    }

    public GenDrop build() {
        return new GenDrop()
                .setDropItem(buildItem())
                .setDropName(dropName)
                .setDropLore(dropLore)
                .setDropTier(tier)
                .setDropPrice(price);
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
        if (name != null) this.dropName = ChatColor.translateAlternateColorCodes('&', name);
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore != null) {
            ListIterator<String> iterator = lore.listIterator();
            while (iterator.hasNext()) {
                iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next().replace("%price%",String.valueOf(price))));
            }
            this.dropLore = lore;
        }
    }
    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public Material getMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning(String.format("Failed to load the material for generator %s! Reverting to material STONE.",dropName));
            return Material.STONE;
        }
    }
}
