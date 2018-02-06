package com.ugleh.redstoneproximitysensor.utils;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerAddons;
import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;

import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Trigger {
	private String flagName;
	private ItemStack item;
	private List<String> lore;
	private String displayNamePrefix;
	private int slot;
	private String perm;
	
	private Glow glow;
	private String suffixOne;
	private String suffixTwo;
	
	public Trigger(String trigger_permission, ItemStack button_material, int slot_number, String button_title, String sensor_flag, String toggle_off, String toggle_on, List<String> loreTextWrapped) {
		PlayerListener pl = PlayerListener.instance;
		this.item = button_material;
		this.displayNamePrefix = langString(button_title) + ": ";
		this.lore = loreTextWrapped;
		this.flagName = sensor_flag;
		this.slot = slot_number;
		this.glow = RedstoneProximitySensor.getInstance().glow;
		String suffixOnePre = langString(toggle_off);
		this.suffixOne = suffixOnePre.substring(0, 1).toUpperCase() + suffixOnePre.substring(1);
		String suffixTwoPre = langString(toggle_on);
		this.suffixTwo = suffixTwoPre.substring(0, 1).toUpperCase() + suffixTwoPre.substring(1);
		this.perm = trigger_permission;

		
		ItemMeta itemMeta = button_material.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + this.displayNamePrefix);
		itemMeta.setLore(this.lore);
		button_material.setItemMeta(itemMeta);
		pl.guiMenu.setItem(slot, button_material);

	}
	
	public Trigger(String trigger_permission, ItemStack button_material, String button_title, String sensor_flag, String toggle_off, String toggle_on, List<String> loreTextWrapped) {
		PlayerListener pl = RedstoneProximitySensor.getInstance().playerListener;
		TriggerAddons ta = RedstoneProximitySensor.getInstance().getTriggerAddons();
		
		
		this.item = button_material;
		this.displayNamePrefix = langString(button_title) + ": ";
		this.lore = loreTextWrapped;
		this.flagName = sensor_flag;
		this.slot = ta.getSlot();
		this.glow = RedstoneProximitySensor.getInstance().glow;
		String suffixOnePre = langString(toggle_off);
		this.suffixOne = suffixOnePre.substring(0, 1).toUpperCase() + suffixOnePre.substring(1);
		String suffixTwoPre = langString(toggle_on);
		this.suffixTwo = suffixTwoPre.substring(0, 1).toUpperCase() + suffixTwoPre.substring(1);
		this.perm = trigger_permission;

		
		ItemMeta itemMeta = button_material.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + this.displayNamePrefix);
		itemMeta.setLore(this.lore);
		button_material.setItemMeta(itemMeta);
		pl.guiMenu.setItem(slot, button_material);

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
	
	private String langString(String key)
	{
		if(RedstoneProximitySensor.getInstance().getLang().containsKey(key))
		{
			return RedstoneProximitySensor.getInstance().getLang().get(key);

		}else
		{
			return key;
		}
	}
	public String getDisplayNamePrefix() {
		return displayNamePrefix;
	}
	
	public String getFlag() {
		return flagName;
	}
	public ItemStack getItem()
	{
		return item;
	}




	public String getPerm() {
		return perm;
	}
}
