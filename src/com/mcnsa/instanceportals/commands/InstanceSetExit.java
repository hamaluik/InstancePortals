package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "isexit", permission = "instance.create", usage = "", description = "defines the exit of your instance set")
public class InstanceSetExit implements Command {
	private static InstancePortals plugin = null;
	public InstanceSetExit(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() > 0) {
			return false;
		}
		
		// see if they're already defining a portal
		if(!plugin.playerManager.playerDefiningInstanceSet(player)) {
			// they are! tell them they can't do that!
			ColourHandler.sendMessage(player, "&cBut you aren't defining an instance set!");
			return true;
		}
		
		// ok, start the definition process!
		plugin.playerManager.defineInstanceSetExit(player);
		
		return true;
	}
}
