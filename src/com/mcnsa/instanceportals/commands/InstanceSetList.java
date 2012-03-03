package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "islist", permission = "instance.list", usage = "", description = "lists all instance sets")
public class InstanceSetList implements Command {
	private static InstancePortals plugin = null;
	public InstanceSetList(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() > 0) {
			return false;
		}
		
		String message = "&3Currently defined instance sets: ";
		for(int i = 0; i < plugin.transportManager.instanceSets.size(); i++) {
			message += "&f" + plugin.transportManager.instanceSets.get(i).name;
			if(i < plugin.transportManager.instanceSets.size() - 1) {
				message += "&3, ";
			}
		}
		ColourHandler.sendMessage(player, message);
		
		return true;
	}
}
