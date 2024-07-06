package org.dafy.gens.game.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.dafy.gens.game.managers.BlockManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.game.upgrader.UpgradeGUI;
import org.dafy.gens.user.User;
import org.dafy.gens.utility.Language;

public class BlockInteraction implements Listener {
    private final Gens plugin;
    private final BlockManager blockManager;
    private final UpgradeGUI upgradeGUI;
    public BlockInteraction(Gens plugin){
        this.plugin = plugin;
        this.blockManager = plugin.getBlockManager();
        this.upgradeGUI = plugin.getUpgradeGUI();
    }
    @EventHandler
    public void onGenInteract(PlayerInteractEvent event) {
        if(event.useInteractedBlock().equals(Event.Result.DENY)) return;
        Player player = event.getPlayer();
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK || !player.isSneaking()) return;
        Block block = event.getClickedBlock();
        if(block == null) return;
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if(user == null) return;
        // Returns if not generator block
        if (!blockManager.hasBlockPersistentData(block, BlockManager.GENERATOR_KEY)) return;
        Generator matchingGenerator = user.getGenerators()
                .stream()
                .filter(generator -> generator.getGeneratorLocation().equals(block.getLocation()))
                .findFirst()
                .orElse(null);
        //User tried to break someones gen
        if (matchingGenerator == null) {
            event.getPlayer().sendMessage(Language.DOESNT_OWN_GENERATOR.toString());
            return;
        }
        upgradeGUI.openInventory(player,matchingGenerator);
    }
}
