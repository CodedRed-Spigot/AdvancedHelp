package me.codedred.advancedhelp.commands.tabs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.codedred.advancedhelp.AdvancedHelp;

public class HelpTab implements TabCompleter {

	private List<String> categories = new ArrayList<>();
	private final List<String> privateCategories = new ArrayList<>();
	private final List<String> adminArgs = new ArrayList<>();

	private final AdvancedHelp plugin;
	public HelpTab(AdvancedHelp plugin) {
		this.plugin = plugin;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("help") || cmd.getName().equalsIgnoreCase("ehelp")
				|| cmd.getName().equalsIgnoreCase("?")) {
			
			List<String> finalOne = new ArrayList<>();
			List<String> pages = new ArrayList<>();
			
			if (categories.isEmpty()) {
				categories = plugin.directory.listViewer.getList("categories");
				getDefaultCaddys(categories);
			}
			
			if (args.length == 1) {	
				for (String s : categories) {
					if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
						if (privateCategories.contains(s)) {
							if (sender.hasPermission("help." + s))
								finalOne.add(s);
						}
						else
							finalOne.add(s);
					}
				}
				return finalOne;
			}

			else if (args.length == 2 && categories.contains(args[0])) {
				plugin.getCategories().getConfigurationSection("categories." + args[0]).getKeys(false)
						.forEach((key) -> {
							if (!key.equalsIgnoreCase("default")) {
								pages.add(key.replace("'", ""));
							}

						});
				return pages;
			}
			return null;
		}

		if (cmd.getName().equalsIgnoreCase("ahelp")) {
			if (adminArgs.isEmpty()) {
				adminArgs.add("reload");
				adminArgs.add("debug");
				adminArgs.add("info");
			}

			List<String> finalOne = new ArrayList<>();
			if (args.length == 1) {	
				for (String s : adminArgs) {
					if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
						finalOne.add(s);
					}
				}
				return finalOne;
			}
		}

		return null;
	}

	private void getDefaultCaddys(List<String> categories) {
		for (String caddy : categories) {
			if (plugin.getCategories().contains("categories." + caddy + ".default")
					&& !plugin.getCategories().getBoolean("categories." + caddy + ".default")) {
				privateCategories.add(caddy);
			}
		}

	}

}
