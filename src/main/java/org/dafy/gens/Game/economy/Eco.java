package org.dafy.gens.Game.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.dafy.gens.Gens;

import java.util.logging.Logger;

public class Eco {
    private final Gens plugin;
    public Eco(Gens plugin){
        this.plugin = plugin;
    }
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;

    public void initEconomy() {
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", plugin.getDescription().getName()));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
}

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
