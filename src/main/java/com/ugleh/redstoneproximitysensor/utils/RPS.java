package com.ugleh.redstoneproximitysensor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class RPS implements Runnable{
	private UUID uniqueID;
	private Location location;
	private UUID ownerID;
	private BukkitTask toCancel;
	private int range = 5;
	private boolean inverted = false;
	private boolean ownerOnlyTrigger = false;
	private boolean ownerOnlyEdit = true;
	private boolean triggered = false;
	private List<String> acceptedEntities = new ArrayList<String>();
    private static RedstoneProximitySensor plugin = RedstoneProximitySensor.getInstance();

	public RPS(Location placedLocation, UUID placedBy, UUID id, boolean inConfig) {
		this.location = placedLocation;
		this.ownerID = placedBy;
		this.uniqueID = id;
		if(!inConfig)
		{
			//Not yet made
			this.ownerOnlyTrigger = plugin.getgConfig().isDefaultownerOnlyTrigger();
			this.inverted = plugin.getgConfig().isDefaultInverted();
			this.range = plugin.getgConfig().getDefaultRange();
			//Default Settings
			acceptedEntities.add("PLAYER");
		}

	}

	@Override
	public void run() {
		triggered = false;
		List<Entity> entityList = location.getWorld().getEntities();
		for(Player p : location.getWorld().getPlayers())
		{
			entityList.add(p);
		}
		if(this.ownerOnlyTrigger)
		{
			entityList.clear();
			if(Bukkit.getPlayer(this.ownerID) != null && Bukkit.getPlayer(this.ownerID).isOnline())
			{
				entityList.add(Bukkit.getPlayer(this.ownerID));
			}
		}
		for(Entity ent : entityList)
		{
			if((ent.getLocation().distance(location) <= this.range) &&
			((this.acceptedEntities.contains("HOSTILE_ENTITY") && plugin.getgConfig().hostileMobs.contains(ent.getType().name())) ||
					(this.acceptedEntities.contains("PEACEFUL_ENTITY") && plugin.getgConfig().peacefulMobs.contains(ent.getType().name())) ||
					(this.acceptedEntities.contains("PLAYER") && ent.getType().name().equals("PLAYER")) ||
					(this.acceptedEntities.contains("DROPPED_ITEM") && ent.getType().name().equals("DROPPED_ITEM"))
					))
			{
				//Check if entity is player and that player has invisible, if so continue on.
				if(ent.getType().equals(EntityType.PLAYER))
				{
					Player p = (Player) ent;
					if(p.hasPermission("rps.invisible"))
					{
						triggered = false;
						continue;
					}
				}

				//Check if entity is invisible
				if(!this.acceptedEntities.contains("INVISIBLE_ENTITY"))
				{
					if(ent instanceof LivingEntity)
					{
						LivingEntity le = (LivingEntity)ent;
						boolean isInvisible = false;
						 for (PotionEffect effect : le.getActivePotionEffects())
						 {
							 if(effect.getType().equals(PotionEffectType.INVISIBILITY))
							 {
								 isInvisible = true;
							 }
						 }
						 if(isInvisible) continue;
					}
				}
				triggered = true;
				break;
			}
		}

		if((location.getWorld().getBlockAt(location).getType().equals(Material.REDSTONE_TORCH_OFF)) || (location.getWorld().getBlockAt(location).getType().equals(Material.REDSTONE_TORCH_ON)))
		{
			if(triggered)
			{
				location.getWorld().getBlockAt(location).setType(getSensorMaterial(!inverted));

			}else{
				location.getWorld().getBlockAt(location).setType(getSensorMaterial(inverted));
			}
		}else{
			plugin.getSensorConfig().removeSensor(location);
		}



	}

	private Material getSensorMaterial(boolean inv)
	{
		if(!inv)
		{
			return Material.REDSTONE_TORCH_OFF;
		}else
		{
			return Material.REDSTONE_TORCH_ON;
		}

	}

	public void setCancelTask(BukkitTask task) {
		this.toCancel = task;
	}

	public String getUniqueID() {
		return this.uniqueID.toString();
	}


	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public UUID getOwner() {
		return ownerID;
	}

	public void setOwner(UUID owner) {
		this.ownerID = owner;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public List<String> getAcceptedEntities() {
		return acceptedEntities;
	}

	public void setAcceptedEntities(List<String> acceptedEntities) {
		this.acceptedEntities = acceptedEntities;
	}

	public boolean isownerOnlyTrigger() {
		return ownerOnlyTrigger;
	}

	public void setownerOnlyTrigger(boolean ownerOnlyTrigger) {
		this.ownerOnlyTrigger = ownerOnlyTrigger;
	}

	public void cancelTask() {
		this.toCancel.cancel();

	}

	public void setData(boolean oo, boolean inv, int ran, List<String> acpent, boolean ownerEdit) {
		this.setAcceptedEntities(acpent);
		this.setownerOnlyTrigger(oo);
		this.setInverted(inv);
		this.setRange(ran);
		this.setOwnerOnlyEdit(ownerEdit);

	}

	public boolean isownerOnlyEdit() {
		return this.ownerOnlyEdit;
	}

	public boolean setOwnerOnlyEdit(boolean ownerOnlyEdit) {
		return this.ownerOnlyEdit = ownerOnlyEdit;
	}



}
