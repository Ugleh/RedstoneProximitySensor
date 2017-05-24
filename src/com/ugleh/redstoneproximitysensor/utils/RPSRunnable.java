package com.ugleh.redstoneproximitysensor.utils;

import java.util.Map;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class RPSRunnable implements Runnable{
	public RedstoneProximitySensor plugin;
	private BukkitTask toCancel;

	public RPSRunnable(Plugin plugin)
	{
		this.plugin = (RedstoneProximitySensor) plugin;
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

	public void setCancelTask(BukkitTask task) {
		this.toCancel = task;
		
	}
	public void cancelTask() {
		this.toCancel.cancel();

	}
}
