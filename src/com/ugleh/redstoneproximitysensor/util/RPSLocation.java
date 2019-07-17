package com.ugleh.redstoneproximitysensor.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class RPSLocation {
    private String world;
    private Double x;
    private Double y;
    private Double z;

    public RPSLocation(String world, Double x, Double y, Double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static RPSLocation getRPSLoc(Location location) {
        return new RPSLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
    }

    public static String getSLoc(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + location.getZ();
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public String getSLoc() {
        return world + ":" + x + ":" + y + z;
    }
}
