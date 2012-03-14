package com.mcnsa.instanceportals.commands;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;

@CommandInfo(alias = "isreset", permission = "instance.reset", usage = "", description = "clears all players out of all instances")
public class InstanceSetReset implements Command {
	private static InstancePortals plugin = null;
	public InstanceSetReset(InstancePortals instance) {
		plugin = instance;
	}
	
	@Override
	public Boolean handle(Player player, String sArgs) {
		// make sure they have valid args
		if(sArgs.trim().length() > 0) {
			return false;
		}
		
		plugin.transportManager.reset();
		ColourHandler.sendMessage(player, "&6All instances have been reset!");
		
		return true;
	}
}
