package me.codedred.advancedhelp.utils;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholdersUtil {

	/**
	 * Set placeholders to a certain message
	 * 
	 * @param player require parameter for PAPI
	 * @param msg that needs placeholders
	 * @return updated message
	 */
	public static String setPlaceholders(Player player, String msg) {
		return PlaceholderAPI.setPlaceholders(player, msg);
	}
	
	/**
	 * Translate all PAPI placeholders for a ItemStack item
	 * 
	 * @param item that needs translating
	 * @param player required parameter for PAPI
	 * @return updated ItemStack block
	 */
	public static ItemStack translatePlaceholders(ItemStack item, Player player) {
		
		ItemMeta meta = item.getItemMeta();
		String papiName = item.getItemMeta().getDisplayName();
		papiName = PlaceholderAPI.setPlaceholders(player, papiName);
		meta.setDisplayName(papiName);
		
		List<String> papiLore = item.getItemMeta().getLore();
		papiLore = PlaceholderAPI.setPlaceholders(player, papiLore);
		meta.setLore(papiLore);
		item.setItemMeta(meta);
		
		return item;
	}
	
}
