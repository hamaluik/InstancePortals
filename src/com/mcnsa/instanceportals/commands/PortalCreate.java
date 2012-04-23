package com.mcnsa.instanceportals.commands;

import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.util.ColourHandler;
import com.mcnsa.instanceportals.util.Command;
import com.mcnsa.instanceportals.util.CommandInfo;
import com.mcnsa.instanceportals.util.SearchItem;

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
		//Now parse the args
		//[0]name [1]rank [2]item/po8 [3]amount
		String[] parts = sArgs.trim().split("\\s");
		
		String name = parts[0];
		String rank = null;
		String item = null;
		String amt = null;
		
		if(parts.length >= 5) {
			return false;
		}
		//set rank
		if(parts.length >=2){
			rank = parts[1];
			if (plugin.permissions.getGroup(rank) == null){
				return false;
			}
		}
		//set amount
		if(parts.length >=4){
			amt = parts[3];
			//Check it's an int, but don't pass that because I'm(fusty) lazy
			try {
				Integer.parseInt(amt);
			}catch (NumberFormatException e){
				return false;
			}
		}
			
				
		//set item
		if(parts.length >=3){
			item = parts[2];
			
			String[] result = SearchItem.searchItem(item).split(": ");
			if (result[0].equalsIgnoreCase("Error")){
				ColourHandler.sendMessage(player, "&cError: "+result[1]);
				return false;
			}else if(result[0].equalsIgnoreCase("Success")){
				item = result[1];
			}
			//If amount was left null, make it one.
			if(amt == null)
				amt = "1";
		}

		
		// see if they're already defining a portal
		if(plugin.playerManager.playerDefiningPortal(player)) {
			// they are! tell them they can't do that!
			ColourHandler.sendMessage(player, "&cYou can't start a new portal definition without cancelling your old one! Type /pcancel to do this!");
			return true;
		}
		
		// ok, start the definition process!
		plugin.playerManager.startPortalDefinition(player, name, rank, item, amt);
		
		return true;
	}
}
