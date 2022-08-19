package me.codedred.advancedhelp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.codedred.advancedhelp.AdvancedHelp;
import me.codedred.advancedhelp.data.Debugger;
import me.codedred.advancedhelp.menus.AdminMenu;

public class AdminHelp implements CommandExecutor {
	
	private AdvancedHelp plugin;
	public AdminHelp(AdvancedHelp plugin) {
		this.plugin = plugin;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdL, String[] args) {
			if (!sender.hasPermission("help.reload")) {
				sender.sendMessage(plugin.format(plugin.getConfig().getString("prefix") + " " 
										+ plugin.getConfig().getString("messages.no-permission")));
				return true;
			}

		String message = ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "========[" + ChatColor.RESET + "" + ChatColor.GOLD + "" +
				ChatColor.BOLD + "AdvancedHelp" + ChatColor.RESET + ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "]========";
		if (args.length == 0) {
				if (sender instanceof Player) {
					if (plugin.isNewerVersion()) {
						Player player = (Player) sender;
						AdminMenu menu = new AdminMenu(plugin);
						menu.create(player);
						return true;
					}
				}
				sender.sendMessage(message);
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/" + ChatColor.GRAY + "ahelp reload");
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/" + ChatColor.GRAY + "ahelp info");
				return true;
			}
			
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					plugin.dataManager.cfg.reloadConfig();
					plugin.dataManager.gui.reloadConfig();
					plugin.dataManager.pages.reloadConfig();
					plugin.dataManager.categories.reloadConfig();
					plugin.directory.clearScenes();
					plugin.collectCategories();
					plugin.collectGUIs();
					sender.sendMessage(plugin.format("&b&lConfigurations successfully reloaded!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("info")) {
					sender.sendMessage(ChatColor.GRAY + "AdvancedHelp v" + plugin.getDescription().getVersion() + " created by " + ChatColor.DARK_AQUA + "CodedRed");
					sender.sendMessage(ChatColor.GRAY + "Check out spigot site for more info!" + ChatColor.AQUA + "\nhttps://tinyurl.com/y7jeux3a");
					return true;
				}
				if (args[0].equalsIgnoreCase("debug")) {
					plugin.dataManager.cfg.reloadConfig();
					plugin.dataManager.gui.reloadConfig();
					Debugger debug = new Debugger(plugin);
					debug.run();
					debug.clean();
					plugin.getDateFormat();
					plugin.dataManager.cfg.reloadConfig();
					plugin.dataManager.gui.reloadConfig();
					plugin.dataManager.pages.reloadConfig();
					plugin.dataManager.categories.reloadConfig();
					plugin.directory.clearScenes();
					plugin.collectCategories();
					plugin.collectGUIs();
					sender.sendMessage(plugin.format("&a&lConfigurations successfully debugged!"));
					sender.sendMessage(plugin.format("&c&oIf problems are still occurring, check out the spigot site for default config info! &bhttps://tinyurl.com/y7jeux3a"));
					return true;
				}
				sender.sendMessage(message);
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/" + ChatColor.GRAY + "ahelp reload");
				sender.sendMessage(ChatColor.LIGHT_PURPLE + "/" + ChatColor.GRAY + "ahelp info");
				return true;
			}

		return false;
	}

}
