package com.ugleh.redstoneproximitysensor.command;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.util.RPS;

public class CommandRPSList implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command command, String rawCommand, String[] args) {
        if (args.length == 0) grabSelfSensors(sender);
        else if (args.length == 1) grabOthersSensors(sender, args);
        else if (args.length > 1) notEnoughArgs(sender);
        return false;
    }
	
    private boolean grabOthersSensors(CommandSender sender, String[] args) {
    	if(!sender.hasPermission("rps.list.player")) return notAllowed(sender);
    	Player grabbedPlayer = Bukkit.getPlayer(args[0]);

    	if (grabbedPlayer == null) return playerUnknown(sender);
    	return returnSensorList(sender, grabbedPlayer);
	}
    
	private boolean grabSelfSensors(CommandSender sender) {
		if(!(sender instanceof Player)) return notPlayerNotification(sender);
		Player player = (Player)sender;
		return returnSensorList(sender, player);
	}

	private boolean returnSensorList(CommandSender sendTo, Player grabFrom) {
		//boolean sendToIsPlayer = (sendTo instanceof Player);
		sendTo.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE)  + grabFrom.getName() + "'s Redstone Proximity Sensors:");
		
		int asc = 1;
		for(Entry<String, RPS> sensors : getInstance().getSensorConfig().getSensorList().entrySet())
		{
			RPS rps = sensors.getValue();
			if(rps.getOwner().equals(grabFrom.getUniqueId())) {
				Location loc = rps.getLocation();
				sendTo.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE)  + asc + ". W: " + loc.getWorld().getName() + " | X: " + (int)loc.getX() + ", Y: " + (int)loc.getY() + ", Z: " + (int)loc.getZ());
				asc++;
			}
		}
		return true;
	}

	private boolean playerUnknown(CommandSender sender) {
		sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_command_playerunknown"));
		return false;
	}

	private boolean notAllowed(CommandSender sender) {
		sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_restriction_permission_command"));
		return false;
	}

	private void notEnoughArgs(CommandSender sender) {
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + "Redstone Proximity Sensor - Version: " + ChatColor.GREEN + getInstance().getVersion());
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().getLang().get("lang_command_invalidargs"));
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + "/rpslist [player]");

    }
	
    private boolean notPlayerNotification(CommandSender sender) {
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().langStringColor("lang_command_consolesender"));
        return false;
    }

    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
		return (getInstance().chatPrefix + getInstance().getColor(colorNode));
	}
    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
