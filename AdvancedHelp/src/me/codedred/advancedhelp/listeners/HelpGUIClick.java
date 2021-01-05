package me.codedred.advancedhelp.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.codedred.advancedhelp.Main;
import me.codedred.advancedhelp.events.HelpGUIEvent;
import me.codedred.advancedhelp.utils.PlaceholdersUtil;

public class HelpGUIClick implements Listener {
	
	private Main plugin;
	public HelpGUIClick(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onHelpGUIClick(HelpGUIEvent event) {
		
		Player player = event.getPlayer();
		FileConfiguration GUI = plugin.getGUI();
		
		if (plugin.getConfig().getBoolean("gui.close-page.enabled")) {
            if(fixTitle("gui.close-page.item-name", player)
        			.contains(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())) ){
            	player.closeInventory();
            	player.updateInventory();
            	return;
            }
        }
        if (plugin.getConfig().getBoolean("gui.home-page.enabled")) {
            if(fixTitle("gui.home-page.item-name", player)
        			.contains(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())) ){
            	Inventory f = plugin.directory.getMainInventory(player.getName());
            	player.updateInventory();
            	player.openInventory(f);
            	return;
            }
        }
        
       // different pathway buttons
        int slot = 99;
        List<Integer> slots = new ArrayList<Integer>();
        
        
        for (String i : GUI.getConfigurationSection("gui." + event.getInventoryName() + ".items").getKeys(false)) {
        		slots.clear();
        		
					slot = GUI.getInt("gui." + event.getInventoryName() + ".items." + i + ".item-slot");
					if (slot < 1) {
						String slotArray = GUI.getString("gui." + event.getInventoryName() + ".items." + i + ".item-slot").replace("[", "").replace("]", "").replace(" ", "");
						String[] sA = slotArray.split(",");
						for (String j : sA) {
							slots.add(Integer.parseInt(j));
						}
					}

                if ((slot == event.getSlot() + 1) || (slots.contains((event.getSlot() + 1)))) {
                	String type;
                	try {
                		type = getType("gui." + event.getInventoryName() + ".items." + i + ".run");
                	} catch (Exception e) {
                		return;
                	}

                	/**
                	 * OPEN-GUI
                	 */
                	if (type.equalsIgnoreCase("OPEN-GUI") || type.equalsIgnoreCase("OPEN_GUI")
                			|| type.equalsIgnoreCase("GUI")) {
                    	String nextGUI = getNextGUI("gui." + event.getInventoryName() + ".items." + i);	
                    	player.closeInventory();
                    	player.updateInventory();
                    	if (!GUI.getBoolean("gui." + nextGUI + ".default") && GUI.contains("gui." + nextGUI + ".default")) {
                    		if (!player.hasPermission("help." + nextGUI)) {
                    			player.sendMessage(plugin.format(plugin.getConfig().getString("prefix") + " " + 
                    											plugin.getConfig().getString("messages.no-permission")));
                            	return;
                    		}
                    	}

                    	Inventory f = plugin.directory.inventoryViewer.getInventories(
                    			player.getName()).get(plugin.directory.getInventoryID(player, nextGUI));
                        player.openInventory(f);
                        return;
                    }
                	
                	/**
                	 * CATEGORY
                	 */
                	else if (type.equalsIgnoreCase("CATEGORY")) {
                    	String category = "NULL";
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".category")) {
                    		category = GUI.getString("gui." + event.getInventoryName() + ".items." + i + ".category"); 
                    	}
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                    		category = GUI.getString("gui." + event.getInventoryName() + ".items." + i + ".value");
                    	}
                    	player.closeInventory();
                    	if (!category.contains(" "))
                    		Bukkit.dispatchCommand(player, "ehelp -push-" + category);	
                    	else
                    		Bukkit.dispatchCommand(player, "ehelp " + category);	
                    	player.updateInventory();
                    	return;
                    }
                	/**
                	 * MESSAGE
                	 */
                	else if (type.equalsIgnoreCase("MESSAGE")) {
                		if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                			for (String m : GUI.getStringList("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                				if (plugin.hasPAPI())
                					m = PlaceholdersUtil.setPlaceholders(player, m);
                				if (m.indexOf("{\"text\":") != -1) {
                					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ("tellraw " + player.getName() + " " +  m)
           								 .replace("%player%", player.getName())); 
                				} else {
                					player.sendMessage(plugin.format(m.replace("%player%", player.getName())));
                				}
                			}
                			player.closeInventory();
                			player.updateInventory();
                		}
                		return;
                	}
                    /**
                     * HELP-PAGE
                     */
                	else if (type.equalsIgnoreCase("HELP-PAGE") ||
                    		type.equalsIgnoreCase("HELP_PAGE") || 
                    		type.equalsIgnoreCase("PAGE")) {
                    	int page = 1;
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".page")) {
                    		page = GUI.getInt("gui." + event.getInventoryName() + ".items." + i + ".page");
                    	}
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                    		page = GUI.getInt("gui." + event.getInventoryName() + ".items." + i + ".value");
                    	}
                    	player.closeInventory();
                    	Bukkit.dispatchCommand(player, "ehelp " + page);	
                    	player.updateInventory();
                    	return;
                    }
                    
                    
                    /**
                     * PLAYER-COMMAND
                     */
                	else if (type.equalsIgnoreCase("PLAYER-CMD") || 
                    		type.equalsIgnoreCase("PLAYER_CMD")) {
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".commands")) {
                    		player.closeInventory();
                    		player.updateInventory();
                        	for (String cmd : GUI.getStringList("gui." + event.getInventoryName() + ".items." + i + ".commands")) {
                        		Bukkit.dispatchCommand(player, cmd.replace("%player%", player.getName()));	
                        	}
                        	return;
                    	}
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                    		player.closeInventory();
                    		player.updateInventory();
                        	for (String cmd : GUI.getStringList("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                        		Bukkit.dispatchCommand(player, cmd.replace("%player%", player.getName()));	
                        	}
                        	return;
                    	}
                    	return;
                    }
                    
                    /**
                     * CONSOLE-COMMAND
                     */
                	else if (type.equalsIgnoreCase("CONSOLE-CMD") ||
                    		type.equalsIgnoreCase("CONSOLE_CMD") ) {
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".commands")) {
                    		player.closeInventory();
                        	for (String cmd : GUI.getStringList("gui." + event.getInventoryName() + ".items." + i + ".commands")) {
                        		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));	
                        	}
                        	player.updateInventory();
                        	return;
                    	}
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                    		player.closeInventory();
                        	for (String cmd : GUI.getStringList("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                        		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));	
                        	}
                        	player.updateInventory();
                        	return;
                    	}
                    	return;
                    }
                    /**
                     * COSMETIC
                     */
                	else if (type.equalsIgnoreCase("COSMETIC")) {
                    	player.updateInventory();
                    	return;
                    }
                    
                    /**
                     * SERVER
                     */
                	else if (type.equalsIgnoreCase("SERVER")) {
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".server")) {
                    		player.closeInventory();
                        	player.updateInventory();
                        	String server  = GUI.getString("gui." + event.getInventoryName() + ".items." + i + ".server");
                        	executeBungeeCommand(server, player);
                        	return;
                    	}
                    	if (GUI.contains("gui." + event.getInventoryName() + ".items." + i + ".value")) {
                    		player.closeInventory();
                        	player.updateInventory();
                        	String server  = GUI.getString("gui." + event.getInventoryName() + ".items." + i + ".value");
                        	executeBungeeCommand(server, player);
                        	return;
                    	}
                    	return;
                    }
                }

           }
    }


	
	/**
	 * Runs /server Command for player through BungeeCord Channel
	 * 
	 * @param server, they are trying to connect to
	 * @param p - player that will be going through channel
	 */
	public void executeBungeeCommand(String server, Player p) {
		/*if (Main.bungeeServers.isEmpty()) 
			plugin.registerBungeeServers();
		p.sendMessage(Main.bungeeServers + "");
		if (!Main.bungeeServers.contains(server)) {
			p.sendMessage(plugin.format("&c&lServer not found.\n&cIs '" + server + "' online?"));
			return;
		} 
		 	TODO ** above is not required but would make it look nicer if server name is entered incorrectly. 
		*/
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		
		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	/**
	 * Collects the next GUI in a sequence
	 * 
	 * @param location of GUI information in the configuration
	 * @return the next GUI that is needed
	 */
	public String getNextGUI(String location) {
		String gui = "main-page";
		if (plugin.getGUI().contains(location + ".gui")) {
			gui = plugin.getGUI().getString(location + ".gui");
		}
		if (plugin.getGUI().contains(location + ".value")) {
			gui = plugin.getGUI().getString(location + ".value");
		}
		if (isGUI(gui) == false) {
			gui = "main-page";
		}
		return gui;
	}
	
	/**
	 * Check to see if the GUI exists 
	 * 
	 * @param gui name from config
	 * @return true if the GUI exists in the configuration
	 */
	public boolean isGUI(String gui) {
		for (String name : plugin.getGUI().getConfigurationSection("gui").getKeys(false)) {
			if (name.equals(gui))
				return true;
		}
		return false;
	}
	
	
	/**
	 * Get the type of run from config, if it doesnt exist create one
	 * 
	 * @param location from the configuration
	 * @return the type of run
	 */
	public String getType(String location) {
		if (!plugin.getGUI().contains(location)) {
              	plugin.getGUI().set(location, "CATEGORY");
              	plugin.dataManager.gui.saveConfig();
		}
		return plugin.getGUI().getString(location);
	}
	
	/**
	 * Fixes the display name for close/home buttons
	 * 
	 * @param location of string inside the configuration
	 * @param player that is inside the GUI
	 * @return a proper fixed display name
	 */
	public String fixTitle(String location, Player player) {
		String msg;
		if (plugin.hasPAPI())
			msg = PlaceholdersUtil.setPlaceholders(player, plugin.getConfig().getString(location));
		else
			msg = plugin.getConfig().getString(location);
		msg = ChatColor.stripColor(msg);
		msg = StringUtils.replace(msg, "%player%", player.getName());
		return msg;
	}
}