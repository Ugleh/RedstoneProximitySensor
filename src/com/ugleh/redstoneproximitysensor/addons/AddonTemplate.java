package com.ugleh.redstoneproximitysensor.addons;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class AddonTemplate {
	public abstract boolean checkTrigger(List<String> acceptedEntities, Entity e, Location l, UUID ownerID);
	
}
