package org.dafy.gens.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dafy.gens.Gens;
import org.dafy.gens.user.UserData;

import java.util.concurrent.CompletableFuture;

public class UserConnection implements Listener {

    private final UserData userData;
    public UserConnection(Gens plugin){
        this.userData = plugin.getUserData();
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        CompletableFuture.runAsync(()->userData.loadOrCreateUser(e.getPlayer().getUniqueId()));

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        CompletableFuture.runAsync(()->userData.saveUser(e.getPlayer().getUniqueId()));
    }
}
