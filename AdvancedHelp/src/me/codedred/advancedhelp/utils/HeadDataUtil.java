package me.codedred.advancedhelp.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class HeadDataUtil {
	/**
	 * Gets block from HeadDatabase if the item ID exists
	 * 
	 * @param ID from HeadDatabase
	 * @return ItemStack of correct item
	 */
	public static ItemStack getHead(String ID) {
		HeadDatabaseAPI api = new HeadDatabaseAPI();
		ItemStack item;
	       try{
	           item = api.getItemHead(ID);
	       }
	       catch(NullPointerException nullpointer){
	          item = new ItemStack(Material.getMaterial("BARRIER"));
	          return item;
	       }
	       return item;
	}
}
