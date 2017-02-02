package com.ugleh.redstoneproximitysensor.addons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
public class TriggerAddons {
	private static TriggerAddons instance;
	public List<String> triggeredAddonFlags = new ArrayList<String>();
	public List<AddonTemplate> triggeredAddons = new ArrayList<AddonTemplate>();
	public int latestSlot = 14;
	public TriggerAddons()
	{
		instance = this;
		initTowny();
		initGP();
	}

	private void initTowny()
	{
		Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("Towny");
		if(plug !=null && plug.isEnabled())
		{
			triggeredAddons.add(new TownyAddon(getSlot()));
		}
	}
	
	private void initGP()
	{
		Plugin plug = RedstoneProximitySensor.getInstance().getServer().getPluginManager().getPlugin("GriefPrevention");
		if(plug !=null && plug.isEnabled())
		{
			triggeredAddons.add(new GPAddon(getSlot()));
		}
	}
	private int getSlot() {
		--latestSlot;
		return latestSlot+1;
	}

	public boolean isTriggered()
	{
		return false;
	}
	
	public static TriggerAddons getInstance()
	{
		return instance;
	}

	public boolean triggerCheck(List<String> acceptedEntities, Entity ent, Location l) {
		for(AddonTemplate addon : triggeredAddons)
		{
			if(addon.checkTrigger(acceptedEntities, ent, l)) return true;
		}
		return false;
	}

}
