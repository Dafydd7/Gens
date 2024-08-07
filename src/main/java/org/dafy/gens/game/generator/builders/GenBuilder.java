package org.dafy.gens.game.generator.builders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.dafy.gens.game.generator.Generator;

import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class GenBuilder implements ItemBuilder {
    private final Material genMaterial;
    private String genName;
    private List<String> genLore;
    private double price;
    private int genDelay;
    private int tier;

    public GenBuilder(ConfigurationSection section) {
        setName(section.getString("display.name"));
        this.genMaterial = getMaterial(section.getString("material", "STONE"));
        setPrice(section.getInt("price"));
        setDelay(section.getInt("delay"));
        setLore(section.getStringList("display.lore"));
        setTier(section.getInt("tier"));
    }

    public Generator build() {
        return new Generator()
                .setGeneratorItem(buildItem())
                .setGeneratorTier(tier)
                .setGeneratorDelay(genDelay)
                .setGeneratorPrice(price);
    }
    @Override
    public ItemStack buildItem() {
        ItemStack item = new ItemStack(genMaterial);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        if (genName != null) meta.setDisplayName(genName);
        if (genLore != null) meta.setLore(genLore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    public void setDelay(int delay) {
        this.genDelay = delay;
    }

    @Override
    public Material getMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning(String.format("Failed to load the material for generator %s! Reverting to material STONE.",genName));
            return Material.STONE;
        }
    }

    @Override
    public void setName(String name) {
        if (name != null) {
            this.genName = ChatColor.translateAlternateColorCodes('&', name);
        }
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore != null) {
            ListIterator<String> iterator = lore.listIterator();
            while (iterator.hasNext()) {
                iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next()
                                .replace("%price%", String.valueOf(price))
                                .replace("%delay%",String.valueOf(genDelay))));
            }
            this.genLore = lore;
        }
    }
}