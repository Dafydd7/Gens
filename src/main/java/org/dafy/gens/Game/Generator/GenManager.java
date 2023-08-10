package org.dafy.gens.Game.Generator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.dafy.gens.Game.ItemSpawner;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class GenManager {
    private final Gens plugin;
    private final ItemSpawner itemSpawner;

    public GenManager(Gens plugin) {
        this.itemSpawner = plugin.getItemSpawner();
        this.plugin = plugin;
    }


    public final Map<Integer, Generator> tierToGeneratorMap = new HashMap<>();

    public void initGenBuilder() {
        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection rewardsSection = config.getConfigurationSection("Generators.");

        tierToGeneratorMap.clear();

        if (rewardsSection == null) {
            plugin.getLogger().log(Level.WARNING, "Unable to initialize generator template - data null");
            return;
        }

        rewardsSection.getKeys(false).forEach(key -> {
            Generator genBuilder = new GenBuilder(rewardsSection.getConfigurationSection(key)).build();
            tierToGeneratorMap.put(genBuilder.getTier(), genBuilder);
        });
    }

    public boolean isMaxTier(int tier) {
        return tier == tierToGeneratorMap.size();
    }

    public Generator createGenerator(Location location, int tier) {
        if (location == null) return null;
        Generator generator = tierToGeneratorMap.get(tier);
        generator.setGenLocation(location);
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
        newGenerator.setGenLocation(location);
        itemSpawner.addAndRemoveGen(generator, newGenerator);
        user.addAndRemove(generator, newGenerator);
    }

    public void removeGenerator(Generator generator, Player player) {
        Location location = generator.getGenLocation();
        if (location == null) return;

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user == null) return;

        itemSpawner.removeActiveGenerator(generator);
        location.getBlock().setType(Material.AIR);
        location.getWorld().dropItemNaturally(location, plugin.getBlockManager().addItemPersistentData(generator.getGenItem(),"GeneratorItem",generator.getTier()));
        user.removeGenerator(generator);
        plugin.getBlockManager().removeBlockPersistentData(generator.getGenLocation().getBlock(),"Generator");
    }
}