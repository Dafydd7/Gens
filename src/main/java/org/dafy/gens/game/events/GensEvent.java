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
    public GensEvent(Gens plugin){
        this.plugin = plugin;
        genManager = plugin.getGenManager();
    }
    @Getter
    private EventState activeMode = EventState.INACTIVE;
    private int modeTaskId;

    public void init() {
        // Start the mode selection timer (15 minutes)
        startModeSelectionTimer();
    }

    private void startModeSelectionTimer() {
        // Cancel the existing mode selection task if it's running
        if (modeTaskId != 0) {
            Bukkit.getScheduler().cancelTask(modeTaskId);
        }
        // Start a new mode selection task (15 minutes interval)
        int interval = plugin.getConfig().getInt("Mode-Selection-Interval",1);
        modeTaskId = Bukkit.getScheduler().runTaskLater(plugin, this::changeActiveModeRandomly, 20L * 60 * interval).getTaskId();
        broadcastMessage(plugin.getConfig().getString("Inactive-Event-Message", "&aEvent: Now inactive."));
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
                    broadcastMessage(plugin.getConfig().getString("Upgrade-Event-Message", "&aEvent: Upgrade drop"));
            case DOUBLE_DROP ->
                    broadcastMessage(plugin.getConfig().getString("Double-Event-Message", "&aEvent: Double drop."));
            case SELL_EVENT ->
                    broadcastMessage(plugin.getConfig().getString("Sell-Event-Message", "&aEvent: Sell Event."));
        }
            startGeneratorTask();
    }

    private void broadcastMessage(String msg){
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',msg));
    }

    public String getEventName() {
        switch (activeMode) {
            case UPGRADE_DROP -> {
                return "Upgrade Drop.";
            }
            case DOUBLE_DROP -> {
                return "Double Drop.";
            }
            case SELL_EVENT -> {
                return "Sell Event.";
            }
            case INACTIVE -> {
                return "Inactive.";
            }
        }
        return null;
    }

    private void startGeneratorTask() {
        int eventInterval = plugin.getConfig().getInt("Event-Interval",1);
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

