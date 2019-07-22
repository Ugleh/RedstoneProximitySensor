package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerEntity extends TriggerTemplate {
    public String flagName = "PLAYER_ENTITY";
    private Material buttonMaterial = Material.DIAMOND_SWORD;
    private String triggerPermission = "button_playerentitytrigger";
    private String buttonTitle = "lang_button_playerentitytrigger";
    private String loreNode = "lang_button_pet1_lore";
    private int slotNumber = 5;

    public PlayerEntity(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if(entity instanceof Player) {
            if(!entity.getUniqueId().equals(rps.getOwner()))
                return TriggerCreator.TriggerResult.TRIGGERED;
        }
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
