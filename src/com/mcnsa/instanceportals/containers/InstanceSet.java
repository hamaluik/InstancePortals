package com.mcnsa.instanceportals.containers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;

public class InstanceSet {
	private InstancePortals plugin;
	private PortalRegion entrance;
	private ArrayList<Instance> instances = new ArrayList<Instance>();
	private Location exit;
	private Integer maxPlayers = new Integer(1);
	boolean ready = false;
	
	public InstanceSet(InstancePortals instance) {
		plugin = instance;
	}
	
	public void setEntrance(PortalRegion _entrance) {
		entrance = _entrance;
	}
	
	public void setExit(Location _exit) {
		exit = _exit;
	}
	
	public void addInstance(Instance instance) {
		if(!instances.contains(instance)) {
			instances.add(instance);
		}
	}
	
	public void setMaxPlayers(Integer max) {
		maxPlayers = max;
	}
	
	public Integer nextAvailableInstance() {
		Integer i = 0;
		// start looping through the instances till we find one
		// that has less players than the max
		for(i = 0; i < instances.size(); i++) {
			if(instances.get(i).getNumPlayers() < maxPlayers) {
				// we found a valid instance!
				return i;
			}
		}
		
		// no valid instances D:
		return -1;
	}
	
	public void check() {
		// update the ready flag
		ready = !(entrance == null || exit == null || instances.size() == 0);
		// and make sure the set is fully defined
		if(!ready) return;
		
		// loop through all the instances and see if a player is trying to leave
		for(int i = 0; i < instances.size(); i++) {
			// handle any departures from the instance
			instances.get(i).checkAndHandleDepartures();
		}
		
		// now loop through all online players
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++) {
			if(entrance.containsPlayer(players[i])) {
				// teleport them to the next instance
				// but first get the next available instance
				Integer nextInstance = nextAvailableInstance();
				if(nextInstance < 0) {
					// uh-oh, no more available!
					ColourHandler.sendMessage(players[i], "&cI'm sorry, but all the instances are full!");
					// and exit the loop
					break;
				}
				
				// ok, if we're here we have a valid instance
				// teleport them to it!
				instances.get(nextInstance).bringPlayer(players[i]);
			}
		}
	}
	
	public void transportToExit(Player player) {
		// make sure all our fields are valid
		if(!ready || exit == null) return;
		
		player.teleport(exit);
	}
}
