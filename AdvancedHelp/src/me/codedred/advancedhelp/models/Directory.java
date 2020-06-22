package me.codedred.advancedhelp.models;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.codedred.advancedhelp.models.scenes.InventoryViewer;
import me.codedred.advancedhelp.models.scenes.ListViewer;

public class Directory {

	public InventoryViewer inventoryViewer;
	public ListViewer listViewer;
	
	public Directory() {
		this.inventoryViewer = new InventoryViewer();
		this.listViewer = new ListViewer();
	}
	
	public void clearScenes() {
		this.inventoryViewer.clear();
		this.listViewer.clear();
	}
	
	public List<String> getCategories() {
		return listViewer.getList("categories");
	}
	
	public List<String> getGUIs() {
		return listViewer.getList("guis");
	}
	
	public Inventory getAdminInventory() {
		return this.inventoryViewer.getInventories("admin-inv").get(0);
	}
	
	public Inventory getMainInventory(String player) {
		return this.inventoryViewer.getInventories(player).get(0);
	}
	
	public boolean hasAdminInventory() {
		if (this.inventoryViewer.hasInventories("admin-inv"))
			return true;
		return false;
	}
	
	public boolean hasMainInventory(String player) {
		if (this.inventoryViewer.hasInventories(player))
			return true;
		return false;
	}
	
	
	/**
	 * Gets the name ID for a GUI given the inventory
	 * 
	 * @param player opening the GUI
	 * @param inv that is currently opened
	 * @return the ID location for the name
	 */
	public String getNameID(Player player, Inventory inv) {
		 int ID = 0;
	        for (int i = 0; i < listViewer.getList(player.getName()).size() ; i++) {
	        	if (inventoryViewer.getInventories(player.getName()).get(i).equals(inv))
	        		ID = i;
	        }
	     return listViewer.getList(player.getName()).get(ID);
	}
	
	
	/**
	 * Gets the inventory ID for a GUI given the name 
	 * 
	 * @param player opening the GUI
	 * @param inv that is currently opened
	 * @return the ID location for the name
	 */
	public int getInventoryID(Player player, String inv) {
		 int ID = 0;
		 for (int in = 0; in < listViewer.getList(player.getName()).size() ; in++) {
         	if (listViewer.getList(player.getName()).get(in).equals(inv))
         		ID = in;
         }
	     return ID;
	}
}
