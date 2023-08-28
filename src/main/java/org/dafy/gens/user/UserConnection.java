package org.dafy.gens.user;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.block.BlockManager;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;

import java.util.concurrent.CompletableFuture;

public class UserConnection implements Listener {

    private final UserData userData;
    private final GenManager genManager;
    private final BlockManager blockManager;
    public UserConnection(Gens plugin){
        this.userData = plugin.getUserData();
        this.genManager = plugin.getGenManager();
        blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(!player.hasPlayedBefore()){
            Generator generator = genManager.genFromTier(1);
            ItemStack genItem = generator.getGenItem();
            blockManager.addItemPersistentData(genItem,"GeneratorItem",1);
            player.getInventory().addItem(genItem);
        }
        CompletableFuture.runAsync(()->userData.loadOrCreateUser(player));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CompletableFuture.runAsync(()->userData.saveUser(e.getPlayer().getUniqueId()));
    }
}
