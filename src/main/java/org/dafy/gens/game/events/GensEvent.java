package org.dafy.gens.game.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.utility.Language;

public class GensEvent {
    private final Gens plugin;
    private final GeneratorManager generatorManager;
    @Getter @Setter
    private EventState activeMode = EventState.INACTIVE;
    private final int modeIntervalInteger;
    private final int eventInterval;
    private int modeTaskId;

    public GensEvent(Gens plugin){
        this.plugin = plugin;
        generatorManager = plugin.getGeneratorManager();
        //init mode intervals.
        modeIntervalInteger = plugin.getConfig().getInt("Mode-Selection-Interval",1);
        eventInterval = plugin.getConfig().getInt("Event-Interval",1);
        startModeSelectionTimer();
    }

    private void startModeSelectionTimer() {
        // Cancel the existing mode selection task if it's running
        if (modeTaskId != 0) {
            Bukkit.getScheduler().cancelTask(modeTaskId);
        }
        // Start a new mode selection task (15 minutes interval)
        modeTaskId = Bukkit.getScheduler().runTaskLater(plugin, this::changeActiveModeRandomly, 20L * 60 * modeIntervalInteger).getTaskId();
        Bukkit.broadcastMessage(Language.EVENT_INACTIVE.toString());
    }

    public void getModeAndDrop(Generator generator){
        switch (activeMode){
            case UPGRADE_DROP -> generatorManager.dropItemNaturally(generator,1,true);
            case DOUBLE_DROP -> generatorManager.dropItemNaturally(generator,2,false);
            case INACTIVE, SELL_EVENT -> generatorManager.dropItemNaturally(generator,1,false);
        }
    }

    private void stopEvent() {
        // Set the active mode state to INACTIVE.
        setActiveMode(EventState.INACTIVE);
        // Start the mode selection timer again.
        startModeSelectionTimer();
    }

    private void changeActiveModeRandomly() {
        EventState newMode = EventState.getRandomEventMode();
        setActiveMode(newMode);
        switch (activeMode) {
            case UPGRADE_DROP -> Bukkit.broadcastMessage(Language.EVENT_UPGRADE.toString());
            case DOUBLE_DROP -> Bukkit.broadcastMessage(Language.EVENT_DOUBLE.toString());
            case SELL_EVENT -> Bukkit.broadcastMessage(Language.EVENT_SELL.toString());
        }
        startGeneratorTask();
    }

    private void startGeneratorTask() {
        Bukkit.getScheduler().runTaskLater(plugin, this::stopEvent, 20L * 60 * eventInterval);
    }

    public boolean isSellMode(){
        return activeMode.equals(EventState.SELL_EVENT);
    }


    public void stopModeTimer() {
        Bukkit.getScheduler().cancelTask(modeTaskId);
        setActiveMode(EventState.INACTIVE);
    }
}

