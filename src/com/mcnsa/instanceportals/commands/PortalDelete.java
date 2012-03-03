package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "pdelete", permission = "portal.delete", usage = "<name>", description = "deletes portal <name>")
public class PortalDelete implements Command {
	private static InstancePortals plugin = null;
	public PortalDelete(InstancePortals instance) {
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
		
		// ok, start the definition process!
		if(!plugin.transportManager.portalExists(name)) {
			ColourHandler.sendMessage(player, "&cThat portal doesn't exist!");
			return true;
		}
		
		plugin.transportManager.removePortal(name);
		ColourHandler.sendMessage(player, "&2Portal '" + name + "' has been removed!");
		
		return true;
	}
}