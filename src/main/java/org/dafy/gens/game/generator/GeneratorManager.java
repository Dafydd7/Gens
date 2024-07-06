package org.dafy.gens.game.generator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.managers.BlockManager;
import org.dafy.gens.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GeneratorManager {
    private final Gens plugin;
    private final Map<Integer, Generator> tierToGeneratorMap = new HashMap<>();
    public GeneratorManager(Gens plugin) {
        this.plugin = plugin;
    }
    public void clearGenerators() {
        tierToGeneratorMap.clear();
    }
    public void setDropItem(int tier, ItemStack dropItem){
        tierToGeneratorMap.get(tier).setDropItem(dropItem);
    }
    public void addGenerator(int tier,Generator generator){
        tierToGeneratorMap.put(tier,generator);
    }

    public int genCount(){
        return tierToGeneratorMap.size();
    }

    public boolean isMaxTier(int tier) {
        return tier == genCount();
    }
    public double getPrice(int tier) {
        return genFromTier(tier).getPrice();
    }

    public Collection<Generator> getGenerators() {
        return tierToGeneratorMap.values();
    }
    public boolean isEmpty(){
        return tierToGeneratorMap.isEmpty();
    }
    public Generator genFromTier(int tier){
        return tierToGeneratorMap.get(Math.min(genCount(),tier));
    }

    public Generator createGenerator(Location location,int tier) {
        Generator template = tierToGeneratorMap.get(tier);
        return new Generator()
                .setTier(tier)
                .setGeneratorLocation(location)
                .setGeneratorItem(template.getGeneratorItem())
                .setDropItem(template.getDropItem())
                .setGeneratorDelay(template.getGeneratorDelay());
    }

    public void updateGenerator(Generator generator, Player player) {
        Location location = generator.getGeneratorLocation();
        if (location == null) return;
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;
        int newTier = generator.getTier() + 1;
        Generator newGenerator = createGenerator(location,newTier);
        location.getBlock().setType(newGenerator.getGeneratorItem().getType());
        user.addAndRemove(generator, newGenerator);
    }

    //Method called when island is deleted/reset, as all blocks will be deleted anyway.
    public void deleteIslandGenerator(Generator generator, UUID uuid) {
        User user = plugin.getUserManager().getUser(uuid);
        if (user == null) return;
        user.removeGenerator(generator);
        user.decrementGensPlaced();
        plugin.getBlockManager().removeBlockPersistentData(generator.getGeneratorLocation().getBlock(), BlockManager.GENERATOR_KEY);
    }

    public void removeGenerator(Generator generator, UUID uuid) {
        Location location = generator.getGeneratorLocation();
        if (location == null) return;
        User user = plugin.getUserManager().getUser(uuid);
        if (user == null) return;
        location.getBlock().setType(Material.AIR);
        location.getWorld().dropItemNaturally(location, plugin.getBlockManager().addItemPersistentData(generator.getGeneratorItem(),"GeneratorItem",generator.getTier()));
        user.removeGenerator(generator);
        user.decrementGensPlaced();
        plugin.getBlockManager().removeBlockPersistentData(generator.getGeneratorLocation().getBlock(),BlockManager.GENERATOR_KEY);
    }

    //TODO - Temp force remove to test - implement me into #removeGenerator()
    public void forceRemoveGenerator(Generator generator){
        generator.getGeneratorLocation().getBlock().setType(Material.AIR);
        plugin.getBlockManager().removeBlockPersistentData(generator.getGeneratorLocation().getBlock(),BlockManager.GENERATOR_KEY);
    }

    public void dropItemNaturally(Generator generator,int amount, boolean upgraded){
        if(generator.getGeneratorDelay() > 0){
            generator.decrementDelay();
            return;
        }
        ItemStack generatorDropItem;
        if(upgraded && !isMaxTier(generator.getTier())) {
            int nextGeneratorTier = generator.getTier() + 1;
            generatorDropItem = genFromTier(nextGeneratorTier).getDropItem();
        } else {
            generatorDropItem = generator.getDropItem();
        }
        if(generatorDropItem == null) return;
        generatorDropItem.setAmount(amount);
        Location location = generator.getGeneratorLocation();
        location.getWorld().dropItemNaturally(location,generatorDropItem);
        generator.setGeneratorDelay(genFromTier(generator.getTier()).getGeneratorDelay());
    }
}