package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "iscreate", permission = "instance.create", usage = "<name> <players-per-instance>", description = "starts definition of an instance set")
public class InstanceSetCreate implements Command {
	private static InstancePortals plugin = null;
	public InstanceSetCreate(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() < 1) {
			return false;
		}
		
		// now parse the args
		String[] parts = sArgs.trim().split("\\s");
		
		if(parts.length != 2) {
			return false;
		}
		
		// and make sure they have no spaces
		String name = parts[0];
		Integer max = 1;
		try {
			max = Integer.parseInt(parts[1]);
		}
		catch(Exception e) {
			return false;
		}
		
		// see if they're already defining a portal
		if(plugin.playerManager.playerDefiningInstanceSet(player)) {
			// they are! tell them they can't do that!
			ColourHandler.sendMessage(player, "&cYou can't start a new instance set definition without cancelling your old one! Type /iscancel to do this!");
			return true;
		}
		
		// ok, start the definition process!
		plugin.playerManager.startInstanceSetDefinition(player, name, max);
		
		return true;
	}
}
