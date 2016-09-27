package com.ugleh.redstoneproximitysensor.utils;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class Trigger {
	private String flagName;
	private ItemStack item;
	private List<String> lore;
	private String displayNamePrefix;
	private int slot;

	
	private Glow glow;
	private String suffixOne;
	private String suffixTwo;
	
	public Trigger(Inventory guiMenu, ItemStack itemStack, int slot, String nameNode, String flag, String suffixOne, String suffixTwo, List<String> lore, Glow glow) {
		this.item = itemStack;
		this.displayNamePrefix = langString(nameNode) + ": ";
		this.lore = lore;
		this.flagName = flag;
		this.slot = slot;
		this.glow = glow;
		this.lore = lore;
		this.suffixOne = langString(suffixOne);
		this.suffixOne = this.suffixOne.substring(0, 1).toUpperCase() + this.suffixOne.substring(1);
		this.suffixTwo = langString(suffixTwo);
		this.suffixTwo = this.suffixTwo.substring(0, 1).toUpperCase() + this.suffixTwo.substring(1);

		
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + this.displayNamePrefix);
		itemMeta.setLore(this.lore);
		itemStack.setItemMeta(itemMeta);
		guiMenu.setItem(slot, itemStack);

	}




	public void toggleButton(RPS selectedRPS, Inventory tempInv) {
		ItemMeta itemMeta = item.getItemMeta();
		if(selectedRPS.getAcceptedEntities().contains(flagName))
		{
			itemMeta.addEnchant(glow, 1, true);
			itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + ChatColor.GREEN + suffixOne);
		}else
		{
			itemMeta.removeEnchant(glow);
			itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + ChatColor.RED + suffixTwo);
		}
		item.setItemMeta(itemMeta);
		tempInv.setItem(slot, item);
	}
	
	public String langString(String key)
	{
		return RedstoneProximitySensor.getInstance().getLang().get(key);
	}
	public String getDisplayNamePrefix() {
		return displayNamePrefix;
	}
	
	public String getFlag() {
		return flagName;
	}
}
