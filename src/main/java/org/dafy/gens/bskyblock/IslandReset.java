package org.dafy.gens.bskyblock;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import world.bentobox.bentobox.api.events.island.IslandResettedEvent;


public class IslandReset implements Listener {

    @EventHandler
    private void IslandExit(IslandResettedEvent e) {
       // e.getIsland().getMembers().forEach();
    }
}
