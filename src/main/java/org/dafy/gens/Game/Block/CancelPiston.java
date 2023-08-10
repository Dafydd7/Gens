package org.dafy.gens.Game.Block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.dafy.gens.Gens;

public class CancelPiston implements Listener {
    private final Gens plugin;

    public CancelPiston(Gens plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent e) {
        if (e.isCancelled()) return;
        // Check if the pushed block is not a generator
        if (plugin.getBlockManager().hasBlockPersistentData(e.getBlock(), "Generator")) return;
        e.setCancelled(true);
    }

}
