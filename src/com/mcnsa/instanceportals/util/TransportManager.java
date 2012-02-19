package com.mcnsa.instanceportals.util;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;

public class TransportManager {
	private HashMap<String, TransportVector> inTransport = new HashMap<String, TransportVector>();
	private InstancePortals plugin = null;
	
	public TransportManager(InstancePortals instance) {
		plugin = instance;
	}
	
	public boolean queueTransport(Player player, Location end) {
		// only queue people who are not already being transported
		if(inTransport.containsKey(player.getName())) {
			return false;
		}
		
		// now add them to the list!
		inTransport.put(player.getName(), new TransportVector(player.getLocation(), end));
		
		return true;
	}
	
	public boolean inTransport(Player player) {
		return inTransport.containsKey(player.getName());
	}
	
	public void handleTransports() {
		// loop through everyone in queue
		for(String player: inTransport.keySet()) {
			plugin.getServer().getPlayer(player).teleport(inTransport.get(player).end);
		}
		
		// now clear it out!
		inTransport.clear();
	}

	@SuppressWarnings("unused")
	private class TransportVector {
		Location start = null;
		Location end = null;
		
		TransportVector(Location _start, Location _end) {
			start = _start;
			end = _end;
		}
	}
}
