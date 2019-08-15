package com.ugleh.redstoneproximitysensor.command;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandIgnoreRPS implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String prefix, String[] args) {
        if (!(sender instanceof Player)) return notPlayerNotification(sender);
        if (!sender.hasPermission("rps.invisible")) return false;
        Player p = (Player) sender;
        if (getInstance().playerListener.rpsIgnoreList.contains(p.getUniqueId())) {
            //Toggle off
            getInstance().playerListener.rpsIgnoreList.remove(p.getUniqueId());
            sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().langStringColor("lang_command_invisibledisabled"));

        } else {
            //Toggle on
            getInstance().playerListener.rpsIgnoreList.add(p.getUniqueId());
            sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + getInstance().langStringColor("lang_command_invisibleenabled"));

        }
        return true;
    }

    private boolean notPlayerNotification(CommandSender sender) {
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().langStringColor("lang_command_consolesender"));
        return false;
    }
    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
        return (getInstance().chatPrefix + getInstance().getColor(colorNode));
    }
}
