package org.dafy.gens;

import co.aikar.commands.BukkitCommandManager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.dafy.gens.Game.Block.BlockInteraction;
import org.dafy.gens.Game.Block.BlockManager;
import org.dafy.gens.Game.Block.BlockPlace;
import org.dafy.gens.Game.Block.CancelPiston;
import org.dafy.gens.Game.ConnectionListener;
import org.dafy.gens.Game.Generator.GenManager;
import org.dafy.gens.Game.Generator.GenUpgrade;
import org.dafy.gens.Game.ItemSpawner;
import org.dafy.gens.commands.DebugCMDS;
import org.dafy.gens.commands.Reload;
import org.dafy.gens.commands.Sell;
import org.dafy.gens.config.ConfigManager;
import org.dafy.gens.Game.economy.Eco;
import org.dafy.gens.Game.events.GensEvent;
import org.dafy.gens.user.UserData;
import org.dafy.gens.user.UserManager;

@Getter
public final class Gens extends JavaPlugin {
    private Eco eco;
    private GensEvent gensEvent;
    private ItemSpawner itemSpawner;
    private BlockManager blockManager;
    private UserManager userManager;
    private UserData userTEMP;
    private ConfigManager configManager;
    private GenManager genManager;
    private GenUpgrade genUpgrade;

    private int taskId;
    @Override
    public void onEnable() {
        saveDefaultConfig();

        eco = new Eco(this);
        eco.initEconomy();

        itemSpawner = new ItemSpawner(this);
        taskId = itemSpawner.runTaskTimer(this, 0, 100).getTaskId();

        genManager = new GenManager(this);
        genManager.initGenBuilder();

        userManager = new UserManager();
        userTEMP = new UserData(this);

        configManager = new ConfigManager(this);

        gensEvent = new GensEvent(this);
        gensEvent.init();

        blockManager = new BlockManager();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockPlace(this), this);
        pm.registerEvents(new BlockInteraction(this), this);
        pm.registerEvents(new CancelPiston(this), this);
        pm.registerEvents(new ConnectionListener(this), this);
        pm.registerEvents(genUpgrade = new GenUpgrade(this),this);

        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new Sell());
        manager.registerCommand(new DebugCMDS());
        manager.registerCommand(new Reload());
    }

    @Override
    public void onDisable() {
        userTEMP.saveAllUsers();
        gensEvent.stopModeTimer();
        Bukkit.getScheduler().cancelTask(taskId);
    }
}

