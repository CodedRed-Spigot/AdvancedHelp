package me.codedred.advancedhelp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdminGUIEvent extends Event implements Cancellable {
	
    private final Player player;
    private boolean isCancelled;
    private final String inventoryName;
    private final InventoryClickEvent inv;
	
	public AdminGUIEvent(Player player, InventoryClickEvent inv, String inventoryName) {
		this.player = player;
		this.inv = inv;
		this.inventoryName = inventoryName;
	}

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Inventory getInventory() {
    	return inv.getInventory();
    }
    
    public int getSlot() {
    	return inv.getSlot();
    }
    
    public ItemStack getCurrentItem() {
    	return inv.getCurrentItem();
    }
    
    public String getInventoryTitle() {
    	return inv.getView().getTitle();
    }
    
    public String getInventoryName() {
    	return inventoryName;
    }

    

}