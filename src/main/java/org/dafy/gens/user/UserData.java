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
        plugin.getConfigManager().loadUserConfig(uuid,  true);
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
}
