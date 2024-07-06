package org.dafy.gens.user;

import org.bukkit.entity.Player;
import org.dafy.gens.Gens;
import java.io.File;
import java.io.IOException;
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

    public void saveUser(UUID uuid, boolean remove) {
        File file = new File(plugin.getDataFolder() + "/users/" + uuid.toString() + ".yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        }catch (IOException ignored){
            plugin.getLogger().severe(String.format("Failed to create a file for user %s",uuid));
        }
        plugin.getConfigManager().saveUserConfig(file, uuid);
        if(remove) plugin.getUserManager().removeUser(uuid);
    }

    public void saveAllUsers() {
        //TODO - lazily saved all - clear the map after
        plugin.getUserManager().getUserKeys().forEach(uuid -> saveUser(uuid,false));
    }
}
