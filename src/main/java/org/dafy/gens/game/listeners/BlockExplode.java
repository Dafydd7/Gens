package org.dafy.gens.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.dafy.gens.Gens;
import org.dafy.gens.game.managers.BlockManager;

public class BlockExplode implements Listener {
    private final BlockManager blockManager;
    public BlockExplode(Gens plugin){
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.isCancelled()) return;
        e.blockList().removeIf(block -> blockManager.hasBlockPersistentData(block, BlockManager.GENERATOR_KEY));
    }
}
