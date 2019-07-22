package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import me.ryanhamshire.GriefPrevention.DataStore;
import net.redstoneore.legacyfactions.entity.FPlayer;
import net.redstoneore.legacyfactions.entity.FPlayerColl;
import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.entity.FactionColl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class LegacyFactionsTrigger extends TriggerTemplate {

    public String flagName = "LEGACYFACTIONS";
    private Material buttonMaterial = Material.OAK_FENCE;
    public String triggerPermission = "button_lfactiontrigger";
    private String buttonTitle = "lang_button_lftrigger";
    private String loreNode = "lang_button_lf_lore";

    public LegacyFactionsTrigger() {
        List<String> lore = pl().WordWrapLore(pl().langString(loreNode));
        pl().addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial), buttonTitle, flagName, "lang_button_true", "lang_button_false", lore, this));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e) {
        UUID ownerID = rps.getOwner();
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if (!(e instanceof Player)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        Faction faction = FactionColl.get(Bukkit.getOfflinePlayer(ownerID));
        FPlayer fplayer = FPlayerColl.get((Player) e);
        if (faction.getFPlayers().contains(fplayer))
            return TriggerCreator.TriggerResult.TRIGGERED;
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

    private PlayerListener pl() {
        return getInstance().playerListener;
    }

}
