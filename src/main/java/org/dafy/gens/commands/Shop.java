package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;
import org.dafy.gens.Gens;

@CommandAlias("Gens")
public class Shop  extends BaseCommand {
    @Dependency
    private Gens plugin;
    @Subcommand("Shop")
    public void openShop(Player player){
        plugin.getShopManager().openShopGUI(player);
    }
}
