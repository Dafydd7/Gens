package org.dafy.gens.Game.Generator;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
@Getter
public class GenBuilder {
    private final Material genMaterial;
    private final Material dropMaterial;
    private String genName;
    private String dropName;
    private List<String> genLore;
    private List<String> dropLore;
    private int price;
    private int genDelay;
    private int tier;

    public GenBuilder(ConfigurationSection section) {
        this.genMaterial = getMaterial(section.getString("material", "stone"));
        this.dropMaterial = getMaterial(section.getString(".drop.material", "stone"));

        setGenName(section.getString("display.name"));
        setGenLore(section.getStringList("display.lore"));
        setPrice(section.getInt("price"));
        genDelay(section.getInt("delay"));
        setTier(section.getInt("tier"));
        setDropName(section.getString(".drop.display.name"));
        setDropLore(section.getStringList(".drop.display.lore"));
    }

    public void setGenName(String name) {
        if (name != null) {
            this.genName = ChatColor.translateAlternateColorCodes('&', name);
        }
    }

    public void setDropName(String name) {
        if (name != null) {
            this.dropName = ChatColor.translateAlternateColorCodes('&', name);
        }
    }

    public void setGenLore(List<String> lore) {
        if (lore != null) {
            ListIterator<String> iterator = lore.listIterator();
            while (iterator.hasNext()) {
                iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next()));
            }
            this.genLore = lore;
        }
    }

    public void setDropLore(List<String> lore) {
        if (lore != null) {
            ListIterator<String> iterator = lore.listIterator();
            while (iterator.hasNext()) {
                iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next()));
            }
            this.dropLore = lore;
        }
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void genDelay(int delay) {
        this.genDelay = delay;
    }

    public Generator build() {
        Generator generator = new Generator();
        generator.setGenItem(buildItem(genMaterial, genName, genLore));
        generator.setDelay(genDelay);
        generator.setTier(tier);
        generator.setDropItem(buildItem(dropMaterial, dropName, dropLore));
        return generator;
    }

    private ItemStack buildItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        if (name != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (lore != null) {
            ListIterator<String> iterator = lore.listIterator();
            while (iterator.hasNext()) {
                iterator.set(ChatColor.translateAlternateColorCodes('&', iterator.next()));
            }
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    private Material getMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return Material.STONE;
        }
    }
}