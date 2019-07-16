package com.ugleh.redstoneproximitysensor.utils;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerAddons;
import com.ugleh.redstoneproximitysensor.configs.GeneralConfig;
import com.ugleh.redstoneproximitysensor.configs.Mobs;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.*;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class RPS {
    public RedstoneProximitySensor plugin;
    public GeneralConfig generalConfig;
    private UUID uniqueID;
    private RPSLocation location;
    private UUID ownerID;
    private int range = 5;
    private double rangeSquared;
    private boolean inverted = false;
    private boolean ownerOnlyEdit = true;
    private boolean triggered = false;
    private ArrayList<String> activeFlags = new ArrayList<String>();
    private Random random;

    public RPS(RedstoneProximitySensor plugin, RPSLocation location, UUID placedBy, UUID id, boolean inConfig) {
        this.plugin = plugin;
        this.location = location;
        this.ownerID = placedBy;
        this.uniqueID = id;

        random = new Random();
        generalConfig = plugin.getgConfig();

        if (!inConfig) {
            //Config not yet made
            this.inverted = plugin.getgConfig().isDefaultInverted();
            this.range = plugin.getgConfig().getDefaultRange();
            this.rangeSquared = range * range;

            // Default Settings
            if (generalConfig.isDefaultOwnerTrigger())
                activeFlags.add("OWNER");
            if (generalConfig.isDeaultPlayerEntityTrigger())
                activeFlags.add("PLAYER");
            if (generalConfig.isDefaultPeacefulEntityTrigger())
                activeFlags.add("PEACEFUL_ENTITY");
            if (generalConfig.isDefaultDroppedItemsTrigger())
                activeFlags.add("DROPPED_ITEM");
            if (generalConfig.isDefaultHostileEntityTrigger())
                activeFlags.add("HOSTILE_ENTITY");
            if (generalConfig.isDefaultInvisibleEntityTrigger())
                activeFlags.add("INVISIBLE_ENTITY");
            if (generalConfig.isDefaultVehcileEntityTrigger())
                activeFlags.add("VEHCILE_ENTITY");
            if (generalConfig.isDefaultProjectileEntityTrigger())
                activeFlags.add("PROJECTILE_ENTITY");
        }

    }

    public static Entity[] getNearbyEntities(Location l, int radius, double radiusSquared) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distanceSquared(l) <= radiusSquared && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public ArrayList<String> getAcceptedEntities() {
        return activeFlags;
    }

    public void setAcceptedEntities(ArrayList<String> acceptedEntities) {
        this.activeFlags = acceptedEntities;
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public void setLocation(Location location) {
        this.location = RPSLocation.getRPSLoc(location);
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
        this.rangeSquared = range * range;
    }

    public String getUniqueID() {
        return this.uniqueID.toString();
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isownerOnlyEdit() {
        return this.ownerOnlyEdit;
    }

    public void run() {
        triggered = false;

        if (Bukkit.getWorld(this.location.getWorld()) == null)
            return;
        Location location = this.getLocation();

        boolean isLoaded = location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4);
        if (!isLoaded)
            return;


        Entity[] entityList = getNearbyEntities(this.getLocation(), this.range, this.rangeSquared);

        for (Entity ent : entityList) {
            if (ent.getWorld() != location.getWorld()) continue;
            if(        (this.activeFlags.contains("HOSTILE_ENTITY") && isSupportedEntity(ent) && Mobs.isHostile(ent.getType()))
                    || (this.activeFlags.contains("PEACEFUL_ENTITY") && isSupportedEntity(ent) && Mobs.isPeaceful(ent.getType()))
                    || (this.activeFlags.contains("PLAYER") && ent instanceof Player)
                    || (this.activeFlags.contains("OWNER") && ent.getUniqueId().equals(this.ownerID))
                    || (this.activeFlags.contains("DROPPED_ITEM") && ent.getType().name().equals("DROPPED_ITEM"))
                    || (this.activeFlags.contains("PROJECTILE_ENTITY") && ent instanceof Projectile)
                    || (this.activeFlags.contains("VEHICLE_ENTITY") && ent instanceof Vehicle)
                    || (TriggerAddons.getInstance() != null && TriggerAddons.getInstance().triggerCheck(this, ent))) {

                //If Owner is set to false and player is set to true, I will continue on.
                if ((!this.activeFlags.contains("OWNER")) && this.activeFlags.contains("PLAYER")) {
                    if (ent.getUniqueId().equals(this.getOwner())) {
                        triggered = false;
                        continue;
                    }
                }
                if (ent instanceof Player) {
                    Player pl = (Player) ent;
                    if (pl.getGameMode().equals(GameMode.SPECTATOR)) {
                        triggered = false;
                        continue;
                    }
                }
                // Check if entity is player and that player has invisible, if
                // so continue on.
                if (ent.getType().equals(EntityType.PLAYER)) {
                    Player p = (Player) ent;
                    if (RedstoneProximitySensor.getInstance().playerListener.rpsIgnoreList.contains(p.getUniqueId())) {
                        triggered = false;
                        continue;
                    }
                }

                // Check if entity is invisible
                if (!this.activeFlags.contains("INVISIBLE_ENTITY")) {
                    if (ent instanceof LivingEntity) {
                        LivingEntity le = (LivingEntity) ent;
                        boolean isInvisible = false;
                        for (PotionEffect effect : le.getActivePotionEffects()) {
                            if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
                                isInvisible = true;
                            }
                        }
                        if(ent instanceof Player)
                        {
                        	if(isVanished((Player) ent))
                        		isInvisible = true;
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
        if (m.equals(Material.REDSTONE_TORCH) || m.equals(Material.REDSTONE_WALL_TORCH)) {
            if (triggered) {
                if (!this.inverted && generalConfig.useParticles()) spawnParticle(location);
                setLitState(b, !inverted);

            } else {
                if (this.inverted && generalConfig.useParticles()) spawnParticle(location);
                setLitState(b, inverted);
            }
        } else {
            plugin.getSensorConfig().removeSensor(RPSLocation.getSLoc(location));
        }

    }

    private boolean isSupportedEntity(Entity entity) {
    	return plugin.getgConfig().isSupportedEntity(entity.getType());
	}

	private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
}
    
    private void setLitState(Block b, boolean c) {
        Lightable data = (Lightable) b.getBlockData();
        //Is the current lit setting the same as the inverted setting for said RPS Torch, if not, trigger the change.
        if (data.isLit() != c) {
            data.setLit(c);
            b.setBlockData(data);

        }
    }

    public void setData(boolean inverted, int range, ArrayList<String> acpent, boolean ownerEdit) {
        this.setAcceptedEntities(acpent);
        this.setInverted(inverted);
        this.setRange(range);
        this.setOwnerOnlyEdit(ownerEdit);

    }

    public boolean setOwnerOnlyEdit(boolean ownerOnlyEdit) {
        return this.ownerOnlyEdit = ownerOnlyEdit;
    }

    private void spawnParticle(Location loc) {
        double d0 = loc.getX() + random.nextDouble() * 0.6D + 0.2D;
        double d1 = loc.getY() + random.nextDouble() * 0.6D + 0.2D;
        double d2 = loc.getZ() + random.nextDouble() * 0.6D + 0.2D;

        int red = 199;
        int green = 21;
        int blue = 133;
        Location loc2 = new Location(loc.getWorld(), d0, d1, d2);
        loc.getWorld().spawnParticle(Particle.REDSTONE, new Location(loc.getWorld(), loc2.getX(), loc2.getY(), loc2.getZ()), 1, new Particle.DustOptions(Color.fromRGB(red, green, blue), 1));

    }

    public void pasteSettings(RPS originalRPS) {
        this.setAcceptedEntities(originalRPS.getAcceptedEntities());
        this.setInverted(originalRPS.inverted);
        this.setOwnerOnlyEdit(originalRPS.ownerOnlyEdit);
        this.setRange(originalRPS.range);
        //plugin.getSensorConfig().savePaste(this.getUniqueID(), this.getAcceptedEntities(), this.isInverted(), this.isownerOnlyEdit(), this.getRange());
        plugin.getSensorConfig().savePaste(this);
    }
}
