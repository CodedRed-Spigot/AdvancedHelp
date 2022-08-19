package me.codedred.advancedhelp.models.scenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.Inventory;

public class InventoryViewer {

	private Map<String, List<Inventory>> menus = new HashMap<String, List<Inventory>>();
	
	
	public void addInventories(String name, List<Inventory> inv) {
		menus.put(name, inv);
	}
	
	
	public List<Inventory> getInventories(String name) {
		return menus.get(name);
	}
	
	
	public boolean hasInventories(String name) {
		return menus.containsKey(name);
	}
	
	
	public void clear() {
		menus.clear();
	}
	
	
	public void removeMenu(String name) {
		menus.remove(name);
	}
}
