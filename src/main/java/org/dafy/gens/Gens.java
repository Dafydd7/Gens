package org.dafy.gens;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitMessageFormatter;
import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import org.dafy.gens.bskyblock.IslandDelete;
import org.dafy.gens.commands.*;
import org.dafy.gens.game.managers.*;
import org.dafy.gens.game.listeners.*;
import org.dafy.gens.game.upgrader.UpgradeGUI;
import org.dafy.gens.utility.Language;
import org.dafy.gens.utility.PAPIExpansion;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.ItemCreator;
import org.dafy.gens.game.sellwand.SellwandListener;
import org.dafy.gens.game.upgrader.UpgradeManager;
import org.dafy.gens.game.generator.GeneratorItemSpawner;
import org.dafy.gens.config.ConfigManager;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.game.shop.ShopManager;
import org.dafy.gens.game.shop.GenShop;
import org.dafy.gens.user.UserData;
import org.dafy.gens.user.UserManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public final class Gens extends JavaPlugin {
    private Economy economy = null;
    private GensEvent gensEvent;
    private GeneratorItemSpawner generatorItemSpawner;
    private UserData userData;
    private ItemCreator itemCreator;
    private UpgradeGUI upgradeGUI;
    private BlockManager blockManager;
    private UserManager userManager;
    private ConfigManager configManager;
    private GeneratorManager generatorManager;
    private UpgradeManager upgradeManager;
    private ShopManager shopManager;
    private int taskId;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        userManager = new UserManager();
        //TODO place me somewhere relevant
        File file = new File(getDataFolder(),"messages.yml");
        if(!file.exists()){
            saveResource("messages.yml",false);
        }
        Language.setLanguageConfig(YamlConfiguration.loadConfiguration(file));
        setupEconomy();
        blockManager = new BlockManager();
        generatorManager = new GeneratorManager(this);
        gensEvent = new GensEvent(this);
        generatorItemSpawner = new GeneratorItemSpawner(this);
        taskId = generatorItemSpawner.runTaskTimer(this, 0, 20).getTaskId();
        shopManager = new ShopManager(this);
        itemCreator = new ItemCreator(this);
        itemCreator.initBuilders();
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
        userData.saveAllUsers();
        gensEvent.stopModeTimer();
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void registerListeners(){
        PluginManager pm = getServer().getPluginManager();
        Arrays.asList(
                new BlockPlace(this),
                upgradeGUI = new UpgradeGUI(this),
                new BlockInteraction(this),
                new BlockPiston(this),
                new PlayerConnection(this),
                new UpgradeManager(),
                new GenShop(this),
                new BlockExplode(this),
                new BlockBreak(this),
                new BlockBurn(this),
                new IslandDelete(this),
                new SellwandListener(this)
        ).forEach(listener -> pm.registerEvents(listener, this));
    }

    public void registerCommands(){
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.setFormat(MessageType.HELP, new BukkitMessageFormatter(ChatColor.AQUA, ChatColor.DARK_AQUA, ChatColor.WHITE));
        List<BaseCommand> commandList = Arrays.asList(
                new Help(),
                new DebugCMDS(),
                new Reload(),
                new Shop(),
                new Stats(),
                new GeneratorLimit());
        if(getConfig().getBoolean("selling-enabled",false)){
            commandList.add(new Sell());
        }
        commandList.forEach(commandManager::registerCommand);
    }

    public void registerPlaceholders(){
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPIExpansion(this).register();
        } else {
            getLogger().warning("PlaceholderAPI not found, will not register hooks.");
        }
    }
    public void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) economy = rsp.getProvider();
    }
}

