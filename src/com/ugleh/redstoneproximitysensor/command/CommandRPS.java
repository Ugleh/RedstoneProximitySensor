package com.ugleh.redstoneproximitysensor.command;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.util.RPS;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandRPS implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String rawCommand, String[] args) {
        if (args.length == 0) notEnoughArgs(sender);
        else if (args.length >= 1) enoughArgs(sender, args);
        return false;
    }

    private boolean enoughArgs(CommandSender sender, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
        	if(!sender.hasPermission("rps.admin")) return notAllowed(sender);
        	reloadCommand(sender, args);
        	
        } else if (args[0].equalsIgnoreCase("give")) {
        	if(!sender.hasPermission("rps.admin")) return notAllowed(sender);
        	giveCommand(sender, args);
        } else if (args[0].equalsIgnoreCase("ignore")) {
            if (!(sender instanceof Player)) return notPlayerNotification(sender);
        	if(!sender.hasPermission("rps.invisible")) return notAllowed(sender);
        	ignoreCommand(sender, args);
        }else if(args[0].equalsIgnoreCase("list")) {
        	if(!sender.hasPermission("rps.list")) return notAllowed(sender);
            if (args.length == 1) return grabSelfSensors(sender);
            else if (args.length == 2) return grabOthersSensors(sender, args);

        }
		return true;

    }
    
	
	private boolean notAllowed(CommandSender sender) {
		sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_restriction_permission_command"));
		return false;
	}

	private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }

    private boolean notPlayerNotification(CommandSender sender) {
        sender.sendMessage(getInstance().chatPrefix + ChatColor.RED + getInstance().langString("lang_command_consolesender"));
        return false;
    }	
    
    private boolean notEnoughArgs(CommandSender sender) {
        sender.sendMessage(getInstance().chatPrefix + "Redstone Proximity Sensor - Version: " + ChatColor.GREEN + this.getInstance().getVersion());
        sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_invalidargs"));
        if(sender.hasPermission("rps.admin")) sender.sendMessage(getInstance().chatPrefix + "/rps reload ~ Reload all config files.");
        if(sender.hasPermission("rps.invisible")) sender.sendMessage(getInstance().chatPrefix + "/rps ignore ~ Toggle Ignore RPS Sensors.");
        if(sender.hasPermission("rps.admin")) sender.sendMessage(getInstance().chatPrefix + "/rps give <player> [amount] ~ Give sensor to player.");
        if(sender.hasPermission("rps.list")) sender.sendMessage(getInstance().chatPrefix + "/rps list [player] ~ Lists all of your RPS sensors, or a specific players.");
		return false;

    }
    
	private void reloadCommand(CommandSender sender, String[] args) {
        getInstance().getgConfig().reloadConfig();
        getInstance().getLanguageConfig().reload();
        getInstance().getSensorConfig().reload();
        sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_reload"));
	}
	
    private boolean giveCommand(CommandSender sender, String[] args) {
        if (args.length <= 1) return notEnoughArgs(sender);
            Player givePlayer = Bukkit.getPlayer(args[1]);
            if (givePlayer == null) return playerUnknown(sender);
                if (args.length > 2) {
                    //Give amount
                    int amount = Integer.parseInt(args[2]);
                    if (amount > 0) {
                        ItemStack rpsStack = getInstance().rps.clone();
                        rpsStack.setAmount(amount);
                        givePlayer.getInventory().addItem(rpsStack);
                        sender.sendMessage(getInstance().chatPrefix + "Given RPS * " + amount + " to " + givePlayer.getName());

                    } else {
                        sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_wrongamount"));
                    }
                } else {
                    //Give 1
                    ItemStack rpsStack = getInstance().rps.clone();
                    rpsStack.setAmount(1);
                    givePlayer.getInventory().addItem(rpsStack);
                    sender.sendMessage(getInstance().chatPrefix + "Given RPS * 1 to " + givePlayer.getName());

                }
                
        return true;
	}
    
	private void ignoreCommand(CommandSender sender, String[] args) {
		Player p = (Player) sender;
		if (getInstance().playerListener.rpsIgnoreList.contains(p.getUniqueId())) {
			//Toggle off
			getInstance().playerListener.rpsIgnoreList.remove(p.getUniqueId());
			sender.sendMessage(getInstance().chatPrefix + getInstance().langString("lang_command_invisibledisabled"));
		}else {
			//Toggle on
			getInstance().playerListener.rpsIgnoreList.add(p.getUniqueId());
			sender.sendMessage(getInstance().chatPrefix + ChatColor.GREEN + getInstance().langString("lang_command_invisibleenabled"));
        }
    }
	
    private boolean grabOthersSensors(CommandSender sender, String[] args) {
    	if(!sender.hasPermission("rps.list.player")) return notAllowed(sender);
    	Player grabbedPlayer = Bukkit.getPlayer(args[1]);
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
		sendTo.sendMessage(getInstance().chatPrefix + grabFrom.getName() + "'s Redstone Proximity Sensors:");
		
		int asc = 1;
		for(Entry<String, RPS> sensors : getInstance().getSensorConfig().getSensorList().entrySet())
		{
			RPS rps = sensors.getValue();
			if(rps.getOwner().equals(grabFrom.getUniqueId())) {
				Location loc = rps.getLocation();
				sendTo.sendMessage(getInstance().chatPrefix + asc + ". W: " + loc.getWorld().getName() + " | X: " + (int)loc.getX() + ", Y: " + (int)loc.getY() + ", Z: " + (int)loc.getZ());
				asc++;
			}
		}
		return true;
	}
	
	private boolean playerUnknown(CommandSender sender) {
		sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_playerunknown"));
		return false;
	}
}
