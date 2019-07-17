package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class TriggerAddons {
    private static TriggerAddons instance;
    public List<String> triggeredAddonFlags = new ArrayList<String>();
    public List<AddonTemplate> triggeredAddons = new ArrayList<AddonTemplate>();
    public int latestSlot = 14;

    public TriggerAddons() {
        latestSlot = RedstoneProximitySensor.getInstance().playerListener.menuSize - 1;
        instance = this;
        initTowny();
        initGP();
        initFactions();
    }

    public static TriggerAddons getInstance() {
        return instance;
    }

    private void initTowny() {
        Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("Towny");
        if (plug != null && plug.isEnabled()) {
            triggeredAddons.add(new TownyAddon());
        }
    }

    private void initFactions() {
        Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("LegacyFactions");
        if (plug != null && plug.isEnabled()) {
            triggeredAddons.add(new LegacyFactionsAddon());
        }
    }

    private void initGP() {
        Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("GriefPrevention");
        if (plug != null && plug.isEnabled()) {
            triggeredAddons.add(new GPAddon());
        }
    }

    @Deprecated
    public int getSlot() {
        return getNextAvaliableSlot();
    }

    public int getNextAvaliableSlot() {
        --latestSlot;
        return latestSlot + 1;
    }

    public boolean isTriggered() {
        return false;
    }

    public boolean triggerCheck(RPS rps, Entity ent) {
        for (AddonTemplate addon : triggeredAddons) {
            if (addon.checkTrigger(rps, ent)) return true;
        }
        return false;
    }

    public void addTrigger(Trigger trigger) {
        RedstoneProximitySensor.getInstance().playerListener.addTrigger(trigger);
    }

}
