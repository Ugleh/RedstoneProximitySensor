package com.ugleh.redstoneproximitysensor.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class WorldListener implements Listener {
	public RedstoneProximitySensor redstoneProximitySensor;
	public WorldListener(RedstoneProximitySensor redstoneProximitySensor) {
		this.redstoneProximitySensor = redstoneProximitySensor;
	}

	@EventHandler
	public void worldLoad(WorldLoadEvent e)
	{
		Bukkit.broadcastMessage("Loading world: " + e.getWorld().getName());
		redstoneProximitySensor.sConfig.grabSensors(e.getWorld());
	}
}
