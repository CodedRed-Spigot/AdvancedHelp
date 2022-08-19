package me.codedred.advancedhelp.data;

import me.codedred.advancedhelp.AdvancedHelp;
import me.codedred.advancedhelp.data.files.Categories;
import me.codedred.advancedhelp.data.files.Config;
import me.codedred.advancedhelp.data.files.GUI;
import me.codedred.advancedhelp.data.files.Pages;

public class DataManager {

	public Categories categories;
	public Config cfg;
	public GUI gui;
	public Pages pages;
	
	public DataManager(AdvancedHelp plugin) {
		categories = new Categories(plugin);
		cfg = new Config(plugin);
		gui = new GUI(plugin);
		pages = new Pages(plugin);
	}
	
	// TODO add SQL support?
	
	
}
