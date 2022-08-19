package me.codedred.advancedhelp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.codedred.advancedhelp.AdvancedHelp;
import me.codedred.advancedhelp.events.AdminGUIEvent;
import me.codedred.advancedhelp.events.HelpGUIEvent;

public class GUIClick implements Listener {

	private final AdvancedHelp plugin;
	public GUIClick(AdvancedHelp plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		if (!((Player) event.getWhoClicked()).hasPermission("help.use"))
			return;
		if (plugin.directory.hasAdminInventory()) {
			if (plugin.directory.getAdminInventory() == event.getInventory()) {
				Bukkit.getPluginManager().callEvent(new AdminGUIEvent((Player) event.getWhoClicked(), event, null));
				return;
			}
		}
		if (!plugin.directory.inventoryViewer.hasInventories(((Player) event.getWhoClicked()).getName())) return;
		if (!plugin.directory.inventoryViewer.getInventories(((Player) event.getWhoClicked()).getName()).contains(event.getInventory()))
			return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

		if (event.getView().getType() != InventoryType.PLAYER) {
	        // Get inventory name
	        String invID = plugin.directory.getNameID(player, event.getClickedInventory());
	        
	        Bukkit.getPluginManager().callEvent(new HelpGUIEvent(player, event, invID));
		}
        
	}

    
}

