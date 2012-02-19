package com.mcnsa.instanceportals.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;

public class PlayerListener implements Listener {
	InstancePortals plugin = null;
	public PlayerListener(InstancePortals instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void preprocessHandler(PlayerCommandPreprocessEvent event) {
		// if the command is cancelled, back out
		if(event.isCancelled()) return;
		
		// intercept the command
		if(plugin.commandManager.handleCommand(event.getPlayer(), event.getMessage())) {
			// we handled it, cancel it
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer(); // Grab Player
        
		// make sure they're not already in transport
        if(plugin.transportManager.inTransport(player)) {
        	plugin.debug("cancelled handle, player was already in transport!");
        	return;
        }
		
        Location loc = player.getLocation(); // Grab Location
        
		// get their selection
		LocalWorld world = plugin.getWEAPI().getSession(player).getSelectionWorld();
		Region region = null;
		try {
			region = plugin.getWEAPI().getSession(player).getSelection(world);
		} catch (IncompleteRegionException e) {
			return;
		}
		
		if(contains(region, loc)) {
			ColourHandler.sendMessage(player, "You're in your selection!!");
			// and queue them for transit!
			plugin.transportManager.queueTransport(player, new Location(player.getWorld(), 0, 100, 0));
			plugin.debug("player queued for transport!");
		}
		
		// and handle transports!
		plugin.transportManager.handleTransports();
	}

    private boolean contains(Region r, Location l) {
        if (!r.getWorld().getName().equals(l.getWorld().getName())) {
        	plugin.debug("you're not in the right world");
            return false;
        }
        
        Vector min = r.getMinimumPoint();
        Vector max = r.getMaximumPoint();
        max.add(new Vector(1, 1, 1));
        
        if(!(l.getX() >= min.getX() && l.getX() <= max.getX())) {
        	return false;
        }
        
        if(!(l.getY() >= min.getY() && l.getY() <= max.getY())) {
        	return false;
        }
        
        if(!(l.getZ() >= min.getZ() && l.getZ() <= max.getZ())) {
        	return false;
        }
        
        return true;
    }
}
