package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.util.RPS;
import org.bukkit.entity.Entity;

public abstract class TriggerTemplate {
    public String flagName;
    public String triggerPermission;

    public abstract TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e);

    public abstract void buttonPressed(Boolean on, RPS affectedRPS);

    public abstract void rpsCreated(RPS affectedRPS);

    public abstract void rpsRemoved(RPS affectedRPS);
}
