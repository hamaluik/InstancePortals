package com.mcnsa.instanceportals.containers;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Instance {
	private InstanceSet parent;
	public Location arrival;
	public PortalRegion departure;
	public PortalRegion container;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public Instance(InstanceSet _parent) {
		parent = _parent;
	}
	
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
	
	public void setContainer(PortalRegion _container) {
		container = _container;
	}
	
	public Integer getNumPlayers() {
		return players.size();
	}
	
	public boolean hasPlayer(Player player) {
		return players.contains(player);
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
			player.setFallDistance(0f);
			players.add(player);
		}
	}
	
	public void checkAndHandleDepartures(boolean reset) {
		// now loop over all the players we have here
		for(int i = 0; i < players.size(); i++) {
			if(reset || playerDeparting(players.get(i))) {
				// teleport them out
				parent.transportToExit(players.get(i));
				// and stop tracking them!
				players.remove(i--);
			}
		}
		
		if(players.size() < 1) {
			// reset the region!
			resetRegion();
		}
	}
	
	public void bootPlayerFromInstance(Player player, boolean transport) {
		if(players.contains(player)) {
			// send them to the entrance
			if(transport) {
				parent.transportToEntrance(player);
			}
			players.remove(player);
		}
	}
	
	private void resetRegion() {
		// reset levers
		boolean resetPulse = false;
		for(int x = container.min.getBlockX(); x <= container.max.getBlockX(); x++) {
			for(int y = container.min.getBlockY(); y <= container.max.getBlockY(); y++) {
				for(int z = container.min.getBlockZ(); z <= container.max.getBlockX(); z++) {
					if(parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y, z).getType().equals(Material.LEVER)) {
						byte data = parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y, z).getData();
						parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y, z).setData((byte)(data & 247), true);
					}
					else if(parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y, z).getType().equals(Material.CLAY)) {
						parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y + 1, z).setType(Material.REDSTONE_TORCH_ON);
						resetPulse = true;
					}
				}
			}
		}
		
		// schedule a reset
		if(resetPulse) {
			parent.plugin.getServer().getScheduler().scheduleSyncDelayedTask(parent.plugin, new Runnable() {
				@Override
				public void run() {
					for(int x = container.min.getBlockX(); x <= container.max.getBlockX(); x++) {
						for(int y = container.min.getBlockY(); y <= container.max.getBlockY(); y++) {
							for(int z = container.min.getBlockZ(); z <= container.max.getBlockX(); z++) {
								if(parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y, z).getType().equals(Material.CLAY)) {
									parent.plugin.getServer().getWorld(container.worldName).getBlockAt(x, y + 1, z).setType(Material.REDSTONE_TORCH_OFF);
								}
							}
						}
					}
				}
			}, 10l);
		}
	}
	
	public void reset() {
		checkAndHandleDepartures(true);
	}
}
