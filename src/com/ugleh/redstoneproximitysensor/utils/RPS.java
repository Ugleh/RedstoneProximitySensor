package com.ugleh.redstoneproximitysensor.utils;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.configs.GeneralConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RPS implements Runnable {
	private UUID uniqueID;
	private RPSLocation location;
	private UUID ownerID;
	private BukkitTask toCancel;
	private int range = 5;
	private boolean inverted = false;
	private boolean ownerOnlyEdit = true;
	private boolean triggered = false;
	public RedstoneProximitySensor plugin;
	private List<String> acceptedEntities = new ArrayList<String>();

	private Random random;

	public RPS(RedstoneProximitySensor plugin, RPSLocation location2, UUID placedBy, UUID id, boolean inConfig) {
		this.plugin = plugin;
		this.location = location2;
		this.ownerID = placedBy;
		this.uniqueID = id;

		random = new Random();

		if (!inConfig) {
			// Not yet made
			this.inverted = plugin.getgConfig().isDefaultInverted();
			this.range = plugin.getgConfig().getDefaultRange();
			// Default Settings
			GeneralConfig gC = plugin.getgConfig();
			
			if (gC.isDefaultOwnerTrigger()) {
				acceptedEntities.add("OWNER");
			}
			if (gC.isDeaultPlayerEntityTrigger()) {
				acceptedEntities.add("PLAYER");
			}
			if (gC.isDefaultPeacefulEntityTrigger()) {
				acceptedEntities.add("PEACEFUL_ENTITY");
			}
			if (gC.isDefaultDroppedItemsTrigger()) {
				acceptedEntities.add("DROPPED_ITEM");
			}
			if (gC.isDefaultHostileEntityTrigger()) {
				acceptedEntities.add("HOSTILE_ENTITY");
			}
			if (gC.isDefaultInvisibleEntityTrigger()) {
				acceptedEntities.add("INVISIBLE_ENTITY");
			}
		}

	}

	public void cancelTask() {
		this.toCancel.cancel();

	}

	public List<String> getAcceptedEntities() {
		return acceptedEntities;
	}

	public Location getLocation() {
		return location.getLocation();
	}

	public UUID getOwner() {
		return ownerID;
	}

	public int getRange() {
		return range;
	}

	private Material getSensorMaterial(boolean inv) {
		if (!inv) {
			return Material.REDSTONE_TORCH_OFF;
		} else {
			return Material.REDSTONE_TORCH_ON;
		}

	}

	public String getUniqueID() {
		return this.uniqueID.toString();
	}

	public boolean isInverted() {
		return inverted;
	}

	public boolean isownerOnlyEdit() {
		return this.ownerOnlyEdit;
	}

	@Override
	public void run() {
		if (Bukkit.getWorld(this.location.getWorld()) == null)
			return;
		Location location = this.getLocation();

		triggered = false;
		List<Entity> entityList = location.getWorld().getEntities();
		for (Player p : location.getWorld().getPlayers()) {
			entityList.add(p);
		}
		
		for (Entity ent : entityList) {
			if ((ent.getLocation().distance(location) <= this.range)&& ((this.acceptedEntities.contains("HOSTILE_ENTITY") && plugin.getgConfig().getHostileMobs().contains(ent.getType().name()))
					|| (this.acceptedEntities.contains("PEACEFUL_ENTITY") && plugin.getgConfig().getPeacefulMobs().contains(ent.getType().name()))
					|| (this.acceptedEntities.contains("PLAYER") && ent.getType().name().equals("PLAYER"))
					|| (this.acceptedEntities.contains("OWNER") && ent.getUniqueId().equals(this.ownerID))
					|| (this.acceptedEntities.contains("DROPPED_ITEM") && ent.getType().name().equals("DROPPED_ITEM")))) {
				
				//If Owner is set to false and player is set to true, I will continue on.
				if((!this.acceptedEntities.contains("OWNER")) && this.acceptedEntities.contains("PLAYER"))
				{
					if(ent.getUniqueId().equals(this.getOwner()))
					{
						triggered = false;
						continue;
					}
				}
				if(ent instanceof Player)
				{
					Player pl = (Player) ent;
					if(pl.getGameMode().equals(GameMode.SPECTATOR))
					{
						triggered = false;
						continue;
					}
				}
				// Check if entity is player and that player has invisible, if
				// so continue on.
				if (ent.getType().equals(EntityType.PLAYER)) {
					Player p = (Player) ent;
					if (p.hasPermission("rps.invisible")) {
						triggered = false;
						continue;
					}
				}

				// Check if entity is invisible
				if (!this.acceptedEntities.contains("INVISIBLE_ENTITY")) {
					if (ent instanceof LivingEntity) {
						LivingEntity le = (LivingEntity) ent;
						boolean isInvisible = false;
						for (PotionEffect effect : le.getActivePotionEffects()) {
							if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
								isInvisible = true;
							}
						}
						if (isInvisible)
							continue;
					}
				}
				triggered = true;
				break;
			}
		} 
		Block b = location.getBlock();
		Material m = b.getType();
		if ((m.equals(Material.REDSTONE_TORCH_OFF))
				|| (m.equals(Material.REDSTONE_TORCH_ON))) {
			if (triggered) {
				if(!this.inverted) spawnParticle(location.clone());
				setMaterial(b, !inverted);

			} else {
				if(this.inverted) spawnParticle(location.clone());
				setMaterial(b, inverted);
			}
		} else {
			plugin.getSensorConfig().removeSensor(RPSLocation.getSLoc(location));
		}

	}

	@SuppressWarnings("deprecation")
	private void setMaterial(Block b, boolean c) {
		try
		{
			b.setTypeIdAndData(getSensorMaterial(c).getId(), b.getData(), true);

		}catch(NoSuchMethodError e)
		{
			b.setType(getSensorMaterial(c));
		}
	}

	public void setAcceptedEntities(List<String> acceptedEntities) {
		this.acceptedEntities = acceptedEntities;
	}

	public void setCancelTask(BukkitTask task) {
		this.toCancel = task;
	}

	public void setData(boolean inv, int ran, List<String> acpent, boolean ownerEdit) {
		this.setAcceptedEntities(acpent);
		this.setInverted(inv);
		this.setRange(ran);
		this.setOwnerOnlyEdit(ownerEdit);

	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public void setLocation(Location location) {
		this.location = RPSLocation.getRPSLoc(location);
	}

	public void setOwner(UUID owner) {
		this.ownerID = owner;
	}

	public boolean setOwnerOnlyEdit(boolean ownerOnlyEdit) {
		return this.ownerOnlyEdit = ownerOnlyEdit;
	}

	public void setRange(int range) {
		this.range = range;
	}

	private void spawnParticle(Location loc) {
		double d0 = loc.getX() + random.nextDouble() * 0.6D + 0.2D;
		double d1 = loc.getY() + random.nextDouble() * 0.6D + 0.2D;
		double d2 = loc.getZ() + random.nextDouble() * 0.6D + 0.2D;

		// loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, d0, d1, d2, 0,
		// 0.0D, 0.0D, 0.0D);
		int red = 199;
		int green = 21;
		int blue = 133;
		Location loc2 = new Location(loc.getWorld(), d0, d1, d2);
		loc.getWorld().spigot().playEffect(loc2, Effect.COLOURED_DUST, 0, 0, (float) red / 255, (float) green / 255,
				(float) blue / 255, 1, 0, 5);
	}

	public void pasteSettings(RPS originalRPS) {
		this.setAcceptedEntities(originalRPS.getAcceptedEntities());
		this.setInverted(originalRPS.inverted);
		this.setOwnerOnlyEdit(originalRPS.ownerOnlyEdit);
		this.setRange(originalRPS.range);
		plugin.getSensorConfig().savePaste(this.getUniqueID(), this.getAcceptedEntities(), this.isInverted(), this.isownerOnlyEdit(), this.getRange());

	}

}
