package org.dafy.gens.utility;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Language {
    PREFIX("prefix", "&7[&bKITS&7] "),
    EVENT_INACTIVE("event-inactive", "&fThe event has now ended!"),
    EVENT_UPGRADE("event-upgrade", "&fAn &bUpgrade Event &fhas begun! All generator drops will increase by &b1 tier&f."),
    EVENT_SELL("event-sell", "&fA &b2x Sell Event &fhas started! All drops are now worth 2x the original price."),
    EVENT_DOUBLE("event-double", "&fA &b2x Drops Event &fhas started! All drops are now doubled."),
    SELL_NO_ITEMS("no-sellable-items", "&fYou do not have any generator drops to sell."),
    DOESNT_OWN_GENERATOR("do-not-own-generator","&fYou cannot interact with this generator as you do not own it!")
    ;

    private final String configPath;
    private final String defaultMessage;
    @Setter private static YamlConfiguration languageConfig;

    Language(String configPath, String defaultMessage) {
        this.configPath = configPath;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String toString() {
        String prefix = languageConfig.getString(PREFIX.configPath, PREFIX.defaultMessage);
        String message = languageConfig.getString(this.configPath, this.defaultMessage);
        return ChatColor.translateAlternateColorCodes('&', prefix + " " + message);
    }
}
