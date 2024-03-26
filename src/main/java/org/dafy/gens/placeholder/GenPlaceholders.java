package org.dafy.gens.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.dafy.gens.Gens;
import org.dafy.gens.game.events.GensEvent;
import org.dafy.gens.user.User;

import javax.annotation.Nonnull;

public class GenPlaceholders extends PlaceholderExpansion {
    private final Gens plugin;
    private final GensEvent gensEvent;
    public GenPlaceholders(Gens plugin){
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
        return "WildKits";
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
