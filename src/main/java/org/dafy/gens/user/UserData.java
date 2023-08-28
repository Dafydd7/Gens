package org.dafy.gens.user;

import org.bukkit.entity.Player;
import org.dafy.gens.Gens;
import java.io.File;
import java.util.UUID;

public class UserData {
    private final Gens plugin;

    public UserData(Gens plugin) {
        File file = new File(plugin.getDataFolder() + "/users/");
        file.mkdirs();
        this.plugin = plugin;
    }

    public void loadOrCreateUser(Player player) {
        plugin.getConfigManager().loadUserConfig(player, true);
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
