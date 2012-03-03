package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "isdelete", permission = "instance.delete", usage = "<name>", description = "deletes instance set <name>")
public class InstanceSetDelete implements Command {
	private static InstancePortals plugin = null;
	public InstanceSetDelete(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() < 1) {
			return false;
		}
		
		String name = sArgs.trim();
		
		if(!plugin.transportManager.instanceSetExists(name)) {
			ColourHandler.sendMessage(player, "&cThat instance set doesn't exist!");
			return true;
		}
		
		plugin.transportManager.removeInstanceSet(name);
		ColourHandler.sendMessage(player, "&2Instance set '" + name + "' has been removed!");
		
		return true;
	}
}
