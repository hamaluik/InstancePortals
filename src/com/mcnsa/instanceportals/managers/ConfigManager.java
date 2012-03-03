package com.mcnsa.instanceportals.managers;

import org.bukkit.configuration.file.FileConfiguration;

import com.mcnsa.instanceportals.InstancePortals;

public class ConfigManager {
	// store the main plugin for later access
	static InstancePortals plugin = null;
	public ConfigOptions options = new ConfigOptions();
	public ConfigManager(InstancePortals instance) {
		plugin = instance;
	}

	// load the configuration
	public Boolean load(FileConfiguration config) {
		// load the chat radius
		//plugin.debug("loading options...");
		
		options.updateTicks = config.getInt("update-ticks", 10);
		
		// successful
		return true;
	}

	// create a "class" in here to store config options!
	public class ConfigOptions {
		public Integer updateTicks = new Integer(10);
	}
}
