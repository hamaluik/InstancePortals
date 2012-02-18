package com.mcnsa.instanceportals;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.WorldEditAPI;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class InstancePortals extends JavaPlugin {
	// load the minecraft logger
	Logger log = Logger.getLogger("Minecraft");

	// APIs
	public PermissionManager permissions = null;
    protected WorldEditAPI worldEditAPI = null;

	public void onEnable() {
		// set up APIs
		this.setupPermissions();
		this.checkForWorldEdit();
		
		
		log("plugin enabled!");
	}
	
	public void onDisable() {
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
			return permissions.has(player, "mcnsachat2." + permission);
		}
		else {
			return player.isOp();
		}
	}
	
	// load worldedit
    private void checkForWorldEdit() {
        if (this.getServer().getPluginManager().getPlugin("WorldEdit") != null) {
            this.worldEditAPI = new WorldEditAPI((WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit"));
        }
    }
    
    // access the worldedit API
    public WorldEditAPI getWEAPI() {
        return this.worldEditAPI;
    }
}
