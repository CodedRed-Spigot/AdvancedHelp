package me.codedred.advancedhelp;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.codedred.advancedhelp.commands.AdminHelp;
import me.codedred.advancedhelp.commands.Help;
import me.codedred.advancedhelp.commands.QuestionMark;
import me.codedred.advancedhelp.commands.tabs.HelpTab;
import me.codedred.advancedhelp.data.DataManager;
import me.codedred.advancedhelp.data.Debugger;
import me.codedred.advancedhelp.listeners.AdminGUIClick;
import me.codedred.advancedhelp.listeners.GUIClick;
import me.codedred.advancedhelp.listeners.HelpGUIClick;
import me.codedred.advancedhelp.models.Directory;
import me.codedred.advancedhelp.utils.HexUtil;
import net.md_5.bungee.api.ChatColor;

public class AdvancedHelp extends JavaPlugin implements PluginMessageListener {
	
	public DataManager dataManager;
	public Directory directory;
	public static String dateFormat;
	public List<String> bungeeServers = new ArrayList<>();
	
	@Override
	public void onEnable() {
		dataManager = new DataManager(this);
		directory = new Directory();
		
		Debugger debug = new Debugger(this);
		debug.run();
		debug.clean();
		
		if (hasPAPI())
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "PlaceholderAPI Hooked!");
		if (hasHDB())
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "HeadDatabase Hooked!");
		
        if ( getServer().spigot().getConfig().getBoolean( "settings.bungeecord" ) ) {
        	getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    	    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

    	    getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "BungeeCord Hooked!");
			
    	    //registerBungeeServers();
		}
		
		collectCategories();
		collectGUIs();
		getDateFormat();
		
		checkForUpdates();
		registerMetrics();
		registerEvents();
		registerCommands();
		
		getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "Successfully Enabled!");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServers")) {
		      // Use the code sample in the 'Response' sections below to read
		      // the data.
			String[] serverList = in.readUTF().split(", ");
			bungeeServers.addAll(Arrays.asList(serverList));
		}
	}
		
//	public void registerBungeeServers() {
//		// TODO make this work, this isnt required but would make it look nicer
//		ByteArrayDataOutput out = ByteStreams.newDataOutput();
//		out.writeUTF("GetServers");
//
//		getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
//	}
	
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new AdminGUIClick(this), this);
		pm.registerEvents(new HelpGUIClick(this), this);
		pm.registerEvents(new GUIClick(this), this);
	}
	
	private void registerCommands() {
		getCommand("help").setExecutor(new Help(this));
		getCommand("ehelp").setExecutor(new Help(this));
		getCommand("?").setExecutor(new QuestionMark(this));
		getCommand("ahelp").setExecutor(new AdminHelp(this));
		
		if (isNewerVersion()) {
			getCommand("help").setTabCompleter(new HelpTab(this));
			getCommand("ehelp").setTabCompleter(new HelpTab(this));
			getCommand("ahelp").setTabCompleter(new HelpTab(this));
			getCommand("?").setTabCompleter(new HelpTab(this));
		}
	}
	
	private void registerMetrics() {
		Metrics metrics = new Metrics(this);
		try {
			metrics.addCustomChart(new Metrics.SimplePie("gui_enabled", () -> ("" + getConfig().getBoolean("gui.enabled"))));
			metrics.addCustomChart(new Metrics.SimplePie("hide_attributes", () -> ("" + getConfig().getBoolean("gui.hide-attributes"))));
			metrics.addCustomChart(new Metrics.SimplePie("close_page", () -> ("" + getConfig().getBoolean("gui.close-page.enabled"))));
			metrics.addCustomChart(new Metrics.SimplePie("home_page", () -> ("" + getConfig().getBoolean("gui.home-page.enabled"))));
			metrics.addCustomChart(new Metrics.SimplePie("refresh_gui", () -> ("" + getConfig().getBoolean("refresh-guis"))));
			
		} catch(Exception e) {
			return;
		}
	}
	
	private void checkForUpdates() {
		UpdateChecker updater = new UpdateChecker(this, 44478);
        try {
            if (updater.checkForUpdates()) {
                getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
                getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "You are using an older version of AdvancedHelp!");
                getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Download the newest version here:");
                getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "https://goo.gl/mVXpDL");
                getServer().getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            } else {
            	getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "Plugin is up to date! -" 
            			+ getDescription().getVersion());
            }
        } catch (Exception e) {
            getLogger().info("[AdvancedHelp] Could not check for updates! Stacktrace:");
            e.printStackTrace();
        }
	}
	
	public FileConfiguration getConfig() {
		return dataManager.cfg.getConfig();
	}
	
	public FileConfiguration getPages() {
		return dataManager.pages.getConfig();
	}
	
	public FileConfiguration getCategories() {
		return dataManager.categories.getConfig();
	}
	
	public FileConfiguration getGUI() {
		return dataManager.gui.getConfig();
	}
	
	public String format(String msg) {
		if (Bukkit.getVersion().contains("1.16")) {
			msg = HexUtil.hex(msg);
		}
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public void getDateFormat() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat(getConfig().getString("dateFormat"));
		dateFormat = format.format(now);
	}
	
	public boolean hasPAPI() {
		return Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
	}
	
	public boolean hasHDB() {
		return Bukkit.getServer().getPluginManager().getPlugin("HeadDatabase") != null;
	}
	
	public boolean isNewerVersion() {
		 try {
	            Class<?> class_Material = Material.class;
	            Method method = class_Material.getDeclaredMethod("matchMaterial", String.class, Boolean.TYPE);
	            return (method != null);
	        } catch(ReflectiveOperationException ex) {
	        	return false;	
	        }
	}
	
	public void collectCategories() {
		List<String> names = new ArrayList<>(getCategories().getConfigurationSection("categories").getKeys(false));
		directory.listViewer.addList("categories", names);
	}
	
	public void collectGUIs() {
		List<String> names = new ArrayList<>(getGUI().getConfigurationSection("gui").getKeys(false));
		directory.listViewer.addList("guis", names);
	}

}
