package com.mcnsa.instanceportals.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.containers.InstanceSet;

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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerInteract(PlayerInteractEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		// we only care when they right click a block
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// send it to the player manager
			if(plugin.playerManager.onBlockSelect(event.getPlayer(), event.getClickedBlock())) {
				// cancel it so nothing else happens
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerQuit(PlayerQuitEvent event) {
		// if they had any open portal definitions, close them
		if(plugin.playerManager.playerDefiningPortal(event.getPlayer())) {
			plugin.playerManager.cancelPortalDefinition(event.getPlayer());
		}
		// if they had any open instance set definitions, close them
		if(plugin.playerManager.playerDefiningInstanceSet(event.getPlayer())) {
			plugin.playerManager.cancelInstanceSetDefinition(event.getPlayer());
		}
		
		// now check to see if they were in an instance
		if(plugin.transportManager.playerInInstance(event.getPlayer())) {
			// they're in an instance! tp them out so they don't cheat!
			// get the mean location of the entrance portal
			InstanceSet instanceSet = plugin.transportManager.playerInstanceSet(event.getPlayer());
			if(instanceSet == null) {
				// something went wrong
				return;
			}
			
			// now get them out of there!
			instanceSet.bootPlayer(event.getPlayer());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerDied(PlayerDeathEvent event) {
		// check to see if they were in an instance
		if(plugin.transportManager.playerInInstance((Player)event.getEntity())) {
			// they're in an instance! tp them out so they don't cheat!
			// get the mean location of the entrance portal
			InstanceSet instanceSet = plugin.transportManager.playerInstanceSet((Player)event.getEntity());
			if(instanceSet == null) {
				// something went wrong
				return;
			}
			
			// now get them out of there!
			instanceSet.bootPlayer((Player)event.getEntity());
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerTeleported(PlayerTeleportEvent event) {
		// check to see if they were in an instance
		if(plugin.transportManager.playerInInstance(event.getPlayer())) {
			// they're in an instance! tp them out so they don't cheat!
			// get the mean location of the entrance portal
			InstanceSet instanceSet = plugin.transportManager.playerInstanceSet(event.getPlayer());
			if(instanceSet == null) {
				// something went wrong
				return;
			}
			
			// now get them out of there!
			instanceSet.bootPlayer((Player)event.getEntity());
		}
	}
}
