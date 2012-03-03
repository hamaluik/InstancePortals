package com.mcnsa.instanceportals.managers;

import java.util.ArrayList;

import com.mcnsa.instanceportals.InstancePortals;
import com.mcnsa.instanceportals.containers.Portal;

public class TransportManager implements Runnable {
	@SuppressWarnings("unused")
	private InstancePortals plugin;
	public ArrayList<Portal> portals = new ArrayList<Portal>();
	
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

	@Override
	public void run() {
		// just loop through all the portals and check for players
		for(int i = 0; i < portals.size(); i++) {
			portals.get(i).check();
		}
	}
}
