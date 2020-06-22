package me.codedred.advancedhelp.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.codedred.advancedhelp.Main;
import me.codedred.advancedhelp.utils.ItemUtil;

public class AdminMenu {
	
	private Main plugin;
	public AdminMenu(Main plugin) {
		this.plugin = plugin;
	}
	
	public void create(Player player) {
		if (plugin.directory.hasAdminInventory()) {
			player.openInventory(plugin.directory.getAdminInventory());
			return;
		}
			Inventory inv = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "AdvancedHelp Admin Panel");

	        // Reload
			ItemStack reload = ItemUtil.createItem("APPLE", player);
			reload = ItemUtil.addDisplayName(reload, "&9Reload", player);
			List<String> lore = new ArrayList<String>();
	        lore.add("&7Click here to reload files!");
			reload = ItemUtil.addLore(reload, lore, player);
			reload = ItemUtil.addGlow(reload);
			reload = ItemUtil.addFlag(reload);
			inv.setItem(3, reload);
	        
	        // Debug
			ItemStack debug = ItemUtil.createItem("GOLDEN_APPLE", player);
			debug = ItemUtil.addDisplayName(debug, "&6Debug", player);
			lore.clear();
	        lore.add("&7Click here to debug files!");
			debug = ItemUtil.addLore(debug, lore, player);
			debug = ItemUtil.addGlow(debug);
			debug = ItemUtil.addFlag(debug);
			inv.setItem(4, debug);
			
	        
	        // Discord
			ItemStack discord = ItemUtil.createItem("FEATHER", player);
			discord = ItemUtil.addDisplayName(discord, "&cJoin Discord", player);
			lore.clear();
	        lore.add("&7Click here to join help discord!");
			discord = ItemUtil.addLore(discord, lore, player);
			discord = ItemUtil.addGlow(discord);
			discord = ItemUtil.addFlag(discord);
			inv.setItem(5, discord);
			
	        
	        // Support
			ItemStack support = ItemUtil.createItem("NETHER_STAR", player);
			support = ItemUtil.addDisplayName(support, "&4Support Plugin", player);
			lore.clear();
			lore.add("&7Enjoy AdvancedHelp? Support us!");
	        lore.add("&bSupporting AdvancedHelp will keep it up");
	        lore.add("&band running for many years!");
			support = ItemUtil.addLore(support, lore, player);
			support = ItemUtil.addGlow(support);
			support = ItemUtil.addFlag(support);
			inv.setItem(8, support);
	        
	        
	        // Cosmetic
	        int slots[] = {0,1,2,6,7};
	        for (int s : slots) {
	        	ItemStack cosmetic = ItemUtil.createItem("LIGHT_BLUE_STAINED_GLASS_PANE", player);
	        	cosmetic = ItemUtil.addDisplayName(cosmetic, "&bAdvancedHelp", player);
	        	inv.setItem(s, cosmetic);
	        }

	        List<Inventory> invs = new ArrayList<Inventory>();
	        invs.add(inv);
	        plugin.directory.inventoryViewer.addInventories("admin-inv", invs);
	        
	        player.openInventory(inv);
		}
	
}
