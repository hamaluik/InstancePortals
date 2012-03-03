package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "idone", permission = "instance.create", usage = "", description = "finishes definition of your instance")
public class InstanceDone implements Command {
	private static InstancePortals plugin = null;
	public InstanceDone(InstancePortals instance) {
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
		
		// ok, finish the definition process!
		plugin.playerManager.endInstanceDefinition(player);
		
		return true;
	}
}