package org.dafy.gens.game.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.dafy.gens.Gens;

public class GensEvent {
    private final Gens plugin;
    public GensEvent(Gens plugin){
        this.plugin = plugin;
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
        modeTaskId = Bukkit.getScheduler().runTaskLater(plugin, this::changeActiveModeRandomly, 20 * 60 * 5).getTaskId();
        Bukkit.broadcastMessage("Active now: " + getActiveMode().toString());
    }


    private void stopEvent() {
        // Set the active mode state to INACTIVE to pause the generator task during mode off time
        setActiveMode(EventState.INACTIVE);

        // Start the mode selection timer again for the next 15 minutes
        startModeSelectionTimer();
    }

    private void changeActiveModeRandomly() {
        EventState newMode = EventState.getRandomMode();
        setActiveMode(newMode);

        Bukkit.broadcastMessage("New mode: " + newMode.toString());

        // Start the generator task for the current active mode
        startGeneratorTask();
    }
    private void startGeneratorTask() {
        switch (activeMode) {
            case SELL_EVENT:
                // TODO: Perform generator event action for the player in DOUBLE_SELL mode
                break;
             case UPGRADE_DROP:
                 // TODO: Perform generator event action for the player in UPGRADED_DROP mode
                 break;
        }
        Bukkit.getScheduler().runTaskLater(plugin, this::stopEvent, 20 * 60);
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

