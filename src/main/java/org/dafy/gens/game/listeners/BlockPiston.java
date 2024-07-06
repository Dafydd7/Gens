package org.dafy.gens.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.dafy.gens.Gens;
import org.dafy.gens.game.managers.BlockManager;

public class BlockPiston implements Listener {
    private final BlockManager blockManager;

    public BlockPiston(Gens plugin) {
        this.blockManager = plugin.getBlockManager();
    }

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent e) {
        if (e.isCancelled()) return;
        // Check if the pushed block is not a generator
        if (!blockManager.hasBlockPersistentData(e.getBlock().getRelative(e.getDirection()), "Generator")) return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        if (e.isCancelled()) return;
        boolean foundMatchingBlock = e.getBlocks()
                .stream()
                .anyMatch(block -> blockManager.hasBlockPersistentData(block, "Generator"));
        if(foundMatchingBlock) e.setCancelled(true);
    }
}
