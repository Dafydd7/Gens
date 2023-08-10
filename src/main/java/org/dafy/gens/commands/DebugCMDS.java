package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Game.Generator.Generator;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;


@CommandAlias("dbg || debug")
public class DebugCMDS extends BaseCommand {
    @Dependency
    private Gens plugin;
    @Subcommand("state")
    public void checkState(CommandSender sender) {
        sender.sendMessage("Mode is:" + plugin.getGensEvent().getActiveMode().toString());
    }

    @Subcommand("give")
    public void giveGenerator(Player player){
        ItemStack gen = new ItemStack(Material.COAL_BLOCK);
        plugin.getBlockManager().addItemPersistentData(gen,"GeneratorItem",1);
        player.getInventory().addItem(gen);
    }

    @Subcommand("giveTier")
    public void giveDefault(Player player){
        Generator generator = plugin.getGenManager().tierToGeneratorMap.get(1);
        ItemStack genItem = generator.getGenItem();
        plugin.getBlockManager().addItemPersistentData(genItem,"GeneratorItem",1);
        player.getInventory().addItem(genItem);
    }

    @Subcommand("Stats")
    public void playerStats(Player player){
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(
                "----Gen Stats----"+
                "\nGens Placed:" + user.getGensPlaced()
                + "\nGen Limit:" + user.getGenLimit()
                + "\n----------------"
        );
    }
}
