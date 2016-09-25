package com.ugleh.redstoneproximitysensor.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.utils.Glow;
import com.ugleh.redstoneproximitysensor.utils.RPS;
public class PlayerListener implements Listener {
	private static RedstoneProximitySensor plugin = RedstoneProximitySensor.getInstance();
	private Inventory guiMenu;
	private String invName = ChatColor.BLUE + "Redstone Proximity Sensor Menu";
	private ItemStack invertedButton;
	private ItemStack ownerOnlyTriggerButton;
	private ItemStack ownerOnlyEditButton;
	private ItemStack rangeButton;

	private ItemStack playerEntitiesAllowed;
	private ItemStack hostileEntitiesAllowed;
	private ItemStack peacefulEntitiesAllowed;
	private ItemStack droppedItemsAllowed;
	private ItemStack invisibleEntsAllowed;
	private HashMap<UUID, RPS> userSelectedRPS = new HashMap<UUID, RPS>();
	private HashMap<UUID, Inventory> userSelectedInventory = new HashMap<UUID, Inventory>();
	private Glow glow;
	private String chatPrefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "RPS" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED ;

    public PlayerListener()
	{
		glow = new Glow(1234);
		createMenu();
	}

	private void createMenu() {
		guiMenu = Bukkit.createInventory(null, 18, invName);

		invertedButton = new ItemStack(Material.WOOL, 1, (short) 14);
		ItemMeta invertedButtonMeta = invertedButton.getItemMeta();
		invertedButtonMeta.setDisplayName(ChatColor.BLUE + "Invert Power");
		List<String> lore = new ArrayList<String>();
		lore.add("Toggle the sensors power to be");
		lore.add("inverted when triggered.");
		invertedButtonMeta.setLore(lore);
		invertedButton.setItemMeta(invertedButtonMeta);
		guiMenu.setItem(0, invertedButton);

		ownerOnlyTriggerButton = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		ItemMeta ownerOnlyTriggerMeta = ownerOnlyTriggerButton.getItemMeta();
		ownerOnlyTriggerMeta.setDisplayName(ChatColor.BLUE + "Owner Only Trigger");
		List<String> lore2 = new ArrayList<String>();
		lore2.add("Decides if only the owner should");
		lore2.add("be able to set off this sensor.");
		ownerOnlyTriggerMeta.setLore(lore2);
		ownerOnlyTriggerButton.setItemMeta(ownerOnlyTriggerMeta);
		guiMenu.setItem(4, ownerOnlyTriggerButton);

		rangeButton = new ItemStack(Material.COMPASS, 5);
		ItemMeta rangeBMeta = rangeButton.getItemMeta();
		rangeBMeta.setDisplayName(ChatColor.BLUE + "Range");
		List<String> lore3 = new ArrayList<String>();
		lore3.add("Left Click to increase range.");
		lore3.add("Right Click to decrease range.");
		rangeBMeta.setLore(lore3);
		rangeButton.setItemMeta(rangeBMeta);
		guiMenu.setItem(1, rangeButton);

		ownerOnlyEditButton = new ItemStack(Material.NAME_TAG, 1);
		ItemMeta ooeMeta = ownerOnlyEditButton.getItemMeta();
		ooeMeta.setDisplayName(ChatColor.BLUE + "Owner Only Edit");
		lore3.clear();
		lore3.add("Click to toggle if only the owner should be able to edit this, or everyone.");
		ooeMeta.setLore(lore3);
		ownerOnlyEditButton.setItemMeta(ooeMeta);
		guiMenu.setItem(2, ownerOnlyEditButton);

		List<String> tempLore = new ArrayList<String>();
		playerEntitiesAllowed = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta peaMeta = playerEntitiesAllowed.getItemMeta();
		peaMeta.setDisplayName(ChatColor.BLUE + "Player Entity Trigger");
		tempLore.add("Click to have the RPS trigger from Player Entities.");
		peaMeta.setLore(tempLore);
		tempLore.clear();
		playerEntitiesAllowed.setItemMeta(peaMeta);
		guiMenu.setItem(5, playerEntitiesAllowed);

		hostileEntitiesAllowed = new ItemStack(Material.SKULL_ITEM, 1, (short)2);
		ItemMeta heaMeta = hostileEntitiesAllowed.getItemMeta();
		heaMeta.setDisplayName(ChatColor.BLUE + "Hostile Entities Trigger");
		tempLore.add("Click to have the RPS trigger from Hostile Entities.");
		heaMeta.setLore(tempLore);
		tempLore.clear();
		hostileEntitiesAllowed.setItemMeta(heaMeta);
		guiMenu.setItem(6, hostileEntitiesAllowed);

		peacefulEntitiesAllowed = new ItemStack(Material.COOKED_BEEF, 1);
		ItemMeta pea2Meta = peacefulEntitiesAllowed.getItemMeta();
		pea2Meta.setDisplayName(ChatColor.BLUE + "Peaceful Entities Trigger");
		tempLore.add("Click to have the RPS trigger from Peaceful Entities.");
		pea2Meta.setLore(tempLore);
		tempLore.clear();
		peacefulEntitiesAllowed.setItemMeta(pea2Meta);
		guiMenu.setItem(7, peacefulEntitiesAllowed);

		droppedItemsAllowed = new ItemStack(Material.PUMPKIN_SEEDS, 1);
		ItemMeta diaMeta = droppedItemsAllowed.getItemMeta();
		diaMeta.setDisplayName(ChatColor.BLUE + "Dropped Items Trigger");
		tempLore.add("Click to have the RPS trigger from Dropped Items.");
		diaMeta.setLore(tempLore);
		tempLore.clear();
		droppedItemsAllowed.setItemMeta(diaMeta);
		guiMenu.setItem(8, droppedItemsAllowed);

		invisibleEntsAllowed = new ItemStack(Material.POTION, 1);
		ItemMeta ieaMeta = invisibleEntsAllowed.getItemMeta();
		ieaMeta.setDisplayName(ChatColor.BLUE + "Invisible Entities Trigger");
		tempLore.add("Click to have invisible entities trigger the RPS.");
		ieaMeta.setLore(tempLore);
		tempLore.clear();
		invisibleEntsAllowed.setItemMeta(ieaMeta);
		guiMenu.setItem(17, invisibleEntsAllowed);

	}


