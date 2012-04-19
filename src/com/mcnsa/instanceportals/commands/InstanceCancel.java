package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "icancel", permission = "instance.create", usage = "", description = "writes all instance sets in memory to file")
public class InstanceCancel implements Command {
	private static InstancePortals plugin = null;
	public InstanceCancel(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() > 0) {
			return false;
		}
		
		// see if they're already defining a portal
		if(!plugin.playerManager.playerDefiningInstance(player)) {
			// they are! tell them they can't do that!
			ColourHandler.sendMessage(player, "&cBut you aren't defining an instance!");
			return true;
		}
		
		// ok, cancel the definition process!
		plugin.playerManager.cancelInstanceDefinition(player);
		
		return true;
	}
}
