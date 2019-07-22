package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ProjectileEntity extends TriggerTemplate {
    public String flagName = "PROJECTILE_ENTITY";
    private Material buttonMaterial = Material.ARROW;
    private String triggerPermission = "button_projectiletrigger";
    private String buttonTitle = "lang_button_projectiletrigger";
    private String loreNode = "lang_button_pt_lore";
    private int slotNumber = 16;

    public ProjectileEntity(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if(entity instanceof Projectile)
            return TriggerCreator.TriggerResult.TRIGGERED;
        else
            return TriggerCreator.TriggerResult.NOT_TRIGGERED;
    }

    @Override
    public void buttonPressed(Boolean on, RPS affectedRPS) {

    }

    @Override
    public void rpsCreated(RPS affectedRPS) {

    }

    @Override
    public void rpsRemoved(RPS affectedRPS) {

    }

    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
