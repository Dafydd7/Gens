package org.dafy.gens.game.block;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;

public class BlockInteraction implements Listener {
    private final Gens plugin;
    private final BlockManager blockManager;
    public BlockInteraction(Gens plugin){
        this.plugin = plugin;
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onGenInteract(PlayerInteractEvent e){
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if(block == null) return;
        // Returns if not generator block
        if (!blockManager.hasBlockPersistentData(block, BlockManager.GENERATOR_KEY)) return;
        User user = plugin.getUserManager().getUser(e.getPlayer().getUniqueId());
        if(user == null) return;
        Generator matchingGenerator = user.getGenerators()
                .stream()
                .filter(generator -> generator.getGenLocation().equals(block.getLocation()))
                .findFirst()
                .orElse(null);
        //User tried to break someones gen
        if (matchingGenerator == null) {
            e.getPlayer().sendMessage("You do not own this generator.");
            return;
        }
        plugin.getUpgradeManager().open(e.getPlayer(),matchingGenerator);
    }
}
