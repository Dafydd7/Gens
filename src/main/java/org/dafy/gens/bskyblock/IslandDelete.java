package org.dafy.gens.bskyblock;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.dafy.gens.Gens;
import org.dafy.gens.config.ConfigManager;
import org.dafy.gens.game.generator.GenManager;
import org.dafy.gens.game.generator.Generator;
import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.bentobox.api.events.island.IslandResettedEvent;
import world.bentobox.bentobox.api.events.team.TeamKickEvent;
import world.bentobox.bentobox.database.objects.Island;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IslandDelete implements Listener {

    private final GenManager genManager;
    private final ConfigManager configManager;

    public IslandDelete(Gens plugin) {
        genManager = plugin.getGenManager();
        configManager = plugin.getConfigManager();
    }

    @EventHandler
    public void onIslandDelete(IslandDeleteEvent event){

        Island island = event.getIsland();

        deleteIslandGenerators(island);
    }

    // Now just add the rest of the events and call this method

    @EventHandler
    public void onTeamKick(TeamKickEvent event) {

        // Do you understand this event
        Island island = event.getIsland();
        UUID kickedPlayer = event.getPlayerUUID();

        deletePlayerGenerators(island, kickedPlayer);
    }


    @EventHandler
    private void IslandExit(IslandResettedEvent e) {
        Island island = e.getOldIsland();
        deleteIslandGenerators(island);
    }

    private void deleteIslandGenerators(Island island) {
        for(UUID member : island.getMembers().keySet()) {
            deletePlayerGenerators(island, member);
        }
    }

    private void deletePlayerGenerators(Island island, UUID player) {
        Map<UUID, Integer> members = island.getMembers();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        if(!offlinePlayer.hasPlayedBefore()) return;
        configManager.loadUserConfig(player, user -> {
            List<Generator> generators = user.getGenerators();
            for(Generator generator : generators) {
                if(generator.getIsland() != island) continue; // If the generator's island isn't the island that got deleted, then skip

                genManager.removeGenerator(generator, user.getUuid());
            }
        }, false);
    }
}
// This should be it