package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.Generator;



@CommandAlias("dbg || debug") @CommandPermission("gens.commands.admin.debug")
public class DebugCMDS extends BaseCommand {
    @Dependency
    private Gens plugin;
    @Subcommand("state")
    public void checkState(CommandSender sender) {
        sender.sendMessage("Current Event: " + plugin.getGensEvent().getActiveMode().toString());
    }

    @Subcommand("giveTier")
    public void giveDefault(Player player){
        Generator generator = plugin.getGenManager().genFromTier(1);
        ItemStack genItem = generator.getGenItem();
        plugin.getBlockManager().addItemPersistentData(genItem,"GeneratorItem",1);
        player.getInventory().addItem(genItem);
    }
    @Subcommand("sellwand")
    public void giveSellwand(Player player){
        ItemStack sellWand = new ItemStack(Material.STICK);
        sellWand.getItemMeta().setDisplayName(ChatColor.GREEN + "Sell-Wand");
        plugin.getBlockManager().addItemPersistentData(sellWand,"SellWand",1); //TODO implement usage system.
        player.getInventory().addItem(sellWand);
    }
}
