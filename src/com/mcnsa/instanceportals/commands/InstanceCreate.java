package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "icreate", permission = "instance.create", usage = "", description = "starts definition of an instance")
public class InstanceCreate implements Command {
	private static InstancePortals plugin = null;
	public InstanceCreate(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() > 0) {
			return false;
		}
		
		// see if they're already defining an instance
		if(plugin.playerManager.playerDefiningInstance(player)) {
			// they are! tell them they can't do that!
			ColourHandler.sendMessage(player, "&cYou can't start a new instance definition without cancelling your old one! Type /icancel to do this!");
			return true;
		}
		
		// ok, start the definition process!
		plugin.playerManager.startInstanceDefinintion(player);
		
		return true;
	}
}

