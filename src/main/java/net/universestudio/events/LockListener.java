package net.universestudio.events;

import net.universestudio.Tweaks;
import net.universestudio.utils.TextHelper;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.Nullable;

public class LockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isItemLocked(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            event.getPlayer().sendActionBar(TextHelper.text("<red>This item is locked</red>"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTransferItem(InventoryClickEvent event) {
        Inventory clicked = event.getClickedInventory();
        Inventory clickedInventory = event.getClickedInventory();

        ItemStack clickedItem = event.getCurrentItem();
        ItemStack heldItem = event.getCursor();

        boolean clickedIsLocked = isItemLocked(clickedItem);
        boolean heldIsLocked = isItemLocked(heldItem);

        boolean externalInventory = event.getView().getTopInventory().getType() != InventoryType.PLAYER && event.getView().getTopInventory().getType() != InventoryType.CRAFTING;
        HumanEntity player = event.getWhoClicked();

        // Transfer to another inventory via shift + click
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (clickedIsLocked) {
                if (externalInventory && clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER) {
                    event.setCancelled(true);
                    player.sendActionBar(TextHelper.text("<red>This item is locked</red>"));
                    return;
                }
            }
        }

        // Transfer to another inventory via hotbar shortcut + cursor in external inventory
        if (event.getAction() == InventoryAction.HOTBAR_SWAP) {
            if (event.getHotbarButton() >= 0 && event.getHotbarButton() <= 8) {
                if (isItemLocked(player.getInventory().getItem(event.getHotbarButton()))) {
                    event.setCancelled(true);
                    player.sendActionBar(TextHelper.text("<red>This item is locked</red>"));
                    return;
                }
            }
        }

        // Dragging item outside the player's inventory
        if (clicked != null && clicked.getType() != InventoryType.PLAYER) {
            if (clickedIsLocked || heldIsLocked) { // Either the dragged item is locked or the clicked item at the specific clicked slot is locked
                event.setCancelled(true);
                player.sendActionBar(TextHelper.text("<red>This item is locked</red>"));
            }
        }
    }

    private boolean isItemLocked(@Nullable ItemStack item) {
        if (item != null && item.getType() != Material.AIR && item.hasItemMeta()) {
            PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
            if (pdc.has(Tweaks.lockedKey)) {
                Byte value = pdc.get(Tweaks.lockedKey, PersistentDataType.BYTE);
                return value != null && value == 1;
            }
        }

        return false;
    }
}
