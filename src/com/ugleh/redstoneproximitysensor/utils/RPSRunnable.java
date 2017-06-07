package com.ugleh.redstoneproximitysensor.utils;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class RPSRunnable implements Runnable{
	public RedstoneProximitySensor plugin;

	public RPSRunnable(Plugin plugin)
	{
		this.plugin = (RedstoneProximitySensor) plugin;
		Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 5L);
	}

	@Override
	public void run()
	{
		@SuppressWarnings("unchecked")
		Map<String, RPS> map = (Map<String, RPS>) this.plugin.getSensorConfig().getSensorList().clone();
		//RPS[] values = map.values().toArray();
		RPS rpslist[] = new RPS[map.size()];
		rpslist = map.values().toArray(rpslist);
		for (RPS value : rpslist)
		{
			value.run();
		}
	}
}
