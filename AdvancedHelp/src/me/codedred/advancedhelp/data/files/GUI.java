package me.codedred.advancedhelp.data.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.codedred.advancedhelp.AdvancedHelp;

public class GUI {
	private final AdvancedHelp plugin;
	private FileConfiguration dataConfig = null;
	private File dataConfigFile = null;
	private final String name = "gui.yml";

	public GUI(AdvancedHelp plugin) {
		this.plugin = plugin;
		saveDefaultConfig();
	}

	public void reloadConfig() {
		if (dataConfigFile == null)
			dataConfigFile = new File(plugin.getDataFolder(), name);

		dataConfig = YamlConfiguration.loadConfiguration(dataConfigFile);

		InputStream defConfigStream = plugin.getResource(name);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
			dataConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (dataConfig == null)
			reloadConfig();
		return dataConfig;
	}

	public void saveConfig() {
		if ((dataConfig == null) || (dataConfigFile == null))
			return;
		try {
			getConfig().save(dataConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + dataConfigFile, ex);
		}
	}

	public void saveDefaultConfig() {
		if (dataConfigFile == null)
			dataConfigFile = new File(plugin.getDataFolder(), name);
		if (!dataConfigFile.exists())
			plugin.saveResource(name, false);
	}

}