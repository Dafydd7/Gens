package org.dafy.gens.game.generator;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.dafy.gens.Gens;
import org.dafy.gens.game.generator.builders.DropBuilder;
import org.dafy.gens.game.generator.builders.GenBuilder;
import org.dafy.gens.game.shop.ShopManager;

import java.util.logging.Level;

public class ItemCreator {
    private final Gens plugin;
    private final GenManager genManager;
    private final ShopManager shopManager;
    public ItemCreator(Gens plugin){
        this.plugin = plugin;
        this.genManager = plugin.getGenManager();
        this.shopManager = plugin.getShopManager();
    }
    public void initBuilders() {
        //Get the config, as well as the sections for generators/drops.
        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection generatorSection = config.getConfigurationSection("Generators.");
        ConfigurationSection dropSection = config.getConfigurationSection("Drops.");
        //Clear the maps, in case someone has reloaded.
        shopManager.clearSellableItems();
        genManager.clearGenerators();
        //Return early if null.
        if (generatorSection == null || dropSection == null) {
            plugin.getLogger().log(Level.WARNING, "Unable to initialize Generators/Drops - Check your config.yml");
            return;
        }
        //Initialise all the possible generators, and put them inside a map.
        generatorSection.getKeys(false).forEach(key -> {
            Generator genBuilder = new GenBuilder(generatorSection.getConfigurationSection(key)).build();
            genManager.addGenerator(genBuilder.getTier(), genBuilder);
        });
        //Initialise the generator drops, as well as set them in the generator map.
        dropSection.getKeys(false).forEach(key -> {
             GenDrop genDrop = new DropBuilder(dropSection.getConfigurationSection(key)).build();
             ItemStack genItem = genDrop.getDropItem();
             genManager.setDropItem(genDrop.getTier(),genItem);
            genDrop.getDropItem().getItemMeta().getLore().add(ChatColor.YELLOW + "Click to buy!");
            shopManager.addSellableItem(genDrop.getDropItem(), genDrop.getPrice());
        });
    }
}
