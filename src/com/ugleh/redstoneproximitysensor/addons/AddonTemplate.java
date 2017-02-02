package com.ugleh.redstoneproximitysensor.addons;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class AddonTemplate {
	public abstract boolean checkTrigger(List<String> acceptedEntities, Entity e, Location l);
	
}
