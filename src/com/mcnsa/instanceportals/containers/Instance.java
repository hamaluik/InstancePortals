package com.mcnsa.instanceportals.containers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Instance {
	private InstanceSet parent;
	private Location arrival;
	private PortalRegion departure;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Instance(InstanceSet _parent, Location _arrival, PortalRegion _departure) {
		parent = _parent;
		arrival = _arrival;
		departure = _departure;
	}
	
	public Location getArrival() {
		return arrival;
	}
	
	public void setArrival(Location loc) {
		arrival = loc;
	}
	
	public void setDeparture(PortalRegion _departure) {
		departure = _departure;
	}
	
	public Integer getNumPlayers() {
		return players.size();
	}
	
	public boolean playerDeparting(Player player) {
		if(departure == null) {
			return false;
		}
		return departure.containsPlayer(player);
	}
	
	public void bringPlayer(Player player) {
		if(departure != null && !players.contains(player)) {
			player.teleport(arrival);
			players.add(player);
		}
	}
	
	public void checkAndHandleDepartures() {
		// now loop over all the players we have here
		for(int i = 0; i < players.size(); i++) {
			if(playerDeparting(players.get(i))) {
				// teleport them out
				parent.transportToExit(players.get(i));
				// and stop tracking them!
				players.remove(i--);
			}
		}
	}
}
