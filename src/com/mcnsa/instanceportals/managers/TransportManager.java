package com.mcnsa.instanceportals.managers;

import java.util.ArrayList;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.containers.InstanceSet;
import com.mcnsa.instanceportals.containers.Portal;

public class TransportManager implements Runnable {
	@SuppressWarnings("unused")
	private InstancePortals plugin;
	public ArrayList<Portal> portals = new ArrayList<Portal>();
	public ArrayList<InstanceSet> instanceSets = new ArrayList<InstanceSet>();
	
	public TransportManager(InstancePortals instance) {
		plugin = instance;
	}
	
	public boolean portalExists(String name) {
		for(int i = 0; i < portals.size(); i++) {
			if(portals.get(i).name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addPortal(Portal portal) {
		if(!portals.contains(portal)) {
			portals.add(portal);
		}
	}
	
	public void removePortal(String name) {
		for(int i = 0; i < portals.size(); i++) {
			if(portals.get(i).name.equals(name)) {
				portals.remove(i);
				return;
			}
		}
	}
	
	public boolean instanceSetExists(String name) {
		for(int i = 0; i < instanceSets.size(); i++) {
			if(instanceSets.get(i).name.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addInstanceSet(InstanceSet set) {
		if(!instanceSets.contains(set)) {
			instanceSets.add(set);
		}
	}
	
	public void removeInstanceSet(String name) {
		for(int i = 0; i < instanceSets.size(); i++) {
			if(instanceSets.get(i).name.equals(name)) {
				instanceSets.remove(i);
				return;
			}
		}
	}

	@Override
	public void run() {
		// just loop through all the portals and check for players
		for(int i = 0; i < portals.size(); i++) {
			portals.get(i).check();
		}
		
		// and loop through all the instance sets and check
		for(int i = 0; i < instanceSets.size(); i++) {
			instanceSets.get(i).check();
		}
	}
}
