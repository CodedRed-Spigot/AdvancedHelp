package me.codedred.advancedhelp.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import me.codedred.advancedhelp.AdvancedHelp;


public class Debugger {

	private AdvancedHelp plugin;
	public Debugger(AdvancedHelp plugin) {
		this.plugin = plugin;
	}

	
	public void run() {
		FileConfiguration PAGES = plugin.getPages();
		FileConfiguration GROUP = plugin.getCategories();
		FileConfiguration CONFIG = plugin.getConfig();
		FileConfiguration GUI = plugin.getGUI();
		
		File helpFile = new File(plugin.getDataFolder(), "help.yml");
		if (helpFile.length() == 0) {
			PAGES.createSection("pages.1");
			List<String> p = new ArrayList<String>();
			p.add("&bAdd unlimited lines and pages!");
			p.add("&bYou can use JSON text as well!");
			PAGES.set("pages.1", p);
			plugin.dataManager.pages.saveConfig();
			
		}
		File catFile = new File(plugin.getDataFolder(), "categories.yml");
		if (catFile.length() == 0) {
			GROUP.createSection("categories");
			List<String> c = new ArrayList<String>();
			c.add("&bCustomize categories anyway you would like!");
			GROUP.set("categories.example.default", true);
			GROUP.set("categories.example.1", c);
			plugin.dataManager.categories.saveConfig();
		}
		// settings file
		// *
		if (!CONFIG.contains("messages.category-not-found")) {
			CONFIG.set("messages.category-not-found", "&bCategory not found!");
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (!CONFIG.contains("messages.page-not-found")) {
			CONFIG.set("messages.page-not-found", "&bPage not found!");
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (!CONFIG.contains("messages.no-permission")) {
			CONFIG.set("messages.no-permission", "&cYou do not have permission to do this!");
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (!CONFIG.contains("prefix")) {
			CONFIG.set("prefix", "&7[&bAdvancedHelp&7]");
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (CONFIG.contains("checkUpdates")) {
			CONFIG.set("checkUpdates", null);
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (!CONFIG.contains("dateFormat")) {
			CONFIG.set("dateFormat", "HH:mm:ss dd-MM-yyyy");
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (CONFIG.contains("gui.argument") || CONFIG.contains("gui.chest-size")) {
			CONFIG.set("gui", null);
			plugin.dataManager.cfg.saveConfig();
		}
		if (!CONFIG.contains("gui")) {
			CONFIG.set("gui.enabled", true);
			CONFIG.set("gui.hide-attributes", true);
			CONFIG.set("gui.close-page.enabled", true);
			CONFIG.set("gui.close-page.item-name", "&c&lClose");
			CONFIG.set("gui.close-page.item-material", "BARRIER");
			CONFIG.set("gui.close-page.enchanted", false);
			List<String> l = new ArrayList<String>();
			l.add("&c&oClick to close!");
			CONFIG.set("gui.close-page.lore", l);
			
			CONFIG.set("gui.home-page.enabled", true);
			CONFIG.set("gui.home-page.item-name", "&c&lHome Page");
			CONFIG.set("gui.home-page.item-material", "BARRIER");
			CONFIG.set("gui.home-page.enchanted", false);
			List<String> p = new ArrayList<String>();
			p.add("&c&oClick to go back!");
			CONFIG.set("gui.home-page.lore", p);
		
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (CONFIG.contains("gui-lang")) {
			CONFIG.set("gui-lang", null);
			plugin.dataManager.cfg.saveConfig();
		}
		if (!CONFIG.contains("cmd-priority")) {
			CONFIG.set("cmd-priority.gui", true);
			plugin.dataManager.cfg.saveConfig();
			
		}
		if (!CONFIG.contains("refresh-guis")) {
			CONFIG.set("refresh-guis", false);
			plugin.dataManager.cfg.saveConfig();
			
		}
		// gui.yml
		File guiFile = new File(plugin.getDataFolder(), "gui.yml");
		if (guiFile.length() == 0) {
			GUI.set("gui.main-page.title", "&e&lHelp GUI");
			GUI.set("gui.main-page.slots", 18);
			GUI.set("gui.main-page.close-button", 14);
			GUI.set("gui.main-page.items.1.item-name", "&6&lFactions");
			GUI.set("gui.main-page.items.1.item-material", "DIAMOND_SWORD");
			GUI.set("gui.main-page.items.1.enchanted", true);
			List<String> l = new ArrayList<String>();
			l.add("&6Click to see &lFactions&6 help!");
			GUI.set("gui.main-page.items.1.lore", l);
			GUI.set("gui.main-page.items.1.item-slot", 3);
			GUI.set("gui.main-page.items.1.run", "GUI");
			GUI.set("gui.main-page.items.1.value", "factions");

			GUI.set("gui.main-page.items.2.item-name", "&a&lSkyblock");
			GUI.set("gui.main-page.items.2.item-material", "GRASS_BLOCK");
			GUI.set("gui.main-page.items.2.item-amount", 10);
			GUI.set("gui.main-page.items.2.enchanted", true);
			List<String> sl = new ArrayList<String>();
			sl.add("&aClick to see &lSkyblock &ahelp!");
			GUI.set("gui.main-page.items.2.lore", sl);
			GUI.set("gui.main-page.items.2.item-slot", 5);
			GUI.set("gui.main-page.items.2.run", "GUI");
			GUI.set("gui.main-page.items.2.value", "skyblock");
			
			GUI.set("gui.main-page.items.3.item-name", "&b&lEconomy");
			GUI.set("gui.main-page.items.3.item-material", "DIAMOND_BLOCK");
			GUI.set("gui.main-page.items.3.enchanted", true);
			List<String> tl = new ArrayList<String>();
			tl.add("&bClick to see &lEconomy &bhelp!");
			GUI.set("gui.main-page.items.3.lore", tl);
			GUI.set("gui.main-page.items.3.item-slot", 7);
			GUI.set("gui.main-page.items.3.run", "GUI");
			GUI.set("gui.main-page.items.3.value", "economy");
			
			//cosmetic
			
			//GUI.set("gui.main-page.items.4.item-name", "&bAdvanced&6Help");
			//GUI.set("gui.main-page.items.4.item-material", "LIGHT_BLUE_STAINED_GLASS_PANE");
			//GUI.set("gui.main-page.items.4.enchanted", false);
			//GUI.set("gui.main-page.items.4.item-slot", 1);
			//GUI.set("gui.main-page.items.4.run", "COSMETIC");
			
			
			//factions
			GUI.set("gui.factions.title", "&6Factions GUI");
			GUI.set("gui.factions.slots", 27);
			GUI.set("gui.factions.home-button", 23);
			GUI.set("gui.factions.items.1.item-name", "&cRules");
			GUI.set("gui.factions.items.1.item-material", "PAPER");
			GUI.set("gui.factions.items.1.enchanted", true);
			List<String> f1 = new ArrayList<String>();
			f1.add("&cClick to view the rules!");
			GUI.set("gui.factions.items.1.lore", f1);
			GUI.set("gui.factions.items.1.item-slot", 12);
			GUI.set("gui.factions.items.1.run", "CATEGORY");
			GUI.set("gui.factions.items.1.value", "faction-rules");
			GUI.set("gui.factions.items.2.item-name", "&aftop");
			GUI.set("gui.factions.items.2.item-material", "WOODEN_SWORD");
			GUI.set("gui.factions.items.2.enchanted", true);
			List<String> f2 = new ArrayList<String>();
			f2.add("&a&oClick to view ftop!");
			GUI.set("gui.factions.items.2.lore", f2);
			GUI.set("gui.factions.items.2.item-slot", 16);
			GUI.set("gui.factions.items.2.run", "PLAYER-CMD");
			List<String> cmd1 = new ArrayList<String>();
			cmd1.add("ftop");
			GUI.set("gui.factions.items.2.value", cmd1);
			
			// skyblock
			GUI.set("gui.skyblock.title", "&aSkyblock GUI");
			GUI.set("gui.skyblock.slots", 27);
			GUI.set("gui.skyblock.home-button", 23);
			GUI.set("gui.skyblock.items.1.item-name", "&cRules");
			GUI.set("gui.skyblock.items.1.item-material", "PAPER");
			GUI.set("gui.skyblock.items.1.enchanted", true);
			List<String> s1 = new ArrayList<String>();
			s1.add("&cClick to view the rules!");
			GUI.set("gui.skyblock.items.1.lore", s1);
			GUI.set("gui.skyblock.items.1.item-slot", 12);
			GUI.set("gui.skyblock.items.1.run", "CATEGORY");
			GUI.set("gui.skyblock.items.1.value", "skyblock-rules");
			GUI.set("gui.skyblock.items.2.item-name", "&5Skyblock Cmd");
			GUI.set("gui.skyblock.items.2.item-material", "SAND");
			GUI.set("gui.skyblock.items.2.enchanted", true);
			List<String> s2 = new ArrayList<String>();
			s2.add("&a&oClick to run skyblock command!");
			GUI.set("gui.skyblock.items.2.lore", s2);
			GUI.set("gui.skyblock.items.2.item-slot", 16);
			GUI.set("gui.skyblock.items.2.run", "PLAYER-CMD");
			List<String> cmd2 = new ArrayList<String>();
			cmd2.add("ss");
			GUI.set("gui.skyblock.items.2.value", cmd2);
			
			// economy
			GUI.set("gui.economy.title", "&bEconomy GUI");
			GUI.set("gui.economy.slots", 27);
			GUI.set("gui.economy.home-button", 23);
			GUI.set("gui.economy.items.1.item-name", "&cRules");
			GUI.set("gui.economy.items.1.item-material", "PAPER");
			GUI.set("gui.economy.items.1.enchanted", true);
			List<String> e1 = new ArrayList<String>();
			e1.add("&cClick to view the rules!");
			GUI.set("gui.economy.items.1.lore", e1);
			GUI.set("gui.economy.items.1.item-slot", 12);
			GUI.set("gui.economy.items.1.run", "PAGE");
			GUI.set("gui.economy.items.1.value", 1);
			GUI.set("gui.economy.items.2.item-name", "&3Change Weather");
			GUI.set("gui.economy.items.2.item-material", "GLASS");
			GUI.set("gui.economy.items.2.enchanted", true);
			List<String> e2 = new ArrayList<String>();
			e2.add("&a&oClick to change the weather!");
			GUI.set("gui.economy.items.2.lore", e2);
			GUI.set("gui.economy.items.2.item-slot", 16);
			GUI.set("gui.economy.items.2.run", "CONSOLE-CMD");
			List<String> cmd3 = new ArrayList<String>();
			cmd3.add("weather world clear");
			GUI.set("gui.economy.items.2.value", cmd3);
			
			plugin.dataManager.gui.saveConfig();
			
		}
		
		if (!GUI.contains("gui.main-page")) {
			GUI.set("gui.main-page.title", "&4Help GUI");
			GUI.set("gui.main-page.slots", 54);
			GUI.set("gui.main-page.close-button", 50);
			GUI.set("gui.main-page.items.1.item-name", "&6&lFactions");
			GUI.set("gui.main-page.items.1.item-material", "DIAMOND_SWORD");
			GUI.set("gui.main-page.items.1.enchanted", true);
			List<String> l = new ArrayList<String>();
			l.add("&6Click to see &lFactions&6 help!");
			GUI.set("gui.main-page.items.1.lore", l);
			GUI.set("gui.main-page.items.1.item-slot", 12);
			GUI.set("gui.main-page.items.1.run", "GUI");
			GUI.set("gui.main-page.items.1.value", "factions");

			GUI.set("gui.main-page.items.2.item-name", "&a&lSkyblock");
			GUI.set("gui.main-page.items.2.item-material", "GRASS_BLOCK");
			GUI.set("gui.main-page.items.2.enchanted", true);
			List<String> sl = new ArrayList<String>();
			sl.add("&aClick to see &lSkyblock &ahelp!");
			GUI.set("gui.main-page.items.2.lore", sl);
			GUI.set("gui.main-page.items.2.item-slot", 16);
			GUI.set("gui.main-page.items.2.run", "GUI");
			GUI.set("gui.main-page.items.2.value", "skyblock");
			
			GUI.set("gui.main-page.items.3.item-name", "&b&lEconomy");
			GUI.set("gui.main-page.items.3.item-material", "DIAMOND_BLOCK");
			GUI.set("gui.main-page.items.3.enchanted", true);
			List<String> tl = new ArrayList<String>();
			tl.add("&bClick to see &lEconomy &bhelp!");
			GUI.set("gui.main-page.items.3.lore", tl);
			GUI.set("gui.main-page.items.3.item-slot", 32);
			GUI.set("gui.main-page.items.3.run", "GUI");
			GUI.set("gui.main-page.items.3.value", "economy");
			
			plugin.dataManager.gui.saveConfig();
		}

	}    
	// old old old 3+ year old configuration options. 
	public void clean() {
		if (plugin.dataManager.pages.getConfig().contains("AdvancedHelpPages")) {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[AdvancedHelp]" + ChatColor.RED + " Change \"AdvancedHelpPages:\" to \"pages:\" inside help.yml file!");
		}
		if (plugin.dataManager.pages.getConfig().contains("AdvancedHelpCategories")) {
			plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[AdvancedHelp]" + ChatColor.RED + " Change \"AdvancedHelpCategories:\" to \"categories:\" inside help.yml file!");
		}
		plugin.dataManager.categories.getConfig().getConfigurationSection("categories").getKeys(false).forEach(key -> {
			if (plugin.dataManager.categories.getConfig().contains("categories." + key + ".Default")) {
				if (plugin.dataManager.categories.getConfig().contains("categories." + key + ".default")) {
					plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp]" + ChatColor.YELLOW + " You may remove 'Default' because the correct 'default' is in use!");
					return;
				}
				boolean def = plugin.dataManager.categories.getConfig().getBoolean("categories." + key + ".Default");
				plugin.dataManager.categories.getConfig().set("categories." + key + ".default", def);
				plugin.dataManager.categories.saveConfig();
			}
		});

		
	}
}
