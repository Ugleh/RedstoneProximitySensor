package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
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

    //private HashMap<String, String> sensorFactions = new HashMap<String, String>();
    public LegacyFactionsTrigger() {
        CreateButton();
    }

    private void CreateButton() {
        List<String> lore = pl().WordWrapLore(pl().langString("lang_button_lf_lore"));
        pl().addTrigger(new Trigger("button_lfactiontrigger", new ItemStack(Material.OAK_FENCE), "lang_button_lftrigger", flagName, "lang_button_true", "lang_button_false", lore, this));

    }

    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }

    private PlayerListener pl() {
        return getInstance().playerListener;
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
    public void buttonPressed(Boolean on, RPS affectedRPS) {
    }

    @Override
    public void rpsCreated(RPS affectedRPS) {
    }

    @Override
    public void rpsRemoved(RPS affectedRPS) {

    }

}
