package com.mcnsa.instanceportals.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcnsa.instanceportals.InstancePortals;

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
}
