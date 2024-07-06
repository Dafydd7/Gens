package org.dafy.gens.game.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.dafy.gens.Gens;
import org.dafy.gens.game.managers.BlockManager;

public class BlockBurn implements Listener {
    private final BlockManager blockManager;
    public BlockBurn(Gens plugin){
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler (ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e){
        if (blockManager.hasBlockPersistentData(e.getBlock(), BlockManager.GENERATOR_KEY)) {
            e.setCancelled(true);
        }
    }
}
