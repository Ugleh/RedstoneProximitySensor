package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DroppedItem  extends TriggerTemplate {
    public String flagName = "DROPPED_ITEM";
    private Material buttonMaterial = Material.PUMPKIN_SEEDS;
    private String triggerPermission = "button_droppeditemtrigger";
    private String buttonTitle = "lang_button_droppeditemtrigger";
    private String loreNode = "lang_button_dit_lore";
    private int slotNumber = 14;

    public DroppedItem(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e) {
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if(e.getType().name().equals("DROPPED_ITEM"))
            return TriggerCreator.TriggerResult.TRIGGERED;
        else
            return TriggerCreator.TriggerResult.NOT_TRIGGERED;
    }

    @Override
    public boolean buttonPressed(Boolean is_on, RPS affectedRPS) {
        return true;
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
