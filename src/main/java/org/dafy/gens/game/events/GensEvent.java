package org.dafy.gens.game.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;

public class GensEvent {
    private final Gens plugin;
    private final GenManager genManager;
    private final String inactiveEventMessage;
    private final String upgradeEventMessage;
    private final String doubleEventMessage;
    private final String sellEventMessage;
    private final int modeIntervalInteger;
    private final int eventInterval;

    public GensEvent(Gens plugin){
        this.plugin = plugin;
        genManager = plugin.getGenManager();
        //init event strings
        inactiveEventMessage = plugin.getConfig().getString("Inactive-Event-Message", "&aEvent: Now inactive.");
        upgradeEventMessage = plugin.getConfig().getString("Upgrade-Event-Message", "&aEvent: Upgrade drop");
        doubleEventMessage = plugin.getConfig().getString("Double-Event-Message", "&aEvent: Double drop.");
        sellEventMessage = plugin.getConfig().getString("Sell-Event-Message", "&aEvent: Sell Event.");
        //& init mode intervals.
        modeIntervalInteger = plugin.getConfig().getInt("Mode-Selection-Interval",1);
        eventInterval = plugin.getConfig().getInt("Event-Interval",1);
        init();
    }

    @Getter
    private EventState activeMode = EventState.INACTIVE;
    private int modeTaskId;

    private void init() {
        // Start the mode selection timer (15 minutes)
        startModeSelectionTimer();
    }

    private void startModeSelectionTimer() {
        // Cancel the existing mode selection task if it's running
        if (modeTaskId != 0) {
            Bukkit.getScheduler().cancelTask(modeTaskId);
        }
        // Start a new mode selection task (15 minutes interval)
        modeTaskId = Bukkit.getScheduler().runTaskLater(plugin, this::changeActiveModeRandomly, 20L * 60 * modeIntervalInteger).getTaskId();
        broadcastMessage(inactiveEventMessage);
    }

    public void getModeAndDrop(Generator generator){
        switch (activeMode){
            //Drops the item from the tier above.
            case UPGRADE_DROP -> genManager.dropUpgradedNaturally(generator);
            case DOUBLE_DROP -> genManager.dropItemNaturally(generator,2);
            case INACTIVE, SELL_EVENT -> genManager.dropItemNaturally(generator,1);
        }
    }

    private void stopEvent() {
        // Set the active mode state to INACTIVE.
        setActiveMode(EventState.INACTIVE);

        // Start the mode selection timer again.
        startModeSelectionTimer();
    }

    private void changeActiveModeRandomly() {
        EventState newMode = EventState.getRandomMode();
        setActiveMode(newMode);

        switch (activeMode) {
            case UPGRADE_DROP ->
                    broadcastMessage(upgradeEventMessage);
            case DOUBLE_DROP ->
                    broadcastMessage(doubleEventMessage);
            case SELL_EVENT ->
                    broadcastMessage(sellEventMessage);
        }
            startGeneratorTask();
    }

    private void broadcastMessage(String msg){
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',msg));
    }

    public String getEventName() {
        return switch (activeMode) {
            case UPGRADE_DROP -> "Upgrade Drop.";
            case DOUBLE_DROP -> "Double Drop.";
            case SELL_EVENT -> "Sell Event.";
            default -> "Inactive.";
        };
    }

    private void startGeneratorTask() {
        Bukkit.getScheduler().runTaskLater(plugin, this::stopEvent, 20L * 60 * eventInterval);
    }

    private void setActiveMode(EventState newMode) {
        activeMode = newMode;
    }

    public boolean isSellMode(){
        return activeMode.equals(EventState.SELL_EVENT);
    }


    public void stopModeTimer() {
        Bukkit.getScheduler().cancelTask(modeTaskId);
        setActiveMode(EventState.INACTIVE);
    }
}

