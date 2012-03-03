package com.mcnsa.instanceportals.managers;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.containers.Portal;
import com.mcnsa.instanceportals.containers.PortalRegion;
import com.mcnsa.instanceportals.util.ColourHandler;

public class PlayerManager {
	private InstancePortals plugin;
	private HashMap<Player, Portal> definingPortals = new HashMap<Player, Portal>();
	
	public PlayerManager(InstancePortals instance) {
		plugin = instance;
	}
	
	public boolean playerDefiningPortal(Player player) {
		return definingPortals.containsKey(player);
	}
	
	public void startPortalDefinition(Player player, String name) {
		// firt make sure that portal doesn't exist
		if(plugin.transportManager.portalExists(name)) {
			ColourHandler.sendMessage(player, "&4Error - a portal already exists with that name!");
			return;
		}
		
		Portal portal = new Portal(plugin, name);
		portal.entrance = new PortalRegion(plugin, player);
		definingPortals.put(player, portal);
		
		// and inform them
		ColourHandler.sendMessage(player, "&3To define the entrance portal, right click on the two corners of your portal");
		ColourHandler.sendMessage(player, "&3To define where your portal will go to, stand in that spot and type &f/pexit");
		ColourHandler.sendMessage(player, "&3When you're done, type &f/pdone&3. To cancel at any time, type &f/pcancel");
	}
	
	public boolean onBlockSelect(Player player, Block block) {
		// make sure we're tracking the player
		if(!definingPortals.containsKey(player)) {
			return false;
		}
		
		if(definingPortals.get(player).entrance.nextIsMax()) {
			// change the max point!
			definingPortals.get(player).entrance.updateMax(block.getLocation());
		}
		else {
			// change the min point!
			definingPortals.get(player).entrance.updateMin(block.getLocation());
		}
		ColourHandler.sendMessage(player, "&a(&f"+block.getLocation().getBlockX()+"&a,&f"+block.getLocation().getBlockY()+"&a,&f"+block.getLocation().getBlockZ()+"&a) has been registered!");
		
		// see if the portal region is fully defined
		if(definingPortals.get(player).entrance.portalDefined()) {
			ColourHandler.sendMessage(player, "&2Your portal entrance is now fully defined!");
			if(definingPortals.get(player).exit != null) {
				ColourHandler.sendMessage(player, "&2Your portal now fully defined! Type /pdone to save and activate it now!");
			}
		}
		
		return true;
	}
	
	public void definePortalExit(Player player) {
		// make sure we're tracking the player
		if(!definingPortals.containsKey(player)) {
			return;
		}
		
		definingPortals.get(player).exit = player.getLocation();
		ColourHandler.sendMessage(player, "&2Your portal exit is now defined!");
		if(definingPortals.get(player).entrance.portalDefined()) {
			ColourHandler.sendMessage(player, "&2Your portal now fully defined! Type /pdone to save and activate it now!");
		}
	}
	
	public void endPortalDefinition(Player player) {
		// make sure we're tracking the player
		if(!definingPortals.containsKey(player)) {
			return;
		}
		
		// check to make sure it's been fully defined
		if(!definingPortals.get(player).entrance.portalDefined()) {
			ColourHandler.sendMessage(player, "&4Error - your portal entrance is not fully defined!");
			return;
		}
		if(definingPortals.get(player).exit == null) {
			ColourHandler.sendMessage(player, "&4Error - your portal exit is not defined!");
			return;
		}
		
		// now reorder the min and max
		definingPortals.get(player).entrance.properlyOrder();
		
		// ok, it's defined! add it to the main portal list
		plugin.transportManager.addPortal(definingPortals.get(player));
		
		// notify them
		ColourHandler.sendMessage(player, "&2Success, your portal ("+definingPortals.get(player).name+") is now implemented!");
		
		// and stop tracking it
		definingPortals.remove(player);
	}
	
	public void cancelPortalDefinition(Player player) {
		// make sure we're tracking the player
		if(!definingPortals.containsKey(player)) {
			return;
		}
		
		// and stop tracking it
		definingPortals.remove(player);
		
		// and tell them about it!
		ColourHandler.sendMessage(player, "&2You are no longer creating a portal!");
	}
}
