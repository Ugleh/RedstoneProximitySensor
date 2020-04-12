package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.Mobs;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PeacefulEntity extends TriggerTemplate {
    public String flagName = "PEACEFUL_ENTITY";
    private Material buttonMaterial = Material.COOKED_BEEF;
    private String triggerPermission = "button_peacefulentitytrigger";
    private String buttonTitle = "lang_button_peacefulentitytrigger";
    private String loreNode = "lang_button_pet2_lore";
    private int slotNumber = 7;

    public PeacefulEntity(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore, this));
    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if(getInstance().getgConfig().isSupportedEntity(entity.getType()) && Mobs.isPeaceful(entity.getType()))
            return TriggerCreator.TriggerResult.TRIGGERED;
        else
            return TriggerCreator.TriggerResult.NOT_TRIGGERED;
    }

    @Override
    public boolean buttonPressed(Boolean is_on, RPS affectedRPS, Player playerWhoClicked) {
        return true;
    }

    @Override
    public void rpsCreated(RPS affectedRPS) {

    }

    @Override
    public void rpsRemoved(RPS affectedRPS) {

    }

    @Override
    public ItemMeta updateButtonLore(RPS selectedRPS, ItemMeta itemMeta) {
        return itemMeta;
    }

    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
