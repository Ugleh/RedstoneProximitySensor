package com.ugleh.redstoneproximitysensor.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class WorldListener implements Listener {
	public WorldListener() {
	}

	@EventHandler
	public void worldLoad(WorldLoadEvent e)
	{
		RedstoneProximitySensor.getInstance().sConfig.grabSensors(e.getWorld());
	}
}
