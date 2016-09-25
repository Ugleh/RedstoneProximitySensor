package com.ugleh.redstoneproximitysensor.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class CommandRPS implements CommandExecutor {
	public String chatPrefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "RPS" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED ;
	private static RedstoneProximitySensor plugin = RedstoneProximitySensor.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String rawCommand, String[] args) {
		if(args.length == 0) notEnoughArgs(sender);
		if(args.length >= 1) enoughArgs(sender, args);
		return false;
	}

	private void enoughArgs(CommandSender sender, String[] args) {
		if(args[0].equalsIgnoreCase("reload"))
		{
			plugin.getgConfig().reloadConfig();
			sender.sendMessage(chatPrefix + "Reload complete.");
		}else if(args[0].equalsIgnoreCase("give"))
		{
			if( args.length > 1 )
			{
				Player givePlayer = Bukkit.getPlayer(args[1]);
				if(givePlayer == null)
				{
					sender.sendMessage(chatPrefix + "Player not found.");
				}else
				{
					if(args.length > 2)
					{
						//Give amount
						int amount = Integer.parseInt(args[2]);
						if(amount > 0)
						{
							ItemStack rpsStack = plugin.rps.clone();
							rpsStack.setAmount(amount);
							givePlayer.getInventory().addItem(rpsStack);
							sender.sendMessage(chatPrefix + "Given RPS * " +amount+" to" + givePlayer.getName());

						}else
						{
							sender.sendMessage(chatPrefix + "Amount can not be less than 1");
						}
					}else
					{
						//Give 1
						ItemStack rpsStack = plugin.rps.clone();
						rpsStack.setAmount(1);
						givePlayer.getInventory().addItem(rpsStack);
						sender.sendMessage(chatPrefix + "Given RPS * 1 to" + givePlayer.getName());

					}
				}
			}else
			{
				notEnoughArgs(sender);
			}
		}

	}

	private void notEnoughArgs(CommandSender sender) {
		sender.sendMessage(chatPrefix + "Redstone Proximity Sensor - Version: " + ChatColor.GREEN + this.plugin.getVersion());
		sender.sendMessage(chatPrefix + "Not enough Arguments.");
		sender.sendMessage(chatPrefix + "/rps reload");
		sender.sendMessage(chatPrefix + "/rps give <player> [amount]");

	}

}
