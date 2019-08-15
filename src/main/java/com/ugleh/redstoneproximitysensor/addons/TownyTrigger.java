package com.ugleh.redstoneproximitysensor.addons;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TownyTrigger extends TriggerTemplate {
    public String flagName = "TOWNY";

    public TownyTrigger() {
        CreateButton();
    }

    private void CreateButton() {
        List<String> lore = pl().WordWrapLore(pl().langString("lang_button_tt_lore"));
        pl().addTrigger(new Trigger("button_townytrigger", new ItemStack(Material.BEACON), "lang_button_townytrigger", "TOWNY", "lang_button_true", "lang_button_false", lore, this));

    }

    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }

    private PlayerListener pl() {
        return getInstance().playerListener;
    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e) {
        Location l = rps.getLocation();
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if (!(e instanceof Player)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if (TownyUniverse.isWilderness(l.getBlock())) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        try {
            Resident r = TownyUniverse.getDataSource().getResident(e.getName());
            Town rTown = r.getTown();
            boolean isWild = TownyUniverse.isWilderness(l.getBlock());
            if (isWild && rTown == null) {
                return TriggerCreator.TriggerResult.TRIGGERED;
            } else if (!isWild && rTown == null) {
                return TriggerCreator.TriggerResult.NOT_TRIGGERED;
            } else if (isWild && rTown != null) {
                return TriggerCreator.TriggerResult.NOT_TRIGGERED;
            }
            TownBlock townBlock = TownyUniverse.getTownBlock(l);
            if (townBlock.getTown().getUID() == rTown.getUID()) return TriggerCreator.TriggerResult.TRIGGERED;
            else return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        } catch (NotRegisteredException e1) {
            e1.printStackTrace();
        }
        return TriggerCreator.TriggerResult.TRIGGERED;
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

}
