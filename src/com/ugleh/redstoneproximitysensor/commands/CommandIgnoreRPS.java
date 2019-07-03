package com.ugleh.redstoneproximitysensor.commands;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandIgnoreRPS implements CommandExecutor {
    RedstoneProximitySensor plugin;

    public CommandIgnoreRPS(RedstoneProximitySensor plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String prefix, String[] args) {
        if (!(sender instanceof Player)) return notPlayerNotification(sender);
        if (!sender.hasPermission("rps.invisible")) return false;
        Player p = (Player) sender;
        if (RedstoneProximitySensor.getInstance().playerListener.rpsIgnoreList.contains(p.getUniqueId())) {
            //Toggle off
            RedstoneProximitySensor.getInstance().playerListener.rpsIgnoreList.remove(p.getUniqueId());
            sender.sendMessage(RedstoneProximitySensor.getInstance().chatPrefix + RedstoneProximitySensor.getInstance().langString("lang_command_invisibledisabled"));

        } else {
            //Toggle on
            RedstoneProximitySensor.getInstance().playerListener.rpsIgnoreList.add(p.getUniqueId());
            sender.sendMessage(RedstoneProximitySensor.getInstance().chatPrefix + ChatColor.GREEN + RedstoneProximitySensor.getInstance().langString("lang_command_invisibleenabled"));

        }
        return true;
    }

    private boolean notPlayerNotification(CommandSender sender) {
        sender.sendMessage(RedstoneProximitySensor.getInstance().chatPrefix + ChatColor.RED + RedstoneProximitySensor.getInstance().langString("lang_command_consolesender"));
        return false;
    }

}
