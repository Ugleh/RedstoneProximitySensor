package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class RPSRunnable implements Runnable {
    private RedstoneProximitySensor plugin;
    Map<Chunk, Entity[]> cachedEntities = new HashMap<>();
    public RPSRunnable(Plugin plugin) {
        this.plugin = (RedstoneProximitySensor) plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, this.plugin.getgConfig().getTick_rate());
    }

    @Override
    public void run() {
        Map<String, RPS> copy = new HashMap<>(this.plugin.getSensorConfig().getSensorList());
        cachedEntities.clear();
        for (Map.Entry<String, RPS> stringRPSEntry : copy.entrySet()) {
            stringRPSEntry.getValue().run(this);
        }
    }
}
