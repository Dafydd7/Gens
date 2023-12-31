package org.dafy.gens.game.generator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.spawner.SpawnerManager;
import org.dafy.gens.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenManager {
    private final Gens plugin;
    private final SpawnerManager spawnerManager;

    public GenManager(Gens plugin) {
        this.spawnerManager = plugin.getSpawnerManager();
        this.plugin = plugin;
    }
    private final Map<Integer, Generator> tierToGeneratorMap = new HashMap<>();
    public void clearGenerators(){
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

    //TODO debug - remove me or use me
    public Generator genFromTier(int tier){
        return tierToGeneratorMap.get(tier > genCount() ? 1:tier);
    }

    public Generator createGenerator(Location location,int tier) {
        Generator template = tierToGeneratorMap.get(tier);
        Generator generator = new Generator();
        generator.setTier(tier);
        generator.setGenLocation(location);
        generator.setGenItem(template.getGenItem());
        generator.setDropItem(template.getDropItem());
        generator.setDelay(template.getDelay());
        return generator;
    }

    public void updateGenerator(Generator generator, Player player) {
        Location location = generator.getGenLocation();
        if (location == null) return;

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        int newTier = generator.getTier() + 1;
        Generator newGenerator = createGenerator(location,newTier);
        location.getBlock().setType(newGenerator.getGenItem().getType());
        user.addAndRemove(generator, newGenerator);
        spawnerManager.addAndRemoveGen(generator, newGenerator);
    }

    //Method called when island is deleted/reset, as all blocks will be deleted anyway.
    public void deleteIslandGenerator(Generator generator, UUID uuid) {
        User user = plugin.getUserManager().getUser(uuid);
        if (user == null) return;

        spawnerManager.removeActiveGenerator(generator);
        user.removeGenerator(generator);
        user.removePlaced();
        plugin.getBlockManager().removeBlockPersistentData(generator.getGenLocation().getBlock(),"Generator");
    }

    public void removeGenerator(Generator generator, UUID uuid) {
        Location location = generator.getGenLocation();
        if (location == null) return;

        User user = plugin.getUserManager().getUser(uuid);
        if (user == null) return;

        spawnerManager.removeActiveGenerator(generator);
        location.getBlock().setType(Material.AIR);
        location.getWorld().dropItemNaturally(location, plugin.getBlockManager().addItemPersistentData(generator.getGenItem(),"GeneratorItem",generator.getTier()));
        user.removeGenerator(generator);
        user.removePlaced();
        plugin.getBlockManager().removeBlockPersistentData(generator.getGenLocation().getBlock(),"Generator");
    }
    public void dropItemNaturally(Generator generator,int amount){
        if(!delayReady(generator.getDelay())){
            generator.minusDelay();
            return;
        }
        Location location = generator.getGenLocation();
        ItemStack itemStack = generator.getDropItem();
        if(itemStack == null || location == null) return;
        itemStack.setAmount(amount);
        location.getWorld().dropItemNaturally(location,itemStack);
        generator.setDelay(genFromTier(generator.getTier()).getDelay());
    }
    public void dropUpgradedNaturally(Generator generator){
        if(!delayReady(generator.getDelay())){
            generator.minusDelay();
            return;
        }
        Location location = generator.getGenLocation();
        if(generator.getDropItem() == null || location == null) return;
        int newTier = Math.min(genCount(), generator.getTier()+1); //Compares the total tier count with the new tier, gets the lowest if newTier > count.
        location.getWorld().dropItemNaturally(location,tierToGeneratorMap.get(newTier).getDropItem());
        generator.setDelay(genFromTier(generator.getTier()).getDelay());
    }
    private boolean delayReady(int delay) {
        return delay <= 0;
    }
}