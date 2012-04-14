package com.mcnsa.instanceportals.managers;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.containers.InstanceSet;
import com.mcnsa.instanceportals.containers.Portal;

public class TransportManager implements Runnable {
	@SuppressWarnings("unused")
	private InstancePortals plugin;
	public ArrayList<Portal> portals = new ArrayList<Portal>();
	public ArrayList<InstanceSet> instanceSets = new ArrayList<InstanceSet>();
	
	// keep track of who's in an instance so we can get them out
	public HashMap<Player, InstanceSet> playersInInstances = new HashMap<Player, InstanceSet>();
	
	// and keep track of who we're currently teleporting so we don't interfere with bukkit teleports
	public ArrayList<Player> inTransit = new ArrayList<Player>();
	
	public TransportManager(InstancePortals instance) {
		plugin = instance;
	}
	
	public boolean inTransit(Player player) {
		return inTransit.contains(player);
	}
	
	public void transport(Player player, Location location) {
		if(!inTransit(player)) {
			inTransit.add(player);
		}
		player.teleport(location);
		inTransit.remove(player);
	}
	
	public boolean portalExists(String name) {
		for(int i = 0; i < portals.size(); i++) {
			if(portals.get(i).name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addPortal(Portal portal) {
		if(!portals.contains(portal)) {
			portals.add(portal);
		}
	}
	
	public void removePortal(String name) {
		for(int i = 0; i < portals.size(); i++) {
			if(portals.get(i).name.equals(name)) {
				portals.remove(i);
				return;
			}
		}
	}
	
	public boolean playerInInstance(Player player) {
		return playersInInstances.containsKey(player);
	}
	
	public InstanceSet playerInstanceSet(Player player) {
		if(!playersInInstances.containsKey(player)) {
			return null;
		}
		return playersInInstances.get(player);
	}
	
	public void playerEnteredInstance(Player player, InstanceSet set) {
		if(!playersInInstances.containsKey(player)) {
			playersInInstances.put(player, set);
		}
	}
	
	public void playerLeftInstance(Player player) {
		if(playersInInstances.containsKey(player)) {
			playersInInstances.remove(player);
		}
	}
	
	public boolean instanceSetExists(String name) {
		for(int i = 0; i < instanceSets.size(); i++) {
			if(instanceSets.get(i).name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addInstanceSet(InstanceSet set) {
		if(!instanceSets.contains(set)) {
			instanceSets.add(set);
		}
	}
	
	public void removeInstanceSet(String name) {
		for(int i = 0; i < instanceSets.size(); i++) {
			if(instanceSets.get(i).name.equals(name)) {
				instanceSets.remove(i);
				return;
			}
		}
	}
	
	public void reset() {
		for(int i = 0; i < instanceSets.size(); i++) {
			instanceSets.get(i).reset();
		}
	}

	@Override
	public void run() {
		// just loop through all the portals and check for players
		for(int i = 0; i < portals.size(); i++) {
			portals.get(i).check();
		}
		
		// and loop through all the instance sets and check
		for(int i = 0; i < instanceSets.size(); i++) {
			instanceSets.get(i).check();
		}
	}
}
