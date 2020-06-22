package me.codedred.advancedhelp.data;

import me.codedred.advancedhelp.Main;
import me.codedred.advancedhelp.data.files.Categories;
import me.codedred.advancedhelp.data.files.Config;
import me.codedred.advancedhelp.data.files.GUI;
import me.codedred.advancedhelp.data.files.Pages;

public class DataManager {

	public Categories categories;
	public Config cfg;
	public GUI gui;
	public Pages pages;
	
	public DataManager(Main plugin) {		
		this.categories = new Categories(plugin);
		this.cfg = new Config(plugin);
		this.gui = new GUI(plugin);
		this.pages = new Pages(plugin);
	}
	
	// TODO add SQL support?
	
	
}
