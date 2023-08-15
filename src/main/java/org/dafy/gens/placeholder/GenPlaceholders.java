package org.dafy.gens.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.dafy.gens.Gens;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.user.User;
import org.jetbrains.annotations.NotNull;

public class GenPlaceholders extends PlaceholderExpansion {
    private final Gens plugin;
    private final GensEvent gensEvent;
    public GenPlaceholders(Gens plugin){
        this.plugin = plugin;
        this.gensEvent = plugin.getGensEvent();
    }
    @Override
    public @NotNull String getIdentifier() {
        return "gens";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Dafydd";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("name")) {
            return player == null ? null : player.getName();
        }
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if(params.equalsIgnoreCase("limit")) {
            return String.valueOf(user.getGenLimit());
        }
        if(params.equalsIgnoreCase("placed")) {
            return String.valueOf(user.getGensPlaced());
        }
        if(params.equalsIgnoreCase("event")) {
            return gensEvent.getEventName();
        }
        return null; // Placeholder is unknown by the Expansion
    }
}
