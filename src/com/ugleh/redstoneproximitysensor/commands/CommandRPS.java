package com.ugleh.redstoneproximitysensor.commands;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandRPS implements CommandExecutor {
	private RedstoneProximitySensor plugin;
	public CommandRPS(RedstoneProximitySensor redstoneProximitySensor) {
		this.plugin = redstoneProximitySensor;
	}

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
			plugin.getLanguageConfig().reload();
			plugin.getSensorConfig().reload();
			sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_reload"));
		}else if(args[0].equalsIgnoreCase("give"))
		{
			if( args.length > 1 )
			{
				Player givePlayer = Bukkit.getPlayer(args[1]);
				if(givePlayer == null)
				{
					sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_playerunknown"));
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
							sender.sendMessage(getInstance().chatPrefix + "Given RPS * " +amount+" to " + givePlayer.getName());

						}else
						{
							sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_wrongamount"));
						}
					}else
					{
						//Give 1
						ItemStack rpsStack = plugin.rps.clone();
						rpsStack.setAmount(1);
						givePlayer.getInventory().addItem(rpsStack);
						sender.sendMessage(getInstance().chatPrefix + "Given RPS * 1 to " + givePlayer.getName());

					}
				}
			}else
			{
				notEnoughArgs(sender);
			}
		}
		
	}

	private RedstoneProximitySensor getInstance() {
		return RedstoneProximitySensor.getInstance();
	}

	private void notEnoughArgs(CommandSender sender) {
		sender.sendMessage(getInstance().chatPrefix + "Redstone Proximity Sensor - Version: " + ChatColor.GREEN + this.plugin.getVersion());
		sender.sendMessage(getInstance().chatPrefix + getInstance().getLang().get("lang_command_invalidargs"));
		sender.sendMessage(getInstance().chatPrefix + "/rps reload");
		sender.sendMessage(getInstance().chatPrefix + "/rps give <player> [amount]");
		
	}

}
