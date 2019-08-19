package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class InvisibleEntity extends TriggerTemplate {
    public String flagName = "INVISIBLE_ENTITY";
    private Material buttonMaterial = Material.FERMENTED_SPIDER_EYE;
    private String triggerPermission = "button_invisibleentitiestrigger";
    private String buttonTitle = "lang_button_invisibleentitytrigger";
    private String loreNode = "lang_button_iet_lore";
    private int slotNumber = 17;

    public InvisibleEntity(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore, this));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        boolean isInvisible = false;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = ((LivingEntity) entity);

            for (PotionEffect effects : livingEntity.getActivePotionEffects()) {
                if (effects.getType().equals(PotionEffectType.INVISIBILITY)) {
                    isInvisible = true;
                    break;
                }
            }

            if (entity instanceof Player) {
                if (isVanished(((Player) entity)))
                    isInvisible = true;
            }
        }

        if (!rps.getAcceptedTriggerFlags().contains(flagName)) {
            if (isInvisible) //INVISIBLE_ENTITY flag is disabled & entity is invisible, skip entity
                return TriggerCreator.TriggerResult.SKIP_ENTITY;
        } else {
            if (isInvisible)
                return TriggerCreator.TriggerResult.TRIGGERED; //INVISIBLE_FLAG is enabled, and entity is invisible
        }
        return TriggerCreator.TriggerResult.NOT_TRIGGERED;
    }


    private boolean isVanished(Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
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

    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
