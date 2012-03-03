package com.mcnsa.instanceportals;

import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcnsa.instanceportals.listeners.PlayerListener;
import com.mcnsa.instanceportals.managers.CommandManager;
import com.mcnsa.instanceportals.managers.PersistanceManager;
import com.mcnsa.instanceportals.managers.PlayerManager;
import com.mcnsa.instanceportals.managers.TransportManager;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class InstancePortals extends JavaPlugin {
	// load the minecraft logger
	Logger log = Logger.getLogger("Minecraft");

	// APIs
	public PermissionManager permissions = null;

	// and commands
	public CommandManager commandManager = null;

	// keep track of listeners
	public PlayerListener playerListener = null;
	
	// the player manager
	public PlayerManager playerManager = null;
	
	// and finally, the transport manager
	public TransportManager transportManager = null;
	
	// keep track of portals between reloads
	public PersistanceManager persistanceManager = null;

	public void onEnable() {
		// set up APIs
		this.setupPermissions();
		
		// setup things
		commandManager = new CommandManager(this);
		
		// start the transport manager
		transportManager = new TransportManager(this);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, transportManager, 5, 5);
		
		playerManager = new PlayerManager(this);
		
		// set up listeners
		playerListener = new PlayerListener(this);
		
		// load persistance
		persistanceManager = new PersistanceManager(this);
		persistanceManager.readPersistance();
		
		log("plugin enabled!");
	}
	
	public void onDisable() {
		persistanceManager.writePersistance();
		log("plugin disabled!");
	}

	// for simpler logging
	public void log(String info) {
		log.info("[InstancePortals] " + info);
	}

	// for error reporting
	public void error(String info) {
		log.info("[InstancePortals] <ERROR> " + info);
	}

	// for debugging
	// (disable for final release)
	public void debug(String info) {
		log.info("[InstancePortals] <DEBUG> " + info);
	}

	// load the permissions plugin
	public void setupPermissions() {
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
			this.permissions = PermissionsEx.getPermissionManager();
			log("permissions successfully loaded!");
		}
		else {
			error("PermissionsEx not found!");
		}
	}

	// just an interface function for checking permissions
	// if permissions are down, default to OP status.
	public boolean hasPermission(Player player, String permission) {
		if(permissions != null) {
			return permissions.has(player, "instanceportals." + permission);
		}
		else {
			return player.isOp();
		}
	}
}
