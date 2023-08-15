package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.dafy.gens.Gens;
import org.dafy.gens.user.User;

@CommandAlias("Gens")
public class Admin extends BaseCommand {
    @Dependency
    private Gens plugin;
    @CommandPermission("gens.commands.admin.give")
    @Subcommand("give limit") @Syntax("<Player> <Amount>")
    public void onTierGive(Player sender, OnlinePlayer player, int amount){
        if(amount <= 0 ){
            sender.sendMessage(ChatColor.RED + "ERROR: Number must be greater than 0.");
            return;
        }
        User user = plugin.getUserManager().getUser(player.getPlayer().getUniqueId());
        int userLimit = user.getGenLimit();
        int newLimit = userLimit + amount;
        user.setGenLimit(newLimit);
        sender.sendMessage(ChatColor.GREEN + "Added limit to " + player.getPlayer().getName() + "." +
        "\nTheir new limit is now: " + userLimit + ".");
    }
    @CommandPermission("gens.commands.admin.take")
    @Subcommand("take limit") @Syntax("<Player> <Amount>")
    public void onTierRemove(Player sender, OnlinePlayer player, int amount){
        User user = plugin.getUserManager().getUser(player.getPlayer().getUniqueId());
        int userLimit = user.getGenLimit();
        if(amount <= userLimit) {
            user.setGenLimit(userLimit - amount);
            return;
        }
        sender.sendMessage(ChatColor.RED + "ERROR: Amount cannot be higher than users limit. (" + userLimit + ")");
    }
}
