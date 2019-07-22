package com.ugleh.redstoneproximitysensor.addons;

import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class GPTrigger extends TriggerTemplate {
    private static DataStore dataStore;
    public String flagName = "GP";

    public GPTrigger() {
        CreateButton();
    }

    private static DataStore getDataStore() {
        if (dataStore == null) {
            dataStore = GriefPrevention.getPlugin(GriefPrevention.class).dataStore;
        }
        return dataStore;
    }

    private void CreateButton() {
        List<String> lore = WordWrapLore(pl().langString("lang_button_gp_lore"));
        pl().addTrigger(new Trigger("button_gptrigger", new ItemStack(Material.GOLDEN_SHOVEL), "lang_button_gptrigger", flagName, "lang_button_true", "lang_button_false", lore, this));

    }

    private PlayerListener pl() {
        return PlayerListener.instance;
    }

    public List<String> WordWrapLore(String string) {
        StringBuilder sb = new StringBuilder(string);

        int i = 0;
        while (i + 35 < sb.length() && (i = sb.lastIndexOf(" ", i + 35)) != -1) {
            sb.replace(i, i + 1, "\n");
        }
        return Arrays.asList(sb.toString().split("\n"));

    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e) {
        Location l = rps.getLocation();
        if (!rps.getAcceptedTriggerFlags().contains(flagName)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        if (!(e instanceof Player)) return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        Claim claim = getDataStore().getClaimAt(l, true, null);
        if(claim != null && claim.allowBuild((Player) e, null) == null)
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
}
