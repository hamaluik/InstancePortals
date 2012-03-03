package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "plist", permission = "portal.list", usage = "", description = "lists all portals")
public class PortalList implements Command {
	private static InstancePortals plugin = null;
	public PortalList(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() > 0) {
			return false;
		}
		
		String message = "&3Currently defined portals: ";
		for(int i = 0; i < plugin.transportManager.portals.size(); i++) {
			message += "&f" + plugin.transportManager.portals.get(i).name;
			if(i < plugin.transportManager.portals.size() - 1) {
				message += "&3, ";
			}
		}
		ColourHandler.sendMessage(player, message);
		
		return true;
	}
}
