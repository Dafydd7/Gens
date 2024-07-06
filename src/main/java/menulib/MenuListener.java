package menulib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuListener {
    private final MenuManager menuManager;

    public MenuListener(MenuManager guiManager) {
        this.menuManager = guiManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        this.menuManager.handleMenuClick(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        this.menuManager.handleMenuOpen(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        this.menuManager.handleMenuClose(event);
    }
}
