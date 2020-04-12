package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;


public class Others extends TriggerTemplate {

    public Others(PlayerListener playerListener) {
    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        if(entity instanceof Player) {
            Player player = ((Player) entity);
            //Skip Spectator Players
            if(player.getGameMode().equals(GameMode.SPECTATOR))
                return TriggerCreator.TriggerResult.SKIP_ENTITY;
            //Skip RPS Ignored Players
            if(getInstance().playerListener.rpsIgnoreList.contains(player.getUniqueId()))
                return TriggerCreator.TriggerResult.SKIP_ENTITY;
        }
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
