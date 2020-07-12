package me.codedred.advancedhelp.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.codedred.advancedhelp.Main;
import me.codedred.advancedhelp.enums.Types;
import net.md_5.bungee.api.ChatColor;

public class ItemUtil {

	/**
	 * Creates an ItemStack with proper material if it exists. 1.8 - 1.14.x
	 * 
	 * @param material String of material to parse into item
	 * @param player that is creating the GUI
	 * @return the ItemStack of the material if it exists
	 */
	public static ItemStack createItem(String material, Player player) {
		
		try {
			if (material.toUpperCase().contains("PLAYER")) {
				if (material.toUpperCase().contains("PLAYER[")) {
					String name = material.substring(material.indexOf("[")+1, material.indexOf("]"));
					return getPlayerHead(name);
				}
				return getPlayerHead(player.getName());
			}
			if (material.contains("HDB["))
				if (hasHDB())
					return HeadDataUtil.getHead(material.replace("HDB[", "").replace("]", ""));
			if (isNewerVersion())
				return new ItemStack(Material.matchMaterial(material));
			return Types.matchMaterial(material);
		} catch (Exception e) {
			return new ItemStack(Material.matchMaterial("BARRIER"));
		}
	}
	
	/**
	 * Adds display name to item if the name exists
	 * 
	 * @param item that needs the display name
	 * @param name of the item
	 * @param player that is creating gui
	 * @return the fixed item with new display name
	 */
	public static ItemStack addDisplayName(ItemStack item, String name, Player player) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(format(name.replace("%player%", player.getName()).replace("%time%", Main.dateFormat)));
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Adds custom lore to item
	 * 
	 * @param item needed new lore
	 * @param lore lore from the config file
	 * @param player that is creating GUI
	 * @return updated item with new lore
	 */
	public static ItemStack addLore(ItemStack item, List<String> lore, Player player) {
		List<String> updatedLore = new ArrayList<String>();
		for (String l : lore) {
			updatedLore.add(format(l.replace("%player%", player.getName()).replace("%time%", Main.dateFormat)));
		}
		ItemMeta meta = item.getItemMeta();
		meta.setLore(updatedLore);
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Add glow to item
	 * 
	 * @param item that needs enchantment
	 * @return update item with glow feature
	 */
	public static ItemStack addGlow(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Add HIDE_ATTRIBUTES flag
	 * 
	 * @param item that needs enchantment
	 * @return update item with glow feature
	 */
	public static ItemStack addFlag(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * Gets the player head as a skull
	 * 
	 * @param player
	 * @return ItemStack of skull with players skin
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getPlayerHead(String player) {
		//String serverVersion = Bukkit.getVersion();
        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");

        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        ItemStack item = new ItemStack(type, 1);
        if(!isNewVersion) item.setDurability((short) 3);

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        skullMeta.setOwner(player);

        item.setItemMeta(skullMeta);
        return item;
	}

	
	/**
	 * Place item in GUI only if slot given is a String
	 * 
	 * @param item that needs to be in GUI
	 * @param slot positions
	 * @param GUI that needs the item
	 */
	public static void placeItem(ItemStack item, String slot, Inventory GUI) {
		try {
			slot = slot.replace("]", "").replace(" ", "").replace("[", "");
			String[] slots = slot.split(",");
			for (String s : slots) {
				GUI.setItem(Integer.parseInt(s)-1, item);;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(format("&cItem in GUI could not be created due to being out of bounds!"));
			Bukkit.getConsoleSender().sendMessage(format("&cItem type: " + item.getType()));
			Bukkit.getConsoleSender().sendMessage(format("&cItem GUI: " + GUI));
			Bukkit.getConsoleSender().sendMessage(format("&cItem name: " + item.getItemMeta().getDisplayName()));
		}
}
	
	/**
	 * Checks to see if HeadDatabase resource is installed in server
	 * 
	 * @return true if HDB is installed
	 */
	public static boolean hasHDB() {
		if (Bukkit.getServer().getPluginManager().getPlugin("HeadDatabase") != null)
			return true;
		return false;
	}
	
	/**
	 * Returns true is server version is up to date with new block IDs
	 * 
	 * @return true if version of server is 1.13+
	 */
	public static boolean isNewerVersion() {
		 try {
	            Class<?> class_Material = Material.class;
	            Method method = class_Material.getDeclaredMethod("matchMaterial", String.class, Boolean.TYPE);
	            return (method != null);
	        } catch(ReflectiveOperationException ex) {
	        	return false;	
	        }
	}
	
	/**
	 * Translate color codes
	 * 
	 * @param msg needing color code changing
	 * @return updated message
	 */
	private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
	public static String format(String msg) {
		if (Bukkit.getVersion().contains("1.16")) {
			//hex colors
			Matcher match = pattern.matcher(msg);
			while (match.find()) {
				String color = msg.substring(match.start(), match.end());
				msg = msg.replace(color, ChatColor.of(color) + "");
				match = pattern.matcher(msg);
			}
		}
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
}
