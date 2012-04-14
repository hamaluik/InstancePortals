package com.mcnsa.instanceportals.containers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;

public class InstanceSet {
	public InstancePortals plugin;
	public String name;
	public String world;
	public PortalRegion entrance;
	public ArrayList<Instance> instances = new ArrayList<Instance>();
	public Location bootEntrance;
	public Location exit;
	public Integer maxPlayers = new Integer(1);
	
	public InstanceSet(InstancePortals instance, String _name) {
		plugin = instance;
		name = _name;
		// TODO: track world names on instance sets
		world = "world";
	}
	
	public InstanceSet(InstancePortals instance, String _name, Integer _max) {
		plugin = instance;
		name = _name;
		maxPlayers = _max;
		// TODO: track world names on instance sets
		world = "world";
	}
	
	public void setMax(Integer _max) {
		maxPlayers = _max;
	}
	
	public void setEntrance(PortalRegion _entrance) {
		entrance = _entrance;
	}
	
	public void setBootEntrance(Location _bootEntrance) {
		bootEntrance = _bootEntrance;
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
		// and make sure the set is fully defined
		if(entrance == null || exit == null || instances.size() == 0) return;
		
		// loop through all the instances and see if a player is trying to leave
		for(int i = 0; i < instances.size(); i++) {
			// handle any departures from the instance
			instances.get(i).checkAndHandleDepartures(false);
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
				plugin.transportManager.playerEnteredInstance(players[i], this);
			}
		}
	}
	
	public void transportToExit(Player player) {
		// make sure all our fields are valid
		if(exit == null) return;
		
		//player.teleport(exit);
		plugin.transportManager.transport(player, exit);
		player.setFallDistance(0f);
		
		plugin.transportManager.playerLeftInstance(player);
	}
	
	public void bootPlayer(Player player) {
		// figure out which instance they're in
		for(int i = 0; i < instances.size(); i++) {
			if(instances.get(i).hasPlayer(player)) {
				instances.get(i).bootPlayerFromInstance(player, true, true);
				return;
			}
		}
	}
	
	public void delistPlayer(Player player) {
		// figure out which intance they're in
		for(int i = 0; i < instances.size(); i++) {
			if(instances.get(i).hasPlayer(player)) {
				instances.get(i).bootPlayerFromInstance(player, false, true);
				instances.get(i).checkReset();
				return;
			}
		}
	}
	
	public void transportToEntrance(Player player) {
		/*double meanX = (entrance.min.getX() + entrance.max.getX()) / 2d;
		double meanY = (entrance.min.getY() + entrance.max.getY()) / 2d;
		double meanZ = (entrance.min.getZ() + entrance.max.getZ()) / 2d;
		
		player.teleport(new Location(player.getWorld(), meanX, meanY, meanZ));*/
		//player.teleport(bootEntrance);
		plugin.transportManager.transport(player, bootEntrance);
		player.setFallDistance(0f);
		
		plugin.transportManager.playerLeftInstance(player);
	}
	
	public void reset() {
		for(int i = 0; i < instances.size(); i++) {
			instances.get(i).reset();
		}
	}
}
