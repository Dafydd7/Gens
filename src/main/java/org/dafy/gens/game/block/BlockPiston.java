package org.dafy.gens.game.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.dafy.gens.Gens;

public class BlockPiston implements Listener {
    private final BlockManager blockManager;

    public BlockPiston(Gens plugin) {
        this.blockManager = plugin.getBlockManager();
    }

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent e) {
        if (e.isCancelled()) return;
        // Check if the pushed block is not a generator
        if (!blockManager.hasBlockPersistentData(e.getBlock(), "Generator")) return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        if (e.isCancelled()) return;
        System.out.println("Entity Event");
        boolean foundMatchingBlock = e.getBlocks()
                .stream()
                .anyMatch(block -> blockManager.hasBlockPersistentData(block, "Generator"));
        if(foundMatchingBlock) e.setCancelled(true);
    }
}
