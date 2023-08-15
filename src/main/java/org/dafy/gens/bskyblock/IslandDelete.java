package org.dafy.gens.bskyblock;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dafy.gens.Gens;
import org.dafy.gens.config.ConfigManager;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;
import org.dafy.gens.user.User;
import org.dafy.gens.user.UserManager;
import world.bentobox.bentobox.api.events.island.IslandResettedEvent;
import world.bentobox.bentobox.api.events.team.TeamKickEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.api.events.team.TeamSetownerEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.Set;
import java.util.UUID;

public class IslandDelete implements Listener {

    private final GenManager genManager;
    private final UserManager userManager;
    private final Gens plugin;

    public IslandDelete(Gens plugin) {
        this.plugin = plugin;
        userManager = plugin.getUserManager();
        genManager = plugin.getGenManager();
    }

    @EventHandler
    private void onIslandReset(IslandResettedEvent event) {
        String islandID = event.getOldIsland().getUniqueId();
        deleteIslandGenerators(islandID,event.getIsland().getMemberSet());
    }

    @EventHandler
    public void onTeamKick(TeamKickEvent event) {
        Island island = event.getIsland();
        UUID owner = island.getOwner();
        //Return early if null; this event is triggered by island reset.
        if(owner == null) return;
        UUID kickedPlayer = event.getPlayerUUID();
        deletePlayerGenerators(island.getUniqueId(), kickedPlayer);
    }

    @EventHandler
    public void onTeamKick(TeamLeaveEvent event) {
        Island island = event.getIsland();
        UUID owner = island.getOwner();
        //Return early if null; this event is triggered by island reset.
        if(owner == null) return;
        UUID leftPlayer = event.getPlayerUUID();
        deletePlayerGenerators(island.getUniqueId(), leftPlayer);
    }

    @EventHandler
    public void onTeamTransfer(TeamSetownerEvent event) {
        Island island = event.getIsland();
        UUID newOwner = event.getOwner();
        UUID oldOwner = event.getOldOwner();
        //Return early if null; this event is triggered by island reset.
        if(newOwner == null) return;
        UUID leftPlayer = event.getPlayerUUID();
        deletePlayerGenerators(island.getUniqueId(), leftPlayer);
    }

    private void deleteIslandGenerators(String islandID, Set<UUID>members) {
        if(members.isEmpty()) return;
        for (UUID member:members) {
            deletePlayerGenerators(islandID,member);
        }
    }

    private void deletePlayerGenerators(String islandUUID, UUID player) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        //If online, remove all generators on that island from cache.
        if (offlinePlayer.isOnline()) {
            User cachedUser = userManager.getUser(player);
            for (Generator generator : cachedUser.getGenerators()) {
                if (!generator.getIslandUUID().equals(islandUUID)) continue;
                Bukkit.getScheduler().runTask(plugin, () -> genManager.deleteIslandGenerator(generator, cachedUser.getUuid()));
            }
        }
    }
}