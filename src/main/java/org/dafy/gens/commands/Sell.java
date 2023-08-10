package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.dafy.gens.Gens;
import org.dafy.gens.Game.economy.Eco;
import org.dafy.gens.Game.events.EventState;

public class Sell extends BaseCommand {
    @Dependency
    private Gens plugin;
    @CommandAlias("Sell")
    public void sell(Player player){
        int amount = 100;
        int doubledAmount = amount *2;
        if(plugin.getGensEvent().getActiveMode() == EventState.SELL_EVENT){
            Eco.getEconomy().depositPlayer(player,doubledAmount);
            player.sendMessage(ChatColor.GREEN + "" + doubledAmount);
            return;
        }
        Eco.getEconomy().depositPlayer(player,amount);
        player.sendMessage(ChatColor.GREEN + "" +amount);
    }
}
