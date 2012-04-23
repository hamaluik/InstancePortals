package com.mcnsa.instanceportals.containers;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.RemoveItems;

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
				Boolean perms = false;
				Boolean items = true;
				//Check Portal Requirements  (rank, item(s), po8FUCKPO8RIGHTNOW)
				//Check Rank first
				if(plugin.permissions.getUser(players[i]).getRank("default") <= plugin.permissions.getGroup(entrance.rank).getRank()){
					perms = true;
				}else {
					ColourHandler.sendMessage(players[i], "&cYou must be &a"+entrance.rank+" &cor higher to use this portal");
					perms = false;
				}
				//Check Items
				if(entrance.item != -10 && perms == true){
					PlayerInventory inv = players[i].getInventory();
					ItemStack stack = new ItemStack(entrance.item, entrance.amt);
					Material material = stack.getType();
					if(inv.contains(material, entrance.amt)){//Remove items if present
						RemoveItems.removeItem(inv, material.getId(), entrance.amt);
						ColourHandler.sendMessage(players[i], "&6"+entrance.amt+" "+new ItemStack(entrance.item).getType().toString()+"&a deducted from your inventory.");
						items = true;
					}else {
						ColourHandler.sendMessage(players[i], "&cYou need &6"+entrance.amt+" "+new ItemStack(entrance.item).getType().toString());
						items = false;
					}
				}
				
				if (perms == true && items == true) {
					// teleport them to the exit
					//plugin.debug("\transporting player to exit...");
					//players[i].teleport(exit);
					plugin.transportManager.transport(players[i], exit);
					players[i].setFallDistance(0f);
				}
				
			}
		}
	}
}