	@EventHandler
	public void CraftItemEvent(CraftItemEvent e)
	{
		ItemStack result = e.getRecipe().getResult();
		if(!(result != null && result.hasItemMeta() && result.getItemMeta().hasDisplayName())) return;
		//Check if item is a RP Sensor.
		if((!result.getItemMeta().getDisplayName().equals(ChatColor.RED + "Redstone Proximity Sensor"))) return;

		if(!e.getWhoClicked().hasPermission("rps.create"))
		{
			e.setResult(Result.DENY);
			e.setCancelled(true);
			e.getWhoClicked().sendMessage(chatPrefix + "You do not have permission to craft that.");
		}
	}

	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent e)
	{

		//Prevent Memory Leak
		userSelectedRPS.remove(e.getPlayer().getUniqueId());
		userSelectedInventory.remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void InventoryClickEvent(InventoryClickEvent e)
	{
		if(e.getClickedInventory() == null) return;
		if(!(e.getInventory().getName().equals(invName) || e.getClickedInventory().getName().equals(invName))) return;
		RPS selectedRPS = userSelectedRPS.get(e.getWhoClicked().getUniqueId());

		e.setCancelled(true);


		if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName())
		{
			//Clicked a menu item
			String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
			String newString = displayName.replace(ChatColor.BLUE.toString(), "");
			newString = newString.toLowerCase().replace(" ", "");
			String[] testS = newString.split(":");
			String permString = "rps.button_" + testS[0];
			Player playerWhoClicked = (Player) e.getWhoClicked();
			if((!playerWhoClicked.hasPermission(permString)))
			{
				playRejectSound(playerWhoClicked);
				playerWhoClicked.sendMessage(chatPrefix + "You do not have permissions to use this modifier.");
				return;
			}
			if(displayName.startsWith(ChatColor.BLUE + "Invert Power: "))
			{
				//Invert Power
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().setInverted(selectedRPS, !selectedRPS.isInverted());
			}else if(displayName.startsWith(ChatColor.BLUE + "Owner Only Trigger: "))
			{
				//Owner Only Trigger
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().setownerOnlyTrigger(selectedRPS, !selectedRPS.isownerOnlyTrigger());
			}else if(displayName.startsWith(ChatColor.BLUE + "Owner Only Edit: "))
			{
				//Owner Only Trigger
				if(selectedRPS.getOwner().equals(playerWhoClicked.getUniqueId()))
				{
					playToggleSound(playerWhoClicked);
					plugin.getSensorConfig().setownerOnlyEdit(selectedRPS, !selectedRPS.isownerOnlyEdit());
				}else{
					playRejectSound(playerWhoClicked);
					playerWhoClicked.sendMessage(chatPrefix + "Only the owner can modify that setting.");
				}
			}else if(displayName.startsWith(ChatColor.BLUE + "Range"))
			{
				//Range
				int newRange = 0;
				if(e.getClick().isLeftClick())
				{
					playToggleSound(playerWhoClicked);
					newRange = (selectedRPS.getRange()+1) > plugin.getgConfig().getMaxRange() ? 1 : selectedRPS.getRange()+1;
				}else if(e.getClick().isRightClick())
				{
					playToggleSound(playerWhoClicked);
					newRange = (selectedRPS.getRange()-1) < 1 ? plugin.getgConfig().getMaxRange() : selectedRPS.getRange()-1;
				}
				plugin.getSensorConfig().setRange(selectedRPS, newRange);
			}else if(displayName.startsWith(ChatColor.BLUE + "Player Entity"))
			{
				//Player Entity Trigger
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().addAcceptedEntity(selectedRPS, "PLAYER");
			}else if(displayName.startsWith(ChatColor.BLUE + "Dropped Item"))
			{
				//Dropped Item Trigger
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().addAcceptedEntity(selectedRPS, "DROPPED_ITEM");
			}else if(displayName.startsWith(ChatColor.BLUE + "Hostile Entities"))
			{
				//Hostile Entity Trigger
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().addAcceptedEntity(selectedRPS, "HOSTILE_ENTITY");
			}else if(displayName.startsWith(ChatColor.BLUE + "Peaceful Entities"))
			{
				//Peaceful Entity Trigger
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().addAcceptedEntity(selectedRPS, "PEACEFUL_ENTITY");
			}else if(displayName.startsWith(ChatColor.BLUE + "Invisible Entities"))
			{
				//Invisible Entity Trigger
				playToggleSound(playerWhoClicked);
				plugin.getSensorConfig().addAcceptedEntity(selectedRPS, "INVISIBLE_ENTITY");
			}

			showGUIMenu((Player)playerWhoClicked, selectedRPS);

		}
	}


	private void playRejectSound(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
	}

	private void playToggleSound(Player p) {
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1F);
	}

	@EventHandler
	public void DisplayMenuEvent(PlayerInteractEvent e)
	{
		if(e.getClickedBlock() == null) return;
		Location l = e.getClickedBlock().getLocation();
		Player p = e.getPlayer();

		//Check if player is right clicking a block
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		//User Right clicked an RPS
		if(!(plugin.getSensorConfig().getSensorList().containsKey(l))) return;
		RPS selectedRPS = plugin.getSensorConfig().getSensorList().get(l);
		if(((selectedRPS.getOwner().equals(p.getUniqueId())) && selectedRPS.isownerOnlyEdit()) || (!selectedRPS.isownerOnlyEdit()))
		{
			showGUIMenu(p, selectedRPS);
		}else{
			p.sendMessage(chatPrefix + "This RPS can only be modified by its owner.");
		}
	}

	private void showGUIMenu(Player p, RPS selectedRPS) {
		userSelectedRPS.put(p.getUniqueId(), selectedRPS);
		if(!userSelectedInventory.containsKey(p.getUniqueId()))
		{Inventory tempMenu = Bukkit.createInventory(null, 18, invName);
		tempMenu.setContents(guiMenu.getContents());
		userSelectedInventory.put(p.getUniqueId(), tempMenu);
		}
		SetupInvertedButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupownerOnlyTriggerButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupOwnerOnlyEditButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupRangeButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupAcceptedEntitiesButtons(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		p.openInventory(userSelectedInventory.get(p.getUniqueId()));
	}

	private void SetupOwnerOnlyEditButton(Inventory tempInv, RPS selectedRPS) {
		//ItemMeta ooeMeta = ownerOnlyEditButton.getItemMeta();
		toggleButton(tempInv, ownerOnlyEditButton, selectedRPS.isownerOnlyEdit(), "Owner Only Edit: ", 2);
	}

	private void toggleButton(Inventory tempInv, ItemStack button, boolean buttonStatus, String buttonText, int slot) {
		ItemMeta itemMeta = button.getItemMeta();
		if(buttonStatus)
		{
			itemMeta.addEnchant(glow, 1, true);
			itemMeta.setDisplayName(ChatColor.BLUE + buttonText + ChatColor.GREEN + "True");
		}else
		{
			itemMeta.removeEnchant(glow);
			itemMeta.setDisplayName(ChatColor.BLUE + buttonText + ChatColor.RED + "False");
		}
		button.setItemMeta(itemMeta);
		tempInv.setItem(slot, button);
	}

	private void SetupAcceptedEntitiesButtons(Inventory tempInv, RPS selectedRPS) {

		toggleButton(tempInv, playerEntitiesAllowed, selectedRPS.getAcceptedEntities().contains("PLAYER"), "Player Entity Trigger: ", 5);
		toggleButton(tempInv, hostileEntitiesAllowed, selectedRPS.getAcceptedEntities().contains("HOSTILE_ENTITY"), "Hostile Entities Trigger: ", 6);
		toggleButton(tempInv, peacefulEntitiesAllowed, selectedRPS.getAcceptedEntities().contains("PEACEFUL_ENTITY"), "Peaceful Entities Trigger: ", 7);
		toggleButton(tempInv, droppedItemsAllowed, selectedRPS.getAcceptedEntities().contains("DROPPED_ITEM"), "Dropped Item Trigger: ", 8);
		toggleButton(tempInv, invisibleEntsAllowed, selectedRPS.getAcceptedEntities().contains("INVISIBLE_ENTITY"), "Invisible Entities Trigger: ", 17);
	}

	private void SetupRangeButton(Inventory tempInv, RPS selectedRPS) {
		rangeButton.setAmount(selectedRPS.getRange());
		ItemMeta rangeBMeta = rangeButton.getItemMeta();
		rangeBMeta.setDisplayName(ChatColor.BLUE + "Range: " + ChatColor.GOLD + selectedRPS.getRange());
		rangeButton.setItemMeta(rangeBMeta);
		tempInv.setItem(1, rangeButton);

	}

	private void SetupownerOnlyTriggerButton(Inventory tempInv, RPS selectedRPS) {
		ItemMeta tempOOMeta = ownerOnlyTriggerButton.getItemMeta();
		if(selectedRPS.isownerOnlyTrigger())
		{
			tempOOMeta.addEnchant(glow, 1, true);
			tempOOMeta.setDisplayName(ChatColor.BLUE + "Owner Only Trigger: " + ChatColor.GREEN + "True");
		}else
		{
			tempOOMeta.removeEnchant(glow);
			tempOOMeta.setDisplayName(ChatColor.BLUE + "Owner Only Trigger: " + ChatColor.RED + "False");
		}
		ownerOnlyTriggerButton.setItemMeta(tempOOMeta);
		tempInv.setItem(4, ownerOnlyTriggerButton);

	}

	private void SetupInvertedButton(Inventory tempInv, RPS selectedRPS) {
		ItemMeta tempIBMeta = invertedButton.getItemMeta();
		if(selectedRPS.isInverted())
		{
			invertedButton.setDurability((short)7);
			tempIBMeta.setDisplayName(ChatColor.BLUE + "Invert Power: " + ChatColor.GRAY + "Inverted");
		}else
		{
			invertedButton.setDurability((short)14);
			tempIBMeta.setDisplayName(ChatColor.BLUE + "Invert Power: " + ChatColor.RED + "Not Inverted");
		}

		invertedButton.setItemMeta(tempIBMeta);
		tempInv.setItem(0, invertedButton);
	}
}
