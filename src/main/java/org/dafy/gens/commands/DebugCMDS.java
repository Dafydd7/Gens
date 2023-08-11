package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.Generator;


@CommandAlias("dbg || debug")
public class DebugCMDS extends BaseCommand {
    @Dependency
    private Gens plugin;
    @Subcommand("state")
    public void checkState(CommandSender sender) {
        sender.sendMessage("Mode is:" + plugin.getGensEvent().getActiveMode().toString());
    }

    @Subcommand("giveTier") @Syntax("/gens giveTier <Player> <Tier>")
    public void giveDefault(Player player, int  tier){
        Generator generator = plugin.getGenManager().genFromTier(tier);
        ItemStack genItem = generator.getGenItem();
        plugin.getBlockManager().addItemPersistentData(genItem,"GeneratorItem",1);
        player.getInventory().addItem(genItem);
    }
}
