package org.dafy.gens.game.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.dafy.gens.Gens;

public class BlockBurn implements Listener {
    private final BlockManager blockManager;
    public BlockBurn(Gens plugin){
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onBlockBurn(BlockBurnEvent e){
        if(e.isCancelled()) return;
        if (blockManager.hasBlockPersistentData(e.getBlock(), BlockManager.GENERATOR_KEY)) e.setCancelled(true);
    }
}
