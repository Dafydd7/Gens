package menulib;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private final Map<Inventory, MenuHandler> activeInventories = new HashMap<>();

    public void openGUI(MenuGUI gui, Player player) {
        this.cachePlayerMenu(gui.getInventory(), gui);
        player.openInventory(gui.getInventory());
    }

    public void cachePlayerMenu(Inventory inventory, MenuHandler handler) {
        this.activeInventories.put(inventory, handler);
    }

    public void removePlayerMenu(Inventory inventory) {
        this.activeInventories.remove(inventory);
    }

    public void handleMenuClick(InventoryClickEvent event) {
        MenuHandler handler = this.activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onClick(event);
        }
    }

    public void handleMenuOpen(InventoryOpenEvent event) {
        MenuHandler handler = this.activeInventories.get(event.getInventory());
        if (handler != null) {
            handler.onOpen(event);
        }
    }

    public void handleMenuClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        MenuHandler handler = this.activeInventories.get(inventory);
        if (handler != null) {
            handler.onClose(event);
            this.removePlayerMenu(inventory);
        }
    }
}
