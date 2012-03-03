package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "pcreate", permission = "portal.create", usage = "<name>", description = "starts definition of a portal")
public class PortalCreate implements Command {
	private static InstancePortals plugin = null;
	public PortalCreate(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() < 1) {
			return false;
		}
		
		// and make sure they have no spaces
		String name = sArgs.trim().replace(" ", "_");
		
		// see if they're already defining a portal
		if(plugin.playerManager.playerDefiningPortal(player)) {
			// they are! tell them they can't do that!
			ColourHandler.sendMessage(player, "&cYou can't start a new portal definition without cancelling your old one! Type /pcancel to do this!");
			return true;
		}
		
		// ok, start the definition process!
		plugin.playerManager.startPortalDefinition(player, name);
		
		// and inform them
		ColourHandler.sendMessage(player, "&3To define the entrance portal, right click on the two corners of your portal");
		ColourHandler.sendMessage(player, "&3To define where your portal will go to, stand in that spot and type &f/pexit");
		ColourHandler.sendMessage(player, "&3When you're done, type &f/pdone&3. To cancel at any time, type &f/pcancel");
		
		return true;
	}

}
