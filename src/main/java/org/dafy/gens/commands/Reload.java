package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;
import org.dafy.gens.Gens;

@CommandAlias("Gens")
public class Reload extends BaseCommand{
    @Dependency
    private Gens plugin;
    @Subcommand("Reload") @CommandPermission("gens.admin.commands.reload")
    public void onReload(Player player){
        plugin.reloadConfig();
        plugin.getItemCreator().initBuilders();
        player.sendMessage("Gens: Config reloaded.");
    }
}
