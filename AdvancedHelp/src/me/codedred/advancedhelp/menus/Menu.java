package me.codedred.advancedhelp.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.codedred.advancedhelp.AdvancedHelp;
import me.codedred.advancedhelp.utils.ItemUtil;
import me.codedred.advancedhelp.utils.PlaceholdersUtil;

public class Menu {

	private final AdvancedHelp plugin;
	public Menu(AdvancedHelp plugin) {
		this.plugin = plugin;
	}
			
			public void create(Player player) {
				if (plugin.directory.hasMainInventory(player.getName())) {
					player.openInventory(plugin.directory.getMainInventory(player.getName()));
					return;
				}
				FileConfiguration GUI = plugin.getGUI();
				FileConfiguration CONFIG = plugin.getConfig();
				List<Inventory> inventory = new ArrayList<Inventory>();
				List<String> names = new ArrayList<String>();
				
				// Go through all GUIs (including main-page)
				for (String gui : GUI.getConfigurationSection("gui").getKeys(false)) {
					Inventory inv = Bukkit.createInventory(null, clamp(GUI.getInt("gui." + gui + ".slots")), plugin.format(GUI.getString("gui." + gui + ".title").replace("%player%", player.getName())));
					
					if (CONFIG.getBoolean("gui.close-page.enabled") && gui.equalsIgnoreCase("main-page")) {
						if (GUI.contains("gui." + gui + ".close-button"))
							inv.setItem(GUI.getInt("gui.main-page.close-button")-1, closeButton(player));
					}
					if (CONFIG.getBoolean("gui.home-page.enabled") && !gui.equalsIgnoreCase("main-page")) {
						if (GUI.contains("gui." + gui + ".home-button"))
							inv.setItem(GUI.getInt("gui." + gui + ".home-button")-1, homeButton(player));
					}
					
					// go through all items in the certain GUI
					for (String i : GUI.getConfigurationSection("gui." + gui + ".items").getKeys(false)) {
						ItemStack item;
						
						String itemMaterial = "BARRIER";
						if (GUI.contains("gui." + gui + ".items." + i + ".item-material"))
							itemMaterial = GUI.getString("gui." + gui + ".items." + i + ".item-material");
						String itemName = "&c*Item name not found*";
						if (GUI.contains("gui." + gui + ".items." + i + ".item-name"))
							itemName = GUI.getString("gui." + gui + ".items." + i + ".item-name");
						List<String> lore = new ArrayList<String>();
						if (GUI.contains("gui." + gui + ".items." + i + ".lore"))
							lore = GUI.getStringList("gui." + gui + ".items." + i + ".lore");
						
						item = ItemUtil.createItem(itemMaterial, player);
						item = ItemUtil.addDisplayName(item, itemName, player);
						item = ItemUtil.addLore(item, lore, player);

						if (GUI.getBoolean("gui." + gui + ".items." + i + ".enchanted")
								&& !itemMaterial.contains("HDB[")) {
							item = ItemUtil.addGlow(item);
						}
						if (CONFIG.getBoolean("gui.hide-attributes")
								&& !itemMaterial.contains("HDB[")) {
							item = ItemUtil.addFlag(item);
						}
						if (plugin.hasPAPI()) 
							item = PlaceholdersUtil.translatePlaceholders(item, player);
						
						if (GUI.contains("gui." + gui + ".items." + i + ".item-amount"))
							item.setAmount(GUI.getInt("gui." + gui + ".items." + i + ".item-amount"));
						
						try {
							//ItemUtil.placeItem(item, plugin.gui.getConfig().getInt("gui." + gui + ".items." + i + ".item-slot"), inv);
							inv.setItem(GUI.getInt("gui." + gui + ".items." + i + ".item-slot")-1, item);
						} catch (Exception e) {
							ItemUtil.placeItem(item, GUI.getString("gui." + gui + ".items." + i + ".item-slot"), inv);
						}
						
					}
					inventory.add(inv);
					names.add(gui.toLowerCase());
				}
				// done open inventory
				plugin.directory.inventoryViewer.addInventories(player.getName(), inventory);
				plugin.directory.listViewer.addList(player.getName(), names);
				player.openInventory(inventory.get(0));
				return;
			}
			
			
			/**
			 * Clamps inventory size to proper lengths
			 * 
			 * @param num from config file
			 * @return fixed GUI size
			 */
			public int clamp(int num) {
				// 9 18 27 36 45 54
				if (num <= 9 ) num = 9; else if (num > 9 && num <= 18) num = 18; else if (num > 18 && num <= 27) num = 27;
				else if (num > 27 && num <= 36) num = 36; else if (num > 36 && num <= 45) num = 45; else num = 54;
				return num;
			}
			

			/**
			 * Creates a closeButton
			 * 
			 * @param player that is opening GUI
			 * @return ItemStack for the close button
			 */
			public ItemStack closeButton(Player player) {
				FileConfiguration CONFIG = plugin.getConfig();
				
				String ID = CONFIG.getString("gui.close-page.item-material");
				ItemStack item;
				
				item = ItemUtil.createItem(ID, player);
				item = ItemUtil.addDisplayName(item, CONFIG.getString("gui.close-page.item-name"), player);
				item = ItemUtil.addLore(item, CONFIG.getStringList("gui.close-page.lore"), player);
				
				if (CONFIG.getBoolean("gui.close-page.enchanted") && !ID.contains("HDB[")) {
					item = ItemUtil.addGlow(item);
				}
				if (CONFIG.getBoolean("gui.hide-attributes") && !ID.contains("HDB[")) {
					item = ItemUtil.addFlag(item);
				}
				
				if (plugin.hasPAPI()) 
					item = PlaceholdersUtil.translatePlaceholders(item, player);
				
				return item;
			}
			
			/**
			 * Creates home button for specific GUI
			 * 
			 * @param player that is opening the GUI
			 * @return ItemStack of home button
			 */
			public ItemStack homeButton(Player player) {
				FileConfiguration CONFIG = plugin.getConfig();
				String ID = CONFIG.getString("gui.home-page.item-material");
				ItemStack item;
				
				item = ItemUtil.createItem(ID, player);
				item = ItemUtil.addDisplayName(item, CONFIG.getString("gui.home-page.item-name"), player);
				item = ItemUtil.addLore(item, CONFIG.getStringList("gui.home-page.lore"), player);
				
				
				if (CONFIG.getBoolean("gui.home-page.enchanted") && !ID.contains("HDB[")) {
					item = ItemUtil.addGlow(item);
				}
				if (CONFIG.getBoolean("gui.hide-attributes") && !ID.contains("HDB[")) {
					item = ItemUtil.addFlag(item);
				}
				
				if (plugin.hasPAPI()) 
					item = PlaceholdersUtil.translatePlaceholders(item, player);

				return item;
			}
}
