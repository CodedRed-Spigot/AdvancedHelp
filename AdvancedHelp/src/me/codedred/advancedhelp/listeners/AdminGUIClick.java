package me.codedred.advancedhelp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.codedred.advancedhelp.Main;
import me.codedred.advancedhelp.events.AdminGUIEvent;

public class AdminGUIClick implements Listener {

	private Main plugin;
	public AdminGUIClick(Main plugin) {
		this.plugin = plugin;
	}
	
	 @EventHandler
	    public void onAdminClick(AdminGUIEvent event){
	        Player p = (Player) event.getPlayer();
	        

	        	event.getPlayer().closeInventory();
	        	
	        	String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
	        	if (name.equalsIgnoreCase("Reload")) {
	        		p.closeInventory();
	        		Bukkit.getServer().dispatchCommand(p, "ahelp reload");
	        		p.updateInventory();
	        		return;
	        	}
	        	if (name.equalsIgnoreCase("Debug")) {
	        		p.closeInventory();
	        		Bukkit.getServer().dispatchCommand(p, "ahelp debug");
	        		p.updateInventory();
	        		return;
	        	}
	        	if (name.equalsIgnoreCase("Join Discord")) {
	        		p.closeInventory();
	        		p.sendMessage(plugin.format("&9Join our discord for help and more!"));
	        		p.sendMessage(plugin.format("&3https://discord.gg/gqwtqX3"));
	        		p.updateInventory();
	        		return;
	        	}
	        	if (name.equalsIgnoreCase("Support Plugin")) {
	        		p.closeInventory();
	        		p.sendMessage(plugin.format("&bConsider supporting me!"));
	        		p.sendMessage(plugin.format("&3http://tinyurl.com/y4kw3yyk"));
	        		p.updateInventory();
	        		return;
	        	}
	        	if (name.equalsIgnoreCase("AdvancedHelp")) {
	        	} 	
	        	p.updateInventory();
	        	return;
	    }
	
}
