package com.mcnsa.instanceportals.containers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.mcnsa.instanceportals.InstancePortals;

public class PortalRegion  {
	@SuppressWarnings("unused")
	private InstancePortals plugin;
	public String worldName = new String("world");
	public String rank = null;
	public int item = -1;
	public String itemStr = "-1";
	public int amt = -1;
	public String amtStr = "-1";
	private boolean nextIsMax = false;
	public Location min, max;
	
	public PortalRegion(InstancePortals instance, Player player, String _rank, String _item, String _amt) {
		plugin = instance;
		worldName = player.getWorld().getName();
		rank = _rank;
		try{
			item = Integer.parseInt(_item);
			itemStr = ""+_item;
		}catch(Exception e){
		}
		try{
			amt = Integer.parseInt(_amt);
			amtStr = ""+_amt;
		}catch(Exception e){
		}
	}
	
	public PortalRegion(InstancePortals instance, String _worldName, Location _min, Location _max, String _rank, String _item, String _amt) {
		plugin = instance;
		worldName = _worldName;
		min = _min;
		max = _max;
		rank = _rank;
		try{
			item = Integer.parseInt(_item);
			itemStr = ""+_item;
		}catch(Exception e){
		}
		try{
			amt = Integer.parseInt(_amt);
			amtStr = ""+_amt;
		}catch(Exception e){
		}
	}
	
	public boolean nextIsMax() {
		return nextIsMax;
	}
	
	public boolean portalDefined() {
		return !(min == null || max == null);
	}
	
	public void updateMin(Location _min) {
		min = _min;
		nextIsMax = true;
	}
	
	public void updateMax(Location _max) {
		max = _max;
		nextIsMax = false;
	}
	
	public void properlyOrder() {
		// make sure they're defined
		if(min == null || max == null) {
			return;
		}
		
		// now reorder!
		double scratch = 0;
		if(min.getBlockX() > max.getBlockX()) {
			scratch = min.getX();
			min.setX(max.getX());
			max.setX(scratch);
			//plugin.debug("Reordered X");
		}
		if(min.getBlockY() > max.getBlockY()) {
			scratch = min.getY();
			min.setY(max.getY());
			max.setY(scratch);
			//plugin.debug("Reordered Y");
		}
		if(min.getBlockZ() > max.getBlockZ()) {
			scratch = min.getZ();
			min.setZ(max.getZ());
			max.setZ(scratch);
			//plugin.debug("Reordered Z");
		}
	}
	
	public boolean containsPlayer(Player player) {
		// make sure they're in the correct world and that the portal is defined
        if (!player.getWorld().getName().equals(worldName) || min == null || max == null) {
        	//plugin.debug("\t\tplayer was in wrong world or region was not defined");
            return false;
        }
        
        // get the players location
        Location l = player.getLocation();
        /*plugin.debug("\t\tChecking "+player.getName()+" at: " + l.toString());
        plugin.debug("\t\tagainst min: "+min.getBlockX()+","+min.getBlockY()+","+min.getBlockZ());
        plugin.debug("\t\tagainst max: "+max.getBlockX()+","+max.getBlockY()+","+max.getBlockZ());*/
        
        // now check in each direction
        if (!(l.getBlockX() >= min.getBlockX() && l.getBlockX() <= max.getBlockX())) {
        	//plugin.debug("\tFailing for not in X");
            return false;
        }
        if (!(l.getBlockZ() >= min.getBlockZ() && l.getBlockZ() <= max.getBlockZ())) {
        	//plugin.debug("\tFailing for not in Z");
            return false;
        }
        if (!(l.getBlockY() >= min.getBlockY() && l.getBlockY() <= max.getBlockY())) {
        	//plugin.debug("\tFailing for not in Y");
        	//plugin.debug("\t\tl.getBlockY() >= min.getBlockY(): " + (l.getBlockY() >= min.getBlockY()));
        	//plugin.debug("\t\tl.getBlockY() <= max.getBlockY(): " + (l.getBlockY() <= max.getBlockY()));
            return false;
        }
    	//plugin.debug("\tPassing for being in");
        return true;
	}
}
