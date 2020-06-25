package me.codedred.advancedhelp;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
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

public class Main extends JavaPlugin implements PluginMessageListener {
	
	public DataManager dataManager;
	public Directory directory;
	public static String dateFormat;
	public List<String> bungeeServers = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		this.dataManager = new DataManager(this);
		this.directory = new Directory();
		
		Debugger debug = new Debugger(this);
		debug.run();
		debug.clean();
		
		if (hasPAPI())
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "PlaceholderAPI Hooked!");
		if (hasHDB())
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "[AdvancedHelp] " + ChatColor.WHITE + "HeadDatabase Hooked!");
		
        if ( getServer().spigot().getConfig().getBoolean( "settings.bungeecord" ) ) {
        	this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

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
		    for (String srv : serverList) {
				bungeeServers.add(srv);
		    	System.out.print(srv);
		    }
		}
	}
		
	public void registerBungeeServers() {
		// TODO make this work, this isnt required but would make it look nicer
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServers");
			
		this.getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
	
	private void registerEvents() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new AdminGUIClick(this), this);
		pm.registerEvents(new HelpGUIClick(this), this);
		pm.registerEvents(new GUIClick(this), this);
	}
	
	private void registerCommands() {
		this.getCommand("help").setExecutor(new Help(this));
		this.getCommand("ehelp").setExecutor(new Help(this));
		this.getCommand("?").setExecutor(new QuestionMark(this));
		this.getCommand("ahelp").setExecutor(new AdminHelp(this));
		
		if (this.isNewerVersion()) {
			this.getCommand("help").setTabCompleter(new HelpTab(this));
			this.getCommand("ehelp").setTabCompleter(new HelpTab(this));
			this.getCommand("ahelp").setTabCompleter(new HelpTab(this));
			this.getCommand("?").setTabCompleter(new HelpTab(this));
		}
	}
	
	private void registerMetrics() {
		Metrics metrics = new Metrics(this);
		try {
			metrics.addCustomChart(new Metrics.SimplePie("gui_enabled", () -> ("" + this.getConfig().getBoolean("gui.enabled"))));
			metrics.addCustomChart(new Metrics.SimplePie("hide_attributes", () -> ("" + this.getConfig().getBoolean("gui.hide-attributes"))));
			metrics.addCustomChart(new Metrics.SimplePie("close_page", () -> ("" + this.getConfig().getBoolean("gui.close-page.enabled"))));
			metrics.addCustomChart(new Metrics.SimplePie("home_page", () -> ("" + this.getConfig().getBoolean("gui.home-page.enabled"))));
			metrics.addCustomChart(new Metrics.SimplePie("refresh_gui", () -> ("" + this.getConfig().getBoolean("refresh-guis"))));
			
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
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public void getDateFormat() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat(getConfig().getString("dateFormat"));
		dateFormat = format.format(now);
	}
	
	public boolean hasPAPI() {
		if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
			return true;
		return false;
	}
	
	public boolean hasHDB() {
		if (Bukkit.getServer().getPluginManager().getPlugin("HeadDatabase") != null)
			return true;
		return false;
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
		List<String> names = new ArrayList<String>();
		for (String category : this.getCategories().getConfigurationSection("categories").getKeys(false)) {
			names.add(category);
		}
		directory.listViewer.addList("categories", names);
	}
	
	public void collectGUIs() {
		List<String> names = new ArrayList<String>();
		for (String gui : this.getGUI().getConfigurationSection("gui").getKeys(false)) {
			names.add(gui);
		}
		directory.listViewer.addList("guis", names);
	}

}
