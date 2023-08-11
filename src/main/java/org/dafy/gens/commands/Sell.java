package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.economy.GenEconomy;
import org.dafy.gens.game.events.EventState;
import org.dafy.gens.game.shop.ShopManager;

public class Sell extends BaseCommand {
    @Dependency
    private Gens plugin;
    @CommandAlias("Sell")
    public void sell(Player player){
        ShopManager shopManager = plugin.getShopManager();

        int sellAmount = 0;
        ItemStack[] inventoryContents = player.getInventory().getContents();

        for (ItemStack item: inventoryContents) {
            if(!shopManager.containsSellItem(item)) {
                continue;
            }
            sellAmount += shopManager.getSellPrice(item) * item.getAmount();
            player.getInventory().remove(item);
        }
        if(sellAmount == 0){
            player.sendMessage("No sellable item(s) were found.");
            return;
        }
        int doubledAmount = sellAmount * 2;
        if (plugin.getGensEvent().getActiveMode() == EventState.SELL_EVENT) {
            GenEconomy.getEconomy().depositPlayer(player, doubledAmount);
            player.sendMessage(ChatColor.GREEN + "+$" + doubledAmount);
        } else {
            GenEconomy.getEconomy().depositPlayer(player, sellAmount);
            player.sendMessage(ChatColor.GREEN + "+$" + sellAmount);
        }
    }
}
