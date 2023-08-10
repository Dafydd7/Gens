package org.dafy.gens.user;

import org.dafy.gens.Gens;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class UserData {
    private final Gens plugin;

    public UserData(Gens plugin) {
        File file = new File(plugin.getDataFolder() + "/users/");
        file.mkdirs();
        this.plugin = plugin;
    }

    public void loadOrCreateUser(UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/users/" + uuid.toString() + ".yml");
        if (!file.exists()) {
            createUser(file, uuid);
        } else {
            plugin.getConfigManager().loadUserConfig(file, uuid);
        }
    }

    public void saveUser(UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/users/" + uuid.toString() + ".yml");
        if (!file.exists()) {
            file.mkdir();
        }
        plugin.getConfigManager().saveUserConfig(file, uuid);
        plugin.getUserManager().removeUser(uuid);
    }

    public void saveAllUsers() {
        plugin.getUserManager().getUserKeys().forEach(this::saveUser);
    }

    private void createUser(File file, UUID uuid) {
        User user = new User(uuid);
        user.setGenLimit(40);
        user.setGensPlaced(0);
        plugin.getUserManager().cacheUser(user);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, "Unable to create user file: " + uuid);
        }
    }
}
