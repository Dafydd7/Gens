package org.dafy.gens.utility;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.dafy.gens.Gens;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.user.User;

import javax.annotation.Nonnull;

public class PAPIExpansion extends PlaceholderExpansion {
    private final Gens plugin;
    private final GensEvent gensEvent;
    public PAPIExpansion(Gens plugin){
        this.plugin = plugin;
        this.gensEvent = plugin.getGensEvent();
    }

    @Nonnull
    @Override
    public String getAuthor() {
        return "Dafydd";
    }

    @Nonnull
    @Override
    public String getIdentifier() {
        return "Gens";
    }

    @Nonnull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    public String onRequest(OfflinePlayer player, @Nonnull String params) {
        if(player == null) return null;
        User user = plugin.getUserManager().getUser(player.getUniqueId());
        return switch (params){
            case "limit" -> String.valueOf(user.getGenLimit());
            case "placed" -> String.valueOf(user.getGensPlaced());
            case "event" -> gensEvent.getActiveMode().toString();
            default -> "null";
        };
    }
}
