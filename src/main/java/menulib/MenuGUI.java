package menulib;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class MenuGUI implements MenuHandler {

    @Getter
    private final Inventory inventory;
    private final Map<Integer, MenuItem> menuItemMap = new HashMap<>();

    public MenuGUI() {
        this.inventory = this.createInventory();
    }

    public void addMenuItem(int slot, MenuItem item) {
        this.menuItemMap.put(slot, item);
    }

    public void decorate(Player player) {
        this.menuItemMap.forEach((slot, button) -> {
            ItemStack icon = button.getIconCreator().apply(player);
            this.inventory.setItem(slot, icon);
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        MenuItem item = this.menuItemMap.get(slot);
        if (item != null) {
            item.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate((Player) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {

    }

    protected abstract Inventory createInventory();
}
