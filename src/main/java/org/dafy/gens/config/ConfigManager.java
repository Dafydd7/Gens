package org.dafy.gens.config;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;
import org.dafy.gens.user.UserManager;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;

public class ConfigManager {

    private final Gens plugin;
    private final UserManager userManager;
    private final GenManager genManager;

    public ConfigManager(Gens plugin) {
        this.plugin = plugin;
        userManager = plugin.getUserManager();
        genManager = plugin.getGenManager();
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
                section.set(generatorPath + ".Location", generator.getGenLocation());
                section.set(generatorPath + ".Tier", generator.getTier());
                section.set(generatorPath + ".Island-UUID", generator.getIslandUUID());
                plugin.getItemSpawner().removeActiveGenerator(generator);
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

    public void loadUserConfig(UUID uuid, boolean cache) {
            User user = new User(uuid);
            File file = new File(plugin.getDataFolder() + "/users/" + uuid.toString() + ".yml");
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
                    int tier = genSubSection.getInt("Tier");
                    Generator generator = genManager.createGenerator(location, tier);
                    user.getGenerators().add(generator);
                    generator.setIslandUUID(genSubSection.getString("Island-UUID"));
                    plugin.getItemSpawner().addActiveGenerator(generator);
                }
            }
            if (cache) userManager.cacheUser(user);
    }

    //Added this method for now; need changing ;-;
    public void loadOfflinePlayer(UUID uuid, Consumer<User> userResult, boolean cache) {
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            User user = new User(uuid);

            File file = new File(plugin.getDataFolder() + "/users/" + uuid.toString() + ".yml");

            FileConfiguration userYaml = YamlConfiguration.loadConfiguration(file);
            ConfigurationSection section = userYaml.getConfigurationSection("User." + uuid);
            if (section == null) return null;

            user.setGensPlaced(section.getInt(ConfigKeys.GEN_PLACED));
            user.setGenLimit(section.getInt(ConfigKeys.GEN_LIMIT));

            ConfigurationSection genSection = section.getConfigurationSection("Generators.");
            if (genSection != null) {
                for (String key : genSection.getKeys(false)) {
                    ConfigurationSection genSubSection = genSection.getConfigurationSection(key);
                    if (genSubSection == null) continue;
                    Location location = (Location) genSubSection.get("Location");
                    int tier = genSubSection.getInt("Tier");
                    Generator generator = genManager.createGenerator(location, tier);
                    user.getGenerators().add(generator);
                    generator.setIslandUUID(genSubSection.getString("Island-UUID"));
                    plugin.getItemSpawner().addActiveGenerator(generator);
                }
            }
            if (cache) userManager.cacheUser(user);
            return user;
        });
        if (userResult != null) future.thenAccept(userResult);
    }

    public void createUser(File file, User user) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().log(Level.WARNING, "Unable to create user file.");
                return;
            }
            userManager.cacheUser(user);
        }
}