package org.dafy.gens.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.dafy.gens.game.managers.BlockManager;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;
import org.dafy.gens.user.UserManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class ConfigManager {

    private final Gens plugin;
    private final UserManager userManager;
    private final GeneratorManager generatorManager;
    private final BlockManager blockManager;

    public ConfigManager(Gens plugin) {
        this.plugin = plugin;
        userManager = plugin.getUserManager();
        generatorManager = plugin.getGeneratorManager();
        blockManager = plugin.getBlockManager();
    }

    public void saveUserConfig(File file, UUID uuid) {
        FileConfiguration userYaml = new YamlConfiguration();
        User user = userManager.getUser(uuid);
        if (user == null) {
            plugin.getLogger().log(Level.WARNING, "User not found: " + uuid);
            return;
        }
        ConfigurationSection section = userYaml.createSection("User." + uuid);
        section.set(ConfigKeys.GEN_PLACED, user.getGensPlaced());
        section.set(ConfigKeys.GEN_LIMIT, user.getGenLimit());

        if (!user.getGenerators().isEmpty()) {
            int i = 0;
            for (Generator generator : user.getGenerators()) {
                String generatorPath = String.format(ConfigKeys.GENERATORS_PATH, i);
                section.set(generatorPath + ".Location", generator.getGeneratorLocation());
                section.set(generatorPath + ".Tier", generator.getTier());
                section.set(generatorPath + ".Island-UUID", generator.getIslandUUID());
                i++;
            }
        }
        try {
            userYaml.save(file);
            plugin.getLogger().log(Level.INFO, "Saved user: " + uuid);
        } catch (IOException e) {
            String errorMessage = "Failed to save data for player: " + uuid + ". Reason: " + e.getMessage();
            plugin.getLogger().log(Level.WARNING, errorMessage);
        }
    }

    public void loadUserConfig(Player onlinePlayer, boolean cache) {
        final UUID uuid = onlinePlayer.getUniqueId();
        User user = new User(uuid);
        File file = new File(plugin.getDataFolder() + "/users/" + uuid + ".yml");
        if (!file.exists()) {
            createUser(file,user);
            return;
        }
        FileConfiguration userYaml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = userYaml.getConfigurationSection("User." + uuid);
        if (section == null) return;
        user.setGensPlaced(section.getInt(ConfigKeys.GEN_PLACED));
        user.setGenLimit(section.getInt(ConfigKeys.GEN_LIMIT));

        ConfigurationSection genSection = section.getConfigurationSection("Generators.");
        if (genSection != null) {
            for (String key : genSection.getKeys(false)) {
                ConfigurationSection genSubSection = genSection.getConfigurationSection(key);
                if (genSubSection == null) continue;
                Location location = (Location) genSubSection.get("Location");
                Block block = location.getBlock();
                if(!blockManager.hasBlockPersistentData(block,"Generator")) {
                    section.set(key,null); //Deletes the key, as the generator no longer exists.
                    user.decrementGensPlaced(); //Bring back their stats
                    blockManager.removeBlockPersistentData(block,"Generator"); //Remove the NBT, so other blocks can now be placed there again.
                    continue;
                } // Skip over generator, if location is somehow null, or the block has been removed.
                int tier = genSubSection.getInt("Tier");
                Generator generator = generatorManager.createGenerator(location, tier);
                generator.setIslandUUID(genSubSection.getString("Island-UUID"));
                user.getGenerators().add(generator);
            }
        }
        if (!cache) return;
        user.setPlayer(Bukkit.getPlayer(uuid));
        userManager.cacheUser(user);
    }

    public void createUser(File file, User user) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Unable to create user file.");
            return;
        }
        user.setPlayer(Bukkit.getPlayer(user.getUuid()));
        userManager.cacheUser(user);
    }
}