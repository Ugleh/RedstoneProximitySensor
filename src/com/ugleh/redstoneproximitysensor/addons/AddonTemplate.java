package com.ugleh.redstoneproximitysensor.addons;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.ugleh.redstoneproximitysensor.utils.RPS;

public abstract class AddonTemplate {
	public abstract boolean checkTrigger(List<String> acceptedEntities, Entity e, Location l, UUID ownerID);
	public abstract void buttonPressed(Boolean on, RPS affectedRPS);
	
}
