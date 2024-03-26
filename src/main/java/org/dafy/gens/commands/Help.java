package org.dafy.gens.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;

@CommandAlias("Gens")
public class Help extends BaseCommand {
    @Default @Description("Displays a list of all commands available for the generator plugin.")
    public void onCommandHelp(CommandSender sender, CommandHelp help){
        help.showHelp();
    }

}
