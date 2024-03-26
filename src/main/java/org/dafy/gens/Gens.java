package org.dafy.gens;

import co.aikar.commands.BukkitMessageFormatter;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.Dependency;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.dafy.gens.bskyblock.IslandDelete;
import org.dafy.gens.commands.*;
import org.dafy.gens.game.block.*;
import org.dafy.gens.game.spawner.SpawnerManager;
import org.dafy.gens.game.upgrader.UpgradeGUI;
import org.dafy.gens.placeholder.GenPlaceholders;
import org.dafy.gens.user.UserConnection;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.ItemCreator;
import org.dafy.gens.game.sellwand.SellwandListener;
import org.dafy.gens.game.upgrader.UpgradeManager;
import org.dafy.gens.game.spawner.ItemSpawner;
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
    private UserData userData;
    private ItemCreator itemCreator;
    private UpgradeGUI upgradeGUI;

    private BlockManager blockManager;
    private UserManager userManager;
    private ConfigManager configManager;
    private GenManager genManager;
    private UpgradeManager upgradeManager;
    private ShopManager shopManager;
    private SpawnerManager spawnerManager;
    private int taskId;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        genEconomy = new GenEconomy(this);
        genEconomy.initEconomy();
        blockManager = new BlockManager();
        spawnerManager = new SpawnerManager();
        genManager = new GenManager(this);
        gensEvent = new GensEvent(this);
        itemSpawner = new ItemSpawner(this);
        taskId = itemSpawner.runTaskTimer(this, 0, 20).getTaskId();
        shopManager = new ShopManager(this);
        itemCreator = new ItemCreator(this);
        itemCreator.initBuilders();
        userManager = new UserManager();
        userData = new UserData(this);
        configManager = new ConfigManager(this);
        upgradeManager = new UpgradeManager();
        new IslandDelete(this);
        registerListeners();
        registerCommands();
        registerPlaceholders();
    }

    @Override
    public void onDisable() {
        CompletableFuture.runAsync(()-> userData.saveAllUsers());
        gensEvent.stopModeTimer();
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void registerListeners(){
        PluginManager pm = getServer().getPluginManager();
        List<Listener> listeners = Arrays.asList(
                new BlockPlace(this),
                upgradeGUI = new UpgradeGUI(this),
                new BlockInteraction(this),
                new BlockPiston(this),
                new UserConnection(this),
                new UpgradeManager(),
                new GenShop(this),
                new BlockExplode(this),
                new BlockBreak(this),
                new BlockBurn(this),
                new IslandDelete(this),
                new SellwandListener(this)
        );
        listeners.forEach(listener -> pm.registerEvents(listener, this));
    }

    public void registerCommands(){
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, new BukkitMessageFormatter(ChatColor.AQUA, ChatColor.WHITE, ChatColor.GRAY));
        commandManager.registerCommand(new Help());
        commandManager.registerCommand(new Sell());
        commandManager.registerCommand(new DebugCMDS());
        commandManager.registerCommand(new Reload());
        commandManager.registerCommand(new Shop());
        commandManager.registerCommand(new Stats());
        commandManager.registerCommand(new Admin());
    }

    public void registerPlaceholders(){
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new GenPlaceholders(this).register();
            getLogger().info("Gens has been registered!");
        } else {
            getLogger().warning("PlaceholderAPI not found, will not register hooks.");
        }
    }
}

