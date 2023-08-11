package org.dafy.gens.game.block;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockManager{

    public BlockManager(){
    }

    public static final String GENERATOR_KEY = "Generator";
    public ItemStack addItemPersistentData(ItemStack itemStack, String key, Integer tier) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger(key, tier);
        nbtItem.applyNBT(itemStack);
        return itemStack;
    }
    public boolean hasItemPersistentData(ItemStack itemStack, String key) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasTag(key);
    }
    public void addBlockPersistentData(Block block, String key, Integer tier) {
        NBTBlock nbtBlock = new NBTBlock(block);
        nbtBlock.getData().setInteger(key, tier);
    }

    public void removeBlockPersistentData(Block block, String key) {
        NBTBlock nbtBlock = new NBTBlock(block);
        if(!nbtBlock.getData().hasTag(key)) return;
        nbtBlock.getData().clearNBT();
    }
    public boolean hasBlockPersistentData(Block block, String key) {
        NBTBlock nbtBlock = new NBTBlock(block);
        return nbtBlock.getData().hasTag(key);
    }
    public Integer getItemTier(ItemStack itemStack,String key) {
        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.getInteger(key);
    }
}
