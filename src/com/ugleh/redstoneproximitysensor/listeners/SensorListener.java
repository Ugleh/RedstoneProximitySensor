package com.ugleh.redstoneproximitysensor.listeners;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.utils.RPS;
import com.ugleh.redstoneproximitysensor.utils.RPSLocation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.UUID;

public class SensorListener implements Listener {

    @EventHandler
    public void BlockRedstoneEvent(BlockRedstoneEvent e) {
        if (!((e.getBlock().getType().equals(Material.REDSTONE_TORCH)) || (e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH))))
            return;
        if (getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation()))) {
            e.setNewCurrent(e.getOldCurrent());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void SensorBroke(BlockBreakEvent e) {
        Location loc = e.getBlock().getLocation();
        Boolean sensor = false;
        if (getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation()))) {
            sensor = true;
            getInstance().getSensorConfig().removeSensor(RPSLocation.getSLoc(e.getBlock().getLocation()));
            e.setCancelled(true);
        } else if (getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation().clone().add(0, 1, 0)))) {
            sensor = true;
            loc = loc.clone().add(0, 1, 0);
            getInstance().getSensorConfig().removeSensor(RPSLocation.getSLoc(e.getBlock().getLocation().clone().add(0, 1, 0)));
        }
        if (sensor) {
            loc.getBlock().setType(Material.AIR);
            if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                loc.getWorld().dropItemNaturally(loc, getInstance().rps);
            }

        }

    }

    @EventHandler
    public void SensorPlaced(BlockPlaceEvent e) {
        // Check if item has a display name.
        if (!(e.getItemInHand() != null && e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName()))
            return;
        //Check if item is a RP Sensor.
        if ((!e.getItemInHand().getItemMeta().getDisplayName().equals(getInstance().rps.getItemMeta().getDisplayName())))
            return;
        //Permission?
        if (e.getPlayer().hasPermission("rps.place")) {
            //Add Sensor
            RPS sensor = getInstance().getSensorConfig().addSensor(RPSLocation.getRPSLoc(e.getBlock().getLocation()), e.getPlayer().getUniqueId(), UUID.randomUUID());
            //Player is sneaking, shift-place mode pastes settings into RPS as you place it.
            if(e.getPlayer().isSneaking()) {
                if (getInstance().playerListener.userCopiedRPS.containsKey(e.getPlayer().getUniqueId())) {
                    sensor.pasteSettings(getInstance().playerListener.userCopiedRPS.get(e.getPlayer().getUniqueId()));
                    getInstance().playerListener.playToggleSound(e.getPlayer());
                    e.getPlayer().sendMessage(getInstance().chatPrefix + getInstance().playerListener.langString("lang_button_p_reply"));
                } else {
                	getInstance().playerListener.playRejectSound(e.getPlayer());
                    e.getPlayer().sendMessage(getInstance().chatPrefix + getInstance().playerListener.langString("lang_restriction_paste"));
                }
            }
            
        } else {
            //Do not add Sensor, let them know why.
            e.setCancelled(true);
            e.getPlayer().sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_restriction_place"));
        }
    }

    public RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
