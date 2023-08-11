package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;

@CommandAlias("Gens")
public class Stats extends BaseCommand {
    @Dependency
    private Gens plugin;
    @Subcommand("Stats")
    public void playerStats(Player player) {
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "------&b&lGen Stats&r------" +
                        "\n&b&lGens Placed: &r" + user.getGensPlaced()
                        + "\n&b&lGen Limit: &r" + user.getGenLimit()
                        + "\n--------------------"
        ));
    }
}

