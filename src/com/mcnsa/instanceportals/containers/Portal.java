package com.mcnsa.instanceportals.containers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;

public class Portal {
	private InstancePortals plugin;
	public String name = new String("");
	public PortalRegion entrance;
	public Location exit;
	
	public Portal(InstancePortals instance, String _name) {
		plugin = instance;
		name = _name;
	}
	
	public void check() {
		//plugin.debug("checking portal: " + name);
		// and make sure the set is fully defined
		if(entrance == null || exit == null) {
			//plugin.debug("\tportal was not fully defined!");
			return;
		}
		
		// now loop through all online players
		Player[] players = plugin.getServer().getOnlinePlayers();
		for(int i = 0; i < players.length; i++) {
			if(entrance.containsPlayer(players[i])) {
				// teleport them to the exit
				//plugin.debug("\ttransporting player to exit...");
				//players[i].teleport(exit);
				plugin.transportManager.transport(players[i], exit);
				players[i].setFallDistance(0f);
			}
		}
	}
}
