package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class TriggerCreator {
    private static TriggerCreator instance;
    public List<TriggerTemplate> triggeredAddons = new ArrayList<>();
    public int latestSlot;

    public TriggerCreator() {
        latestSlot = RedstoneProximitySensor.getInstance().playerListener.menuSize - 1;
        instance = this;
        initTowny();
        initGP();
        initFactions();
    }

    public static TriggerCreator getInstance() {
        return instance;
    }

    private void initTowny() {
        Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("Towny");
        if (plug != null && plug.isEnabled()) {
            triggeredAddons.add(new TownyTrigger());
        }
    }

    private void initFactions() {
        Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("LegacyFactions");
        if (plug != null && plug.isEnabled()) {
            triggeredAddons.add(new LegacyFactionsTrigger());
        }
    }

    private void initGP() {
        Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("GriefPrevention");
        if (plug != null && plug.isEnabled()) {
            triggeredAddons.add(new GPTrigger());
        }
    }

    @Deprecated
    public int getSlot() {
        return getAvailableSlot();
    }

    public int getAvailableSlot() {
        --latestSlot;
        return latestSlot + 1;
    }


    public TriggerResult triggerCheck(RPS rps, Entity entity) {
        ArrayList<TriggerResult> triggerResults = new ArrayList<>();

        for(TriggerTemplate trigger : RedstoneProximitySensor.getInstance().playerListener.getTriggerRunners()) {
            TriggerResult result = trigger.checkTrigger(rps, entity);
            triggerResults.add(result);
            if(result == TriggerResult.SKIP_ENTITY) break;
        }

        for(TriggerTemplate trigger : triggeredAddons) {
            TriggerResult result = trigger.checkTrigger(rps, entity);
            triggerResults.add(result);
            if(result == TriggerResult.SKIP_ENTITY) break;
        }

        if(triggerResults.contains(TriggerResult.SKIP_ENTITY)) {
            return TriggerResult.SKIP_ENTITY;
        }else if(triggerResults.contains(TriggerResult.TRIGGERED)) {
            return TriggerResult.TRIGGERED;
        }else {
            return TriggerResult.NOT_TRIGGERED;
        }
    }

    public void addTrigger(Trigger trigger) {
        RedstoneProximitySensor.getInstance().playerListener.addTrigger(trigger);
    }


    public enum TriggerResult {
        TRIGGERED,
        NOT_TRIGGERED,
        SKIP_ENTITY
    }
}
