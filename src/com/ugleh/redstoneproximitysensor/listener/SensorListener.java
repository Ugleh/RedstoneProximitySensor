package com.ugleh.redstoneproximitysensor.listener;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.config.SensorConfig;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.RPSLocation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
        Location location = e.getBlock().getLocation();
        SensorConfig sc = getInstance().getSensorConfig();
        Boolean sensor = false;
        if (sc.getSensorList().containsKey(RPSLocation.getSLoc(location))) {
            sensor = true;
            sc.removeSensor(RPSLocation.getSLoc(location));
            e.setCancelled(true);
        } else if (sc.getSensorList().containsKey(RPSLocation.getSLoc(location.clone().add(0, 1, 0)))) {
            sensor = true;
            location = location.clone().add(0, 1, 0);
            sc.removeSensor(RPSLocation.getSLoc(e.getBlock().getLocation().clone().add(0, 1, 0)));
        }
        if (sensor) {
            location.getBlock().setType(Material.AIR);
            if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                location.getWorld().dropItemNaturally(location, getInstance().rps);
            }

        }

    }

    @EventHandler
    public void SensorPlaced(BlockPlaceEvent e) {
    	Player player = e.getPlayer();
        // Check if item has a display name.
        if (!(e.getItemInHand() != null && e.getItemInHand().hasItemMeta() && e.getItemInHand().getItemMeta().hasDisplayName()))
            return;
        //Check if item is a RP Sensor.
        if ((!e.getItemInHand().getItemMeta().getDisplayName().equals(getInstance().rps.getItemMeta().getDisplayName())))
            return;
        //Permission?
        if (player.hasPermission("rps.place")) {
        	if(getInstance().getSensorConfig().canPlaceLimiterCheck(player))
        	{
                //Add Sensor
                RPS sensor = getInstance().getSensorConfig().addSensor(RPSLocation.getRPSLoc(e.getBlock().getLocation()), player.getUniqueId(), UUID.randomUUID());
                //Player is sneaking, shift-place mode pastes settings into RPS as you place it.
                if(player.isSneaking()) {
                    if (getInstance().playerListener.userCopiedRPS.containsKey(player.getUniqueId())) {
                        sensor.pasteSettings(getInstance().playerListener.userCopiedRPS.get(player.getUniqueId()));
                        getInstance().playerListener.playToggleSound(player);
                        player.sendMessage(getInstance().chatPrefix + getInstance().playerListener.langString("lang_button_p_reply"));
                    } else {
                    	getInstance().playerListener.playRejectSound(player);
                        player.sendMessage(getInstance().chatPrefix + getInstance().playerListener.langString("lang_restriction_paste"));
                    }
                }
        	}else {
        		//Do not add Sensor, let them know why.
                e.setCancelled(true);
                player.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_restriction_place_limit"));
        	}
        } else {
            //Do not add Sensor, let them know why.
            e.setCancelled(true);
            player.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_restriction_place"));
        }
    }

    public RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
