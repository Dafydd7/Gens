package org.dafy.gens.game.generator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.dafy.gens.Gens;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.user.User;
import org.dafy.gens.user.UserManager;

import java.util.Iterator;


public class GeneratorItemSpawner extends BukkitRunnable {
    private final GensEvent gensEvent;
    private final GeneratorManager generatorManager;
    private final UserManager userManager;
    public GeneratorItemSpawner(Gens plugin) {
        this.gensEvent = plugin.getGensEvent();
        this.generatorManager = plugin.getGeneratorManager();
        this.userManager = plugin.getUserManager();
    }

    @Override
    public void run(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = userManager.getUser(player.getUniqueId());
            if (user == null || user.getGenerators().isEmpty()) continue;
            Iterator<Generator> iterator = user.getGenerators().iterator();
            while (iterator.hasNext()) {
                Generator generator = iterator.next();
                Location location = generator.getGeneratorLocation();
                if (location == null || location.getBlock().getType() == Material.AIR || location.getBlock().getType() != generator.getGeneratorItem().getType()) {
                    iterator.remove();
                    generatorManager.forceRemoveGenerator(generator);
                    user.removeGenerator(generator);
                    continue;
                }
                if (!generator.isPlayerNearby(player)) continue;
                gensEvent.getModeAndDrop(generator);
            }
        }
    }
}
