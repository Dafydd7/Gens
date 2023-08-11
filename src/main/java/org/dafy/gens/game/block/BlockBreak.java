package org.dafy.gens.game.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.dafy.gens.Gens;

public class BlockBreak implements Listener {
    private final BlockManager blockManager;
    public BlockBreak(Gens plugin){
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onGenBreak(BlockBreakEvent e) {
        if (e.isCancelled()) return;
        if (blockManager.hasBlockPersistentData(e.getBlock(), BlockManager.GENERATOR_KEY))
            e.setCancelled(true);
    }
}
