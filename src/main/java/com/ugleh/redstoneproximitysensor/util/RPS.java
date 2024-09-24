package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.config.GeneralConfig;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RPS {
    private RedstoneProximitySensor plugin;
    private GeneralConfig generalConfig;
    private UUID uniqueID;
    private RPSLocation location;
    private UUID ownerUUID;
    private int range = 5;
    private double rangeSquared;
    private boolean inverted = false;
    private boolean owner_only_edit = true;
    private ArrayList<String> active_flags = new ArrayList<>();
    private ArrayList<String> individual_mobs = new ArrayList<>();
    private Random random;

    public RPS(RedstoneProximitySensor plugin, RPSLocation location, UUID placedBy, UUID id, boolean inConfig) {
        this.plugin = plugin;
        this.location = location;
        this.setOwner(placedBy);
        this.uniqueID = id;
        random = new Random();
        generalConfig = plugin.getgConfig();

        if (!inConfig) {
            //Config not yet made
            this.inverted = generalConfig.isDefaultInverted();
            this.owner_only_edit = generalConfig.isDefaultOwnerOnlyEdit();
            this.range = generalConfig.getDefaultRange();
            this.rangeSquared = range * range;

            // Default Settings
            for(Map.Entry<String, Boolean> entry : generalConfig.getDefaultTriggers().entrySet()) {
                if(entry.getValue()) {
                    active_flags.add(entry.getKey());
                }
            }
        }
    }

    private static Entity[] getNearbyEntities(Location l, int radius, double radiusSquared, RPSRunnable runnable) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                Location loopedLocation = new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16));
                if(!l.getWorld().isChunkLoaded(loopedLocation.getBlockX()  >> 4,loopedLocation.getBlockZ()   >> 4)) continue;
                Chunk loopedLocationChunk = loopedLocation.getChunk();
                    Entity[] chunkEntities;
                    if(runnable.cachedEntities.containsKey(loopedLocationChunk)) {
                        chunkEntities = runnable.cachedEntities.get(loopedLocationChunk);
                    }else{
                        chunkEntities = loopedLocationChunk.getEntities();
                    }
                    for (Entity e : chunkEntities) {
                        if (e.getLocation().distanceSquared(l) <= radiusSquared && e.getLocation().getBlock() != l.getBlock()) {
                            radiusEntities.add(e);
                        }
                    }
                    runnable.cachedEntities.put(loopedLocationChunk, chunkEntities);
            }
        }
        return radiusEntities.toArray(new Entity[0]);
    }

    void run(RPSRunnable runnable) {
        boolean triggered = false;

        if (Bukkit.getWorld(this.location.getWorld()) == null)
            return;
        Location location = this.getLocation();

        boolean isLoaded = Objects.requireNonNull(location.getWorld(), "World could not be found in Sensor: " + this.getUniqueID()).isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4);
        if (!isLoaded)
            return;

        Entity[] entityList = getNearbyEntities(this.getLocation(), this.range, this.rangeSquared, runnable);
        for (Entity entity : entityList) {
            TriggerCreator.TriggerResult triggerResult = TriggerCreator.getInstance().triggerCheck(this, entity);
            if(triggerResult == TriggerCreator.TriggerResult.SKIP_ENTITY) continue;
            if(triggerResult == TriggerCreator.TriggerResult.TRIGGERED) {
                triggered = true;
                break;
            }
        }

        Block b = location.getBlock();
        Material m = b.getType();
        if (m.equals(Material.REDSTONE_TORCH) || m.equals(Material.REDSTONE_WALL_TORCH)) {
            if (triggered) {
                if (!this.inverted && generalConfig.isParticlesEnabled()) spawnParticle(location);
                setLitState(b, !inverted);

            } else {
                if (this.inverted && generalConfig.isParticlesEnabled()) spawnParticle(location);
                setLitState(b, inverted);
            }
        } else {
            plugin.getSensorConfig().removeSensor(RPSLocation.getSLoc(location));
        }
    }

    private void setLitState(Block b, boolean c) {
        try {
            Method method = b.getClass().getMethod("getBlockData");
            Lightable data = (Lightable) method.invoke(b);
            if (data.isLit() != c) {
                data.setLit(c);
                b.setBlockData(data);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

        }
/*        Lightable data = (Lightable) b.getBlockData();
        //Is the current lit setting the same as the inverted setting for said RPS Torch, if not, trigger the change.
        if (data.isLit() != c) {
            data.setLit(c);
            b.setBlockData(data);
        }*/
    }

    public void setData(boolean inverted, int range, ArrayList<String> acceptedEntities, ArrayList<String> individualMobs, boolean ownerEdit) {
        this.setIndividualMobs(individualMobs);
        this.setAcceptedTriggerFlags(acceptedEntities);
        this.setInverted(inverted);
        this.setRange(range);
        this.setOwnerOnlyEdit(ownerEdit);

    }

    public void setOwnerOnlyEdit(boolean ownerOnlyEdit) {
        this.owner_only_edit = ownerOnlyEdit;
    }

    private void spawnParticle(Location loc) {
        double d0 = loc.getX() + random.nextDouble() * 0.6D + 0.2D;
        double d1 = loc.getY() + random.nextDouble() * 0.6D + 0.2D;
        double d2 = loc.getZ() + random.nextDouble() * 0.6D + 0.2D;

        int red = 199;
        int green = 21;
        int blue = 133;
        Location loc2 = new Location(loc.getWorld(), d0, d1, d2);
        Objects.requireNonNull(loc.getWorld(), "World could not be found in Sensor: " + this.getUniqueID()).spawnParticle(Particle.DUST, loc2, 1, new Particle.DustOptions(Color.fromRGB(red, green, blue), 1));

    }

    public void pasteSettings(RPS originalRPS) {
        this.setAcceptedTriggerFlags(originalRPS.getAcceptedTriggerFlags());
        this.setInverted(originalRPS.inverted);
        this.setOwnerOnlyEdit(originalRPS.owner_only_edit);
        this.setRange(originalRPS.range);
        plugin.getSensorConfig().savePaste(this);
    }

    public ArrayList<String> getAcceptedTriggerFlags() {
        return active_flags;
    }

    public void setAcceptedTriggerFlags(ArrayList<String> acceptedEntities) {
        this.active_flags = acceptedEntities;
    }

    public ArrayList<String> getIndividualMobs() {
        return individual_mobs;
    }

    public void setIndividualMobs(ArrayList<String> individualMobs) {
        this.individual_mobs = individualMobs;
    }


    public Location getLocation() {
        return location.getLocation();
    }

    public void setLocation(Location location) {
        this.location = RPSLocation.getRPSLoc(location);
    }

    public UUID getOwner() {
        return ownerUUID;
    }

    private void setOwner(UUID owner) {
        this.ownerUUID = owner;
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

    public boolean isOwnerOnlyEdit() {
        return this.owner_only_edit;
    }
}
