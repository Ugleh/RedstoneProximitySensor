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
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class SensorListener implements Listener {

    @EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent e) {
        if (!((e.getBlock().getType().equals(Material.REDSTONE_TORCH)) || (e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH))))
            return;
        if (getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(e.getBlock().getLocation()))) {
            e.setNewCurrent(e.getOldCurrent());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSensorBreak(BlockBreakEvent e) {
        Location location = e.getBlock().getLocation();
        SensorConfig sc = getInstance().getSensorConfig();
        boolean sensor = false;
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
                Objects.requireNonNull(location.getWorld(), "World cannot be null").dropItemNaturally(location, getInstance().rpsItemStack);
            }

        }

    }

    //TODO: Figure out how to make this work.
/*    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        SensorConfig sc = getInstance().getSensorConfig();
        for(Block b : e.blockList()) {
            if(sc.getSensorList().containsKey(RPSLocation.getSLoc(b.getLocation()))) {
                b.getDrops().clear();
            }
        }
    }*/

    @EventHandler
    public void onSensorPlaced(BlockPlaceEvent e) {
    	Player player = e.getPlayer();
        ItemStack itemInHand = e.getItemInHand();
        // Check if item is a RP Sensor.
        if(!(itemInHand.hasItemMeta() && Objects.requireNonNull(itemInHand.getItemMeta(), "ItemMeta can not be null.").hasDisplayName() && itemInHand.isSimilar(getInstance().rpsItemStack))) return;
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
                        player.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().playerListener.langString("lang_button_p_reply"));
                    } else {
                    	getInstance().playerListener.playRejectSound(player);
                        player.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().playerListener.langString("lang_restriction_paste"));
                    }
                }
        	}else {
        		//Do not add Sensor, let them know why.
                e.setCancelled(true);
                player.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_restriction_place_limit"));
        	}
        } else {
            //Do not add Sensor, let them know why.
            e.setCancelled(true);
            player.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_restriction_place"));
        }
    }

    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
        return (getInstance().chatPrefix + getInstance().getColor(colorNode));
    }

    public RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
