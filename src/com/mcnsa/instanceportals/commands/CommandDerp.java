package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;

@CommandInfo(alias = "derp")
public class CommandDerp implements Command {
	private static InstancePortals plugin = null;
	public CommandDerp(InstancePortals instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// get their selection
		LocalWorld world = plugin.getWEAPI().getSession(player).getSelectionWorld();
		Region region = null;
		try {
			region = plugin.getWEAPI().getSession(player).getSelection(world);
		} catch (IncompleteRegionException e) {
			// they don't have a complete selection!
			ColourHandler.sendMessage(player, "&cError - your selection is incomplete!");
			return true;
		}
		
		// now get region info
		Vector max = region.getMaximumPoint();
		Vector min = region.getMinimumPoint();
		
		ColourHandler.sendMessage(player, "Your region goes from: (" + min.getX() + ", " + min.getY() + ", " + min.getZ() +") to: (" + max.getX() + ", " + max.getY() + ", " + max.getZ() +")");
		
		// and we handled it!
		return true;
	}
}
