package org.dafy.gens.utility;

import com.google.common.base.Preconditions;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private final ItemStack itemStack;

    public ItemBuilder(Material material){
        this(material, 1);
    }

    public ItemBuilder(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material, int amount){
        itemStack = new ItemStack(material, amount);
    }

    public ItemBuilder(ConfigurationSection section){
        Preconditions.checkNotNull(section,"Configuration section cannot be null");
        Material material = Material.valueOf(section.getString("Material","STONE").toUpperCase());
        itemStack = new ItemStack(material);
        this.setDisplayName(section.getString("Name",""));
        this.setLore(section.getStringList("Lore"));
        this.setItemAmount(section.getInt("Amount",1));
        if(section.getBoolean("Enchant-Glint",false)) glow();
        if(section.contains("Model-Data")) setModelData(section.getInt("Model-Data"));
    }

    public ItemBuilder(Material material, int amount, String itemName){
        itemStack = new ItemStack(material, amount);
        setDisplayName(itemName);
    }

    /**
     * Change the durability of the item.
     * @param dur The durability to set it to.
     */
    public ItemBuilder setDurability(int dur){
        ItemMeta itemMeta = itemStack.getItemMeta();
        Damageable damageable = (Damageable) itemMeta;
        if(damageable == null) return this;
        damageable.setDamage(dur);
        itemStack.setItemMeta(damageable);
        return this;
    }

    public ItemBuilder setModelData(int modelData){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setCustomModelData(modelData);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDisplayName(String name){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level){
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment){
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setPersistentDataString(@NotNull NamespacedKey key, String value){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING,value);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder glow(){
        itemStack.addUnsafeEnchantment(Enchantment.LURE,1);
        return addItemFlag(ItemFlag.HIDE_ENCHANTS);
    }

    /**
     * Set the skull owner for the item. Works on skulls only.
     * @param owner The name of the skull's owner.
     */
    public ItemBuilder setSkullOwner(String owner){
        try{
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwner(owner);
            itemStack.setItemMeta(skullMeta);
        }catch(ClassCastException ignored){}
        return this;
    }
    /**
     * Add an enchant to the item.
     * @param enchantment The enchant to add
     * @param level The level
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, level, true);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    public ItemBuilder setInfinityDurability(){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setItemAmount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(String... lore){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        for (String string : lore){
            itemLore.add(ChatColor.translateAlternateColorCodes('&',string));
        }
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(List<String> lore) {
        if(lore == null || lore.isEmpty()) return this;
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> itemLore = new ArrayList<>();
        for (String string : lore) {
            itemLore.add(ChatColor.translateAlternateColorCodes('&',string));
        }
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Re-sets the lore with placeholders translated.
     * @param player The target player for the placeholders
     */
    public ItemBuilder setPlaceholderAPILore(Player player) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return this;
        List<String> lore = itemMeta.getLore();
        if (lore == null || lore.isEmpty()) return this; // Return early if lore is null or empty
        List<String> updatedLore = new ArrayList<>(lore.size());
        for (String line : lore) {
            updatedLore.add(PlaceholderAPI.setPlaceholders(player, ChatColor.translateAlternateColorCodes('&', line)));
        }
        itemMeta.setLore(updatedLore);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack build(){
        return itemStack;
    }
}



