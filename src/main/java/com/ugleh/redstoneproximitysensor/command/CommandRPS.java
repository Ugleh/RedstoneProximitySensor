package com.ugleh.redstoneproximitysensor.command;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.util.RPS;

import java.util.Map.Entry;
import java.util.UUID;

import com.ugleh.redstoneproximitysensor.util.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandRPS implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String rawCommand, String[] args) {
        if (args.length == 0) return notEnoughArgs(sender);
        else return enoughArgs(sender, args);
    }

    private boolean enoughArgs(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            return reloadCommand(sender);
        } else if (args[0].equalsIgnoreCase("give")) {
        	return giveCommand(sender, args);
        } else if (args[0].equalsIgnoreCase("ignore")) {
            return ignoreCommand(sender);
        }else if(args[0].equalsIgnoreCase("list")) {
            return grabSensors(sender, args);
        }
		return true;
    }


    private boolean notAllowed(CommandSender sender) {
		sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_restriction_permission_command"));
		return false;
	}

	private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }

    private boolean notPlayerNotification(CommandSender sender) {
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().langStringColor("lang_command_consolesender"));
        return false;
    }	
    
    private boolean notEnoughArgs(CommandSender sender) {
        String prefixColor = prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE);
        sender.sendMessage(prefixColor + "Redstone Proximity Sensor - Version: " + ChatColor.GREEN + this.getInstance().getVersion());
        sender.sendMessage(prefixColor + getInstance().getLang().get("lang_command_invalidargs"));
        if(sender.hasPermission("rps.admin")) sender.sendMessage(prefixColor + "/rps reload ~ Reload all config files.");
        if(sender.hasPermission("rps.invisible")) sender.sendMessage(prefixColor + "/rps ignore ~ Toggle Ignore RPS Sensors.");
        if(sender.hasPermission("rps.admin")) sender.sendMessage(prefixColor + "/rps give <player> [amount] ~ Give sensor to player.");
        if(sender.hasPermission("rps.list")) sender.sendMessage(prefixColor + "/rps list [player] ~ Lists all of your RPS sensors, or a specific players.");
		return false;

    }
    
	private boolean reloadCommand(CommandSender sender) {
        if(!sender.hasPermission("rps.admin")) return notAllowed(sender);
        getInstance().getgConfig().reloadConfig();
        getInstance().getLanguageConfig().reload();
        getInstance().getSensorConfig().reload();
        sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + getInstance().getLang().get("lang_command_reload"));
        return true;
    }
	
    private boolean giveCommand(CommandSender sender, String[] args) {
        if(!sender.hasPermission("rps.admin")) return notAllowed(sender);
        if (args.length <= 1) return notEnoughArgs(sender);
        Player givePlayer = Bukkit.getPlayer(args[1]);
        if (givePlayer == null) return playerUnknown(sender);
        if (args.length > 2) {
            //Give amount
            int amount = Integer.parseInt(args[2]);
            if (amount > 0) {
                ItemStack rpsStack = getInstance().rpsItemStack.clone();
                rpsStack.setAmount(amount);
                givePlayer.getInventory().addItem(rpsStack);
                sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + "Given RPS * " + amount + " to " + givePlayer.getName());
            } else {
                sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_command_wrongamount"));
            }
        } else {
            //Give 1
            ItemStack rpsStack = getInstance().rpsItemStack.clone();
            rpsStack.setAmount(1);
            givePlayer.getInventory().addItem(rpsStack);
            sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + "Given RPS * 1 to " + givePlayer.getName());
        }
        return true;
	}
    
	private boolean ignoreCommand(CommandSender sender) {
        if (!(sender instanceof Player)) return notPlayerNotification(sender);
        if(!sender.hasPermission("rps.invisible")) return notAllowed(sender);

        Player p = (Player) sender;
		if (getInstance().playerListener.rpsIgnoreList.contains(p.getUniqueId())) {
			//Toggle off
			getInstance().playerListener.rpsIgnoreList.remove(p.getUniqueId());
			sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().langStringColor("lang_command_invisibledisabled"));
		}else {
			//Toggle on
			getInstance().playerListener.rpsIgnoreList.add(p.getUniqueId());
			sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + getInstance().langStringColor("lang_command_invisibleenabled"));
        }
		return true;
    }

    private boolean grabSensors(CommandSender sender, String[] args) {
        if(!sender.hasPermission("rps.list")) return notAllowed(sender);
        if (args.length == 1)
            return grabSelfSensors(sender);
        else if (args.length == 2)
            return grabOthersSensors(sender, args);
        return true;
    }

    private boolean grabOthersSensors(CommandSender sender, String[] args) {
    	if(!sender.hasPermission("rps.list.player"))
    	    return notAllowed(sender);
        OfflinePlayer grabbedPlayer = Bukkit.getPlayer(args[1]);
        if(grabbedPlayer == null) {
            UUID uuid = UUIDFetcher.getUUID(args[1]);
            if(Bukkit.getOfflinePlayer(uuid).hasPlayedBefore()) {
                grabbedPlayer = Bukkit.getOfflinePlayer(uuid);
            }
        }
    	if (grabbedPlayer == null) return playerUnknown(sender);
    	return returnSensorList(sender, grabbedPlayer);
	}
    
	private boolean grabSelfSensors(CommandSender sender) {
		if(!(sender instanceof Player)) return notPlayerNotification(sender);
		Player player = (Player)sender;
		return returnSensorList(sender, player);
	}

	private boolean returnSensorList(CommandSender sendTo, OfflinePlayer grabFrom) {
		//boolean sendToIsPlayer = (sendTo instanceof Player);
		sendTo.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + grabFrom.getName() + "'s Redstone Proximity Sensors:");
		
		int asc = 1;
		for(Entry<String, RPS> sensors : getInstance().getSensorConfig().getSensorList().entrySet())
		{
			RPS rps = sensors.getValue();
			if(rps.getOwner().equals(grabFrom.getUniqueId())) {
				Location loc = rps.getLocation();
				sendTo.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + asc + ". W: " + loc.getWorld().getName() + " | X: " + (int)loc.getX() + ", Y: " + (int)loc.getY() + ", Z: " + (int)loc.getZ());
				asc++;
			}
		}
		return true;
	}
	
	private boolean playerUnknown(CommandSender sender) {
		sender.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + getInstance().getLang().get("lang_command_playerunknown"));
		return false;
	}

    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
        return (getInstance().chatPrefix + getInstance().getColor(colorNode));
    }
}
