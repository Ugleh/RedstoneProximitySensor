package com.ugleh.redstoneproximitysensor.addons;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;
import com.ugleh.redstoneproximitysensor.utils.Trigger; 

import net.redstoneore.legacyfactions.entity.Faction;
import net.redstoneore.legacyfactions.entity.FactionColl;
 
public class LegacyFactionsAddon extends AddonTemplate{
	public String flagName = "LEGACYFACTIONS";
	public int slot;
	public LegacyFactionsAddon(int slotNumber)
	{
		this.slot = slotNumber;
		createButton();
	}
	
	private void createButton()
	{
		List<String> lore = pl().WordWrapLore(pl().langString("lang_button_lf_lore"));
		pl().addTrigger(new Trigger("button_lfactiontrigger", new ItemStack(Material.FENCE), slot, "lang_button_lftrigger", flagName, "lang_button_true", "lang_button_false", lore));

	}
	private RedstoneProximitySensor getInstance()
	{
		return RedstoneProximitySensor.getInstance();
	}
	
	private PlayerListener pl()
	{
		return getInstance().playerListener;
	}

	@Override
	public boolean checkTrigger(List<String> acceptedEntities, Entity e, Location l, UUID ownerID) {
		if(!acceptedEntities.contains(flagName))return false;
		if(!(e instanceof Player)) return false;
		Faction faction = FactionColl.get(Bukkit.getOfflinePlayer(ownerID));
		if(faction.getOnlinePlayers().contains((Player)e))
			return true;
		return false;
		
	}
	
}
