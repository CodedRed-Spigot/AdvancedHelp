package me.codedred.advancedhelp.commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.codedred.advancedhelp.AdvancedHelp;
import me.codedred.advancedhelp.menus.Menu;
import me.codedred.advancedhelp.utils.PlaceholdersUtil;

public class Help implements CommandExecutor, Listener {
	
	private final AdvancedHelp plugin;
	private final Menu menu;
	public Help(AdvancedHelp plugin) {
		this.plugin = plugin;
		menu = new Menu(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	public boolean isNum(String num) {
		try {
			Integer.parseInt(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if ((label.equalsIgnoreCase("help")) || (label.equalsIgnoreCase("ehelp"))) {
			if (!sender.hasPermission("help.use")) {
				sender.sendMessage(plugin.format(plugin.getConfig().getString("prefix") + " " + plugin.getConfig().getString("messages.no-permission")));
				return true;
			}
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
			
			FileConfiguration CONFIG = plugin.getConfig();
			FileConfiguration PAGES = plugin.getPages();
			FileConfiguration GROUP = plugin.getCategories();
			
			
			/**
			 * '/help'
			 */
			if (args.length == 0) {
				if (CONFIG.getBoolean("gui.enabled")) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (CONFIG.getBoolean("refresh-guis")) 
							if (plugin.directory.hasMainInventory(player.getName()))
								plugin.directory.inventoryViewer.removeMenu(player.getName());
						menu.create(player);
						return true;
					}
				}
				if (!PAGES.contains("pages.1")) {
					sender.sendMessage(ChatColor.RED + "No help pages created!");
					return true;
				}
				for (String msg : PAGES.getStringList("pages.1")) {
					if (plugin.hasPAPI() && sender instanceof Player) {
						Player player = (Player) sender;
						msg = PlaceholdersUtil.setPlaceholders(player, msg);
					}
					 if (msg.contains("{\"text\":")) {
						 if (sender instanceof Player) {
						 Player player = (Player) sender;
						 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ("tellraw " + player.getName() + " " +  msg)
								 .replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
						 } else
							 sender.sendMessage("This message contains JSON text that cannot be sent to the console.");
					 }
					 if (!msg.contains("{\"text\":"))
						 sender.sendMessage(plugin.format(msg).replace("%player%", sender.getName()).replace("%time%", format.format(now)));  
				}
				return true;
			}
			/**
			 * '/help <page>'
			 */
			if (args.length == 1) {
				if (isNum(args[0])) {
					int page = Integer.parseInt(args[0]);
					if (!PAGES.contains("pages." + page)) {
						sender.sendMessage(plugin.format(CONFIG.getString("messages.page-not-found")));
						return true;
					}
					for (String msg : PAGES.getStringList("pages." + page)) {
						if (plugin.hasPAPI() && sender instanceof Player) {
							Player player = (Player) sender;
							msg = PlaceholdersUtil.setPlaceholders(player, msg);
						}
						 if (msg.contains("{\"text\":")) {
							 if (sender instanceof Player) {
							 Player player = (Player) sender;
							 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ("tellraw " + player.getName() + " " +  msg)
									 .replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
							 } else
								 sender.sendMessage("This message contains JSON text that cannot be sent to the console.");
						 }
						 if (!msg.contains("{\"text\":"))
							 sender.sendMessage(plugin.format(msg).replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
					}
					return true;
				}
				
				/**
				 * '/help <category> || /help <GUI>'
				 * * GUI priority true then open specific GUI
				 */
				if (CONFIG.getBoolean("cmd-priority.gui") && sender instanceof Player
						&& CONFIG.getBoolean("gui.enabled")) {
					Player player = (Player) sender;
					String gui = args[0].toLowerCase();
					if (!gui.contains("-push-")) {
						if (!plugin.getGUI().getBoolean("gui." + gui + ".default") && plugin.getGUI().contains("gui." + gui + ".default")) {
							if (!player.hasPermission("help." + gui)) {
								sender.sendMessage(plugin.format(CONFIG.getString("prefix") + " " + CONFIG.getString("messages.no-permission")));
								return true;
							}
						}
						if (plugin.getGUI().getConfigurationSection("gui").contains(gui)) {
							if (plugin.directory.inventoryViewer.hasInventories(player.getName())) {
								player.openInventory(plugin.directory.inventoryViewer.getInventories(
		                    			player.getName()).get(plugin.directory.getInventoryID(player, gui)));
								return true;
							}
							menu.create(player);
							player.closeInventory();
							player.openInventory(plugin.directory.inventoryViewer.getInventories(
	                    			player.getName()).get(plugin.directory.getInventoryID(player, gui)));
							return true;
						}
					}
					
				}
				String c = args[0].replace("-push-", "");
				if (!plugin.directory.getCategories().contains(c.toLowerCase())) {
					sender.sendMessage(plugin.format(CONFIG.getString("messages.category-not-found")));
					return true;
				}
				if (!GROUP.getBoolean("categories." + c.toLowerCase() + ".default")) {
					if (!sender.hasPermission("help." + c.toLowerCase())) {
						sender.sendMessage(plugin.format(CONFIG.getString("prefix") + " " + CONFIG.getString("messages.no-permission")));
						return true;
					}
				}
				for (String msg : GROUP.getStringList("categories." + c.toLowerCase() + ".1")) {
					if (plugin.hasPAPI() && sender instanceof Player) {
						Player player = (Player) sender;
						msg = PlaceholdersUtil.setPlaceholders(player, msg);
					}
					 if (msg.contains("{\"text\":")) {
						 if (sender instanceof Player) {
						 Player player = (Player) sender;
						 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ("tellraw " + player.getName() + " " +  msg)
								 .replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
						 } else
							 sender.sendMessage("This message contains JSON text that cannot be sent to the console.");
					 }
					 if (!msg.contains("{\"text\":"))
						 sender.sendMessage(plugin.format(msg).replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
				}
				return true;
			}
			/**
			 * '/help <category> <page>'
			 */
			if (args.length >= 2) {
				if (isNum(args[1])) {
					if (!plugin.directory.getCategories().contains(args[0].toLowerCase())) {
						sender.sendMessage(plugin.format(CONFIG.getString("messages.category-not-found")));
						return true;
					}
					if (!GROUP.getBoolean("categories." + args[0].toLowerCase() + ".default")) {
						if (!sender.hasPermission("help." + args[0].toLowerCase())) {
							sender.sendMessage(plugin.format(CONFIG.getString("prefix") + " " + CONFIG.getString("messages.no-permission")));
							return true;
						}
					}
					int page = Integer.parseInt(args[1]);
					if (!GROUP.contains("categories." + args[0].toLowerCase() + "." + page)) {
						sender.sendMessage(plugin.format(CONFIG.getString("messages.page-not-found")));
						return true;
					}
					for (String msg : GROUP.getStringList("categories." + args[0].toLowerCase() + "." + page)) {
						if (plugin.hasPAPI() && sender instanceof Player) {
							Player player = (Player) sender;
							msg = PlaceholdersUtil.setPlaceholders(player, msg);
						}
						 if (msg.contains("{\"text\":")) {
							 if (sender instanceof Player) {
							 Player player = (Player) sender;
							 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ("tellraw " + player.getName() + " " +  msg)
									 .replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
							 } else
								 sender.sendMessage("This message contains JSON text that cannot be sent to the console.");
						 }
						 if (!msg.contains("{\"text\":"))
							 sender.sendMessage(plugin.format(msg).replace("%player%", sender.getName()).replace("%time%", format.format(now))); 
					}
					return true;
				}
				sender.sendMessage(plugin.format(CONFIG.getString("messages.page-not-found")));
				return true;
			}
			
		}
		return false;
	} 
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPreCommand(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().startsWith("/help"))
			e.setMessage(e.getMessage().replace("/help", "/ehelp"));
		if (e.getMessage().startsWith("/?") && !e.getPlayer().hasPermission("help.override"))
			e.setMessage(e.getMessage().replace("/?", "/ehelp"));
	}
	
}
