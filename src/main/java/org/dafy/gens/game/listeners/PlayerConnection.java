package org.dafy.gens.game.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.managers.BlockManager;
import org.dafy.gens.game.generator.GeneratorManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.user.UserData;

import java.util.concurrent.CompletableFuture;

public class PlayerConnection implements Listener {
    private final UserData userData;
    private final GeneratorManager generatorManager;
    private final BlockManager blockManager;
    public PlayerConnection(Gens plugin){
        this.userData = plugin.getUserData();
        this.generatorManager = plugin.getGeneratorManager();
        this.blockManager = plugin.getBlockManager();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if(!player.hasPlayedBefore()){
            Generator generator = generatorManager.genFromTier(1);
            ItemStack genItem = generator.getGeneratorItem();
            blockManager.addItemPersistentData(genItem,"GeneratorItem",1);
            player.getInventory().addItem(genItem);
        }
        CompletableFuture.runAsync(()->userData.loadOrCreateUser(player));
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CompletableFuture.runAsync(()->userData.saveUser(e.getPlayer().getUniqueId(),true));
    }
}
