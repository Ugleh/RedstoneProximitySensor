package com.ugleh.redstoneproximitysensor.addons;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;
import com.ugleh.redstoneproximitysensor.utils.Trigger;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
 
public class GPAddon extends AddonTemplate{
	public String flagName = "GP";
	public int slot;
    private static DataStore dataStore;
	public GPAddon(int slotNumber)
	{
		this.slot = slotNumber;
		createButton();
	}

    private static DataStore getDataStore() {
        if (dataStore == null) {
            dataStore = GriefPrevention.getPlugin(GriefPrevention.class).dataStore;
        }
        return dataStore;
    }
	private void createButton()
	{
		List<String> lore = WordWrapLore(pl().langString("lang_button_gp_lore"));
		pl().addTrigger(new Trigger(pl().guiMenu, "button_gptrigger", new ItemStack(Material.GOLD_SPADE), slot, "lang_button_gptrigger", flagName, "lang_button_true", "lang_button_false", lore, pl().glow));

	}
	
	private PlayerListener pl()
	{
		return PlayerListener.instance;
	}

	public List<String> WordWrapLore(String string)
	{
		StringBuilder sb = new StringBuilder(string);

		int i = 0;
		while (i + 35 < sb.length() && (i = sb.lastIndexOf(" ", i + 35)) != -1) {
		    sb.replace(i, i + 1, "\n");
		}
		return Arrays.asList(sb.toString().split("\n"));
		
	}

	@Override
	public boolean checkTrigger(List<String> acceptedEntities, Entity e, Location l, UUID ownerID)
	{
		if(!acceptedEntities.contains(flagName))return false;
		if(!(e instanceof Player)) return false;
		Claim claim = getDataStore().getClaimAt(l, true, null);
		return claim != null && claim.allowBuild((Player)e, null) == null;
	}
}
