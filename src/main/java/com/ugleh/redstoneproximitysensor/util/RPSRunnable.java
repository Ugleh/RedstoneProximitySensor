package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class RPSRunnable implements Runnable {
    public RedstoneProximitySensor plugin;

    public RPSRunnable(Plugin plugin) {
        this.plugin = (RedstoneProximitySensor) plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 5L);
    }

    @Override
    public void run() {
        @SuppressWarnings("unchecked")
        Map<String, RPS> map = (Map<String, RPS>) this.plugin.getSensorConfig().getSensorList().clone();
        //RPS[] values = map.values().toArray();
        RPS rpslist[] = new RPS[map.size()];
        rpslist = map.values().toArray(rpslist);
        for (RPS value : rpslist) {
            value.run();
        }
    }
}
