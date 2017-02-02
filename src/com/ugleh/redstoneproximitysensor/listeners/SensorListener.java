package com.ugleh.redstoneproximitysensor.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.utils.RPSLocation;

public class SensorListener implements Listener {
	
	@EventHandler
	public void BlockRedstoneEvent(BlockRedstoneEvent e)
	{
		if(!(e.getBlock().getType().equals(Material.REDSTONE_TORCH_OFF) || e.getBlock().getType().equals(Material.REDSTONE_TORCH_ON))) return;
		if(getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation())))
		{
			e.setNewCurrent(e.getOldCurrent());
		}
	}
	@EventHandler
	public void SensorBroke(BlockBreakEvent e)
	{
		Location loc = e.getBlock().getLocation();
		Boolean sensor = false;
		if(getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation())))
		{
			sensor = true;
			getInstance().getSensorConfig().removeSensor(RPSLocation.getSLoc(e.getBlock().getLocation()));
			e.setCancelled(true);
		}else if(getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation().clone().add(0, 1, 0))))
		{
			sensor = true;
			loc = loc.clone().add(0, 1, 0);
			getInstance().getSensorConfig().removeSensor(RPSLocation.getSLoc(e.getBlock().getLocation().clone().add(0, 1, 0)));
		}
		if(sensor)
		{
			loc.getBlock().setType(Material.AIR);
			if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				loc.getWorld().dropItemNaturally(loc, getInstance().rps);
			}

		}

	}
	@EventHandler
	public void SensorPlaced(BlockPlaceEvent e)
	{
		// Check if item has a display name.
		if(!(e.getItemInHand() != null && e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName())) return;
		//Check if item is a RP Sensor.
		if((!e.getItemInHand().getItemMeta().getDisplayName().equals(getInstance().rps.getItemMeta().getDisplayName()))) return;
		if((e.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.REDSTONE_TORCH_OFF)) || (e.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.REDSTONE_TORCH_ON))) return;
		if(e.getPlayer().hasPermission("rps.place"))
		{
			getInstance().getSensorConfig().addSensor(RPSLocation.getRPSLoc(e.getBlock().getLocation()), e.getPlayer().getUniqueId(), UUID.randomUUID());	
		}else
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_restriction_place"));
		}
	}
	public RedstoneProximitySensor getInstance()
	{
		return RedstoneProximitySensor.getInstance();
	}
}
