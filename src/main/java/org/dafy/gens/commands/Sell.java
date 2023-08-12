package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.dafy.gens.Gens;
import org.dafy.gens.game.shop.ShopManager;


public class Sell extends BaseCommand {
    @Dependency
    private Gens plugin;
    @CommandAlias("Sell")
    public void sell(Player player){
        ShopManager shopManager = plugin.getShopManager();
        shopManager.sellAllItems(player,player.getInventory());
    }
}
