package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.util.RPS;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class TriggerTemplate {
    public String flagName;
    public String triggerPermission;

    public abstract TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e);

    public abstract boolean buttonPressed(Boolean is_on, RPS affectedRPS, Player playerWhoClicked);

    public abstract void rpsCreated(RPS affectedRPS);

    public abstract void rpsRemoved(RPS affectedRPS);

    public abstract ItemMeta updateButtonLore(RPS selectedRPS, ItemMeta itemMeta);
}
