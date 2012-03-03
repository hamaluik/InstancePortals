package com.mcnsa.instanceportals.managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.containers.Portal;
import com.mcnsa.instanceportals.containers.PortalRegion;

@SuppressWarnings("unchecked")
public class PersistanceManager {
	private InstancePortals plugin = null;
	
	public PersistanceManager(InstancePortals instance) {
		plugin = instance;
	}
	
	public void writePersistance() {
		FileWriter outFile;
		try {
			// make sure the data folder exists
			File dataFolder = new File(plugin.getDataFolder().toString());
			if(!dataFolder.exists()) {
				dataFolder.mkdirs();
			}
			
			outFile = new FileWriter(plugin.getDataFolder().toString() + "/persist.json");
			PrintWriter out = new PrintWriter(outFile);
			
			JSONObject obj = new JSONObject();
			
			// start storing!
			// store portals first
			JSONObject portalsObj = new JSONObject();
			for(int i = 0; i < plugin.transportManager.portals.size(); i++) {
				JSONObject portalObj = new JSONObject();
				
				// the entrance coords
				JSONArray entranceMin = new JSONArray();
				entranceMin.add(plugin.transportManager.portals.get(i).entrance.min.getBlockX());
				entranceMin.add(plugin.transportManager.portals.get(i).entrance.min.getBlockY());
				entranceMin.add(plugin.transportManager.portals.get(i).entrance.min.getBlockZ());
				JSONArray entranceMax = new JSONArray();
				entranceMax.add(plugin.transportManager.portals.get(i).entrance.max.getBlockX());
				entranceMax.add(plugin.transportManager.portals.get(i).entrance.max.getBlockY());
				entranceMax.add(plugin.transportManager.portals.get(i).entrance.max.getBlockZ());
				
				// and now the exit
				JSONArray exit = new JSONArray();
				exit.add(plugin.transportManager.portals.get(i).exit.getX());
				exit.add(plugin.transportManager.portals.get(i).exit.getY());
				exit.add(plugin.transportManager.portals.get(i).exit.getZ());
				exit.add(plugin.transportManager.portals.get(i).exit.getYaw());
				exit.add(plugin.transportManager.portals.get(i).exit.getPitch());

				portalObj.put("world", plugin.transportManager.portals.get(i).entrance.worldName);
				portalObj.put("min", entranceMin);
				portalObj.put("max", entranceMax);
				portalObj.put("exit", exit);
				
				portalsObj.put(plugin.transportManager.portals.get(i).name, portalObj);
			}
			
			// and add everything to the head node
			obj.put("portals", portalsObj);
			
			// and save it!
			out.print(obj);			
			out.close();
		} catch (IOException e) {
			plugin.error("failed to write persistance: " + e.getMessage());
		}
	}
	
	public void readPersistance() {
		try {
			// load the file
			String lineSep = System.getProperty("line.separator");
			FileInputStream fin = new FileInputStream(plugin.getDataFolder().toString() + "/persist.json");
			BufferedReader input = new BufferedReader(new InputStreamReader(fin));
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while((nextLine = input.readLine()) != null) {
				sb.append(nextLine);
				sb.append(lineSep);
			}
			
			// and parse it!
			Map<String, Object> obj = (HashMap<String, Object>)JSONValue.parse(sb.toString());
			
			// and grab the objects!
			if(obj != null) {
				// grab the portals
				HashMap<String, HashMap<String, Object>> portals = (HashMap<String, HashMap<String, Object>>)obj.get("portals");
				// iterate over all the portals
				for(String portalName: portals.keySet()) {
					try {
						Portal portal = new Portal(plugin, portalName);
	
						String worldName = (String)portals.get(portalName).get("world");
						ArrayList<Long> minList = (ArrayList<Long>)portals.get(portalName).get("min");
						ArrayList<Long> maxList = (ArrayList<Long>)portals.get(portalName).get("max");
						ArrayList<Double> exitList = (ArrayList<Double>)portals.get(portalName).get("exit");
	
						Location min = new Location(plugin.getServer().getWorld(worldName), minList.get(0).doubleValue(), minList.get(1).doubleValue(), minList.get(2).doubleValue());
						Location max = new Location(plugin.getServer().getWorld(worldName), maxList.get(0).doubleValue(), maxList.get(1).doubleValue(), maxList.get(2).doubleValue());
						Location exit = new Location(plugin.getServer().getWorld(worldName), exitList.get(0), exitList.get(1), exitList.get(2), exitList.get(3).floatValue(), (float)exitList.get(4).floatValue());
						
						// ok, create the portal region
						PortalRegion region = new PortalRegion(plugin, worldName, min, max);
						
						// now set things in the portal
						portal.entrance = region;
						portal.exit = exit;
						
						// and track the portal!
						plugin.transportManager.addPortal(portal);
					}
					catch(Exception e) {
						plugin.error("failed to read portal: " + e.getMessage());
					}
				}
			}
		}
		catch(Exception e) {
			plugin.error("failed to read persistance: " + e.getMessage());
		}
	}
}
