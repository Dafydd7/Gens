package org.dafy.gens;

import co.aikar.commands.BukkitCommandManager;

import co.aikar.commands.annotation.Dependency;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.dafy.gens.bskyblock.IslandDelete;
import org.dafy.gens.commands.*;
import org.dafy.gens.game.block.*;
import org.dafy.gens.game.ConnectionListener;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.ItemCreator;
import org.dafy.gens.game.upgrader.CloseUpgrader;
import org.dafy.gens.game.upgrader.GenUpgrader;
import org.dafy.gens.game.upgrader.UpgradeManager;
import org.dafy.gens.game.ItemSpawner;
import org.dafy.gens.config.ConfigManager;
import org.dafy.gens.game.economy.GenEconomy;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.game.shop.ShopManager;
import org.dafy.gens.game.shop.GenShop;
import org.dafy.gens.user.UserData;
import org.dafy.gens.user.UserManager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public final class Gens extends JavaPlugin {
    @Dependency
    private GenEconomy genEconomy;
    private GensEvent gensEvent;
    private ItemSpawner itemSpawner;
    private UserData userTEMP;
    private ItemCreator itemCreator;

    private BlockManager blockManager;
    private UserManager userManager;
    private ConfigManager configManager;
    private GenManager genManager;
    private UpgradeManager upgradeManager;
    private ShopManager shopManager;
    private int taskId;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Initialize and inject dependencies
        genEconomy = new GenEconomy(this);
        genEconomy.initEconomy();

        itemSpawner = new ItemSpawner();
        taskId = itemSpawner.runTaskTimer(this, 0, 100).getTaskId();

        genManager = new GenManager(this);
        shopManager = new ShopManager(this);

        itemCreator = new ItemCreator(this);
        itemCreator.initBuilders();

        userManager = new UserManager();
        userTEMP = new UserData(this);

        configManager = new ConfigManager(this);

        gensEvent = new GensEvent(this);
        gensEvent.init();

        blockManager = new BlockManager();

        upgradeManager = new UpgradeManager();

        // Register listeners and commands
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        CompletableFuture.runAsync(()->userTEMP.saveAllUsers());
        gensEvent.stopModeTimer();
        Bukkit.getScheduler().cancelTask(taskId);
    }
    public void registerListeners(){
        PluginManager pm = getServer().getPluginManager();
        List<Listener> listeners = Arrays.asList(
                new BlockPlace(this),
                new BlockInteraction(this),
                new BlockPiston(this),
                new ConnectionListener(this),
                new UpgradeManager(),
                new GenShop(this),
                new GenUpgrader(this),
                new BlockExplode(this),
                new BlockBreak(this),
                new CloseUpgrader(this),
                new BlockBurn(this),
                new IslandDelete(this)
        );
        listeners.forEach(listener -> pm.registerEvents(listener, this));
    }
    public void registerCommands(){
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new Sell());
        manager.registerCommand(new DebugCMDS());
        manager.registerCommand(new Reload());
        manager.registerCommand(new Shop());
        manager.registerCommand(new Stats());
    }
}

