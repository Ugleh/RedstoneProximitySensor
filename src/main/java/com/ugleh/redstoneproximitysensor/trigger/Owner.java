package com.ugleh.redstoneproximitysensor.trigger;

import com.cryptomorin.xseries.XMaterial;
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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Owner extends TriggerTemplate {
    public String flagName = "OWNER";
    private Material buttonMaterial = XMaterial.PLAYER_HEAD.parseMaterial();
    private String triggerPermission = "button_ownertrigger";
    private String buttonTitle = "lang_button_ownertrigger";
    private String loreNode = "lang_button_ot_lore";
    private int slotNumber = 4;

    public Owner(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore, this));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if(entity.getUniqueId().equals(rps.getOwner())) return TriggerCreator.TriggerResult.TRIGGERED;
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
