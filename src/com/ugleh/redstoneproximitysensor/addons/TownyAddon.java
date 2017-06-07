package com.ugleh.redstoneproximitysensor.addons;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;
import com.ugleh.redstoneproximitysensor.utils.Trigger;
 
public class TownyAddon extends AddonTemplate{
	public String flagName = "TOWNY";
	public int slot;
	public TownyAddon(int slotNumber)
	{
		this.slot = slotNumber;
		createButton();
	}
	
	private void createButton()
	{
		List<String> lore = pl().WordWrapLore(pl().langString("lang_button_tt_lore"));
		pl().addTrigger(new Trigger(pl().guiMenu, "button_townytrigger", new ItemStack(Material.BEACON), slot, "lang_button_townytrigger", "TOWNY", "lang_button_true", "lang_button_false", lore, pl().glow));

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
		if(TownyUniverse.isWilderness(l.getBlock())) return false;
		try {
			Resident r = TownyUniverse.getDataSource().getResident(e.getName());
			Town rTown = r.getTown();
			boolean isWild = TownyUniverse.isWilderness(l.getBlock());
			if(isWild && rTown == null)
				{
					return true;
				}
			else if(!isWild && rTown == null)
			{
				return false;
			}
			else if(isWild && rTown != null)
			{
				return false;
			}
			TownBlock townBlock = TownyUniverse.getTownBlock(l);
			if(townBlock.getTown().getUID() == rTown.getUID()) return true;
			else return false;
		} catch (NotRegisteredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}
	
}
