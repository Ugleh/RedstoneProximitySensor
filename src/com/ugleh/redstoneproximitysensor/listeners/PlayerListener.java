package com.ugleh.redstoneproximitysensor.listeners;

import java.util.Arrays;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.utils.Glow;
import com.ugleh.redstoneproximitysensor.utils.RPS;
import com.ugleh.redstoneproximitysensor.utils.RPSLocation;
public class PlayerListener implements Listener {
	private Inventory guiMenu;
	private String invName;
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
	public PlayerListener()
	{
		invName = ChatColor.BLUE + langString("lang_main_inventoryname");
		glow = new Glow(1234);
		createMenu();
	}
	
	
	private void createItem(ItemStack button, String langString, List<String> lore, int slot) {
		ItemMeta itemMeta = button.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + langString(langString));
		itemMeta.setLore(lore);
		button.setItemMeta(itemMeta);
		guiMenu.setItem(slot, button);
	}
	private void createMenu() {
		guiMenu = Bukkit.createInventory(null, 18, invName);
		
		List<String> lore = WordWrapLore("Toggle the sensors power to be inverted when triggered.");
		createItem(invertedButton = new ItemStack(Material.WOOL, 1, (short) 14), "lang_button_invertpower", lore, 0);
		
		lore = WordWrapLore("Decides if only the owner should be able to set off this sensor.");
		createItem(ownerOnlyTriggerButton = new ItemStack(Material.SKULL_ITEM, 1, (short)3), "lang_button_owneronlytrigger", lore, 4);		
		
		lore = WordWrapLore("Left Click to increase range, Right Click to decrease range.");
		createItem(rangeButton = new ItemStack(Material.COMPASS, getInstance().getgConfig().getDefaultRange()), "lang_button_range", lore, 1);		
		
		lore = WordWrapLore("Click to toggle if only the owner should be able to edit this, or everyone.");
		createItem(ownerOnlyEditButton = new ItemStack(Material.NAME_TAG, 1), "lang_button_owneronlyedit", lore, 2);		
		
		lore = WordWrapLore("Click to have the RPS trigger from Player Entities.");
		createItem(playerEntitiesAllowed = new ItemStack(Material.DIAMOND_SWORD, 1), "lang_button_playerentitytrigger", lore, 5);
		
		lore = WordWrapLore("Click to have the RPS trigger from Hostile Entities.");
		createItem(hostileEntitiesAllowed = new ItemStack(Material.SKULL_ITEM, 1, (short)2), "lang_button_hostileentitytrigger", lore, 6);		

		lore = WordWrapLore("Click to have the RPS trigger from Peaceful Entities.");
		createItem(peacefulEntitiesAllowed = new ItemStack(Material.COOKED_BEEF, 1), "lang_button_peacefulentitytrigger", lore, 7);		
		
		lore = WordWrapLore("Click to have the RPS trigger from Dropped Items.");
		createItem(droppedItemsAllowed = new ItemStack(Material.PUMPKIN_SEEDS, 1), "lang_button_droppeditemtrigger", lore, 8);		
		
		lore = WordWrapLore("Click to have invisible entities trigger the RPS.");
		createItem(invisibleEntsAllowed = new ItemStack(Material.POTION, 1), "lang_button_invisibleentitytrigger", lore, 17);		
	}
	


	private List<String> WordWrapLore(String string) {
		StringBuilder sb = new StringBuilder(string);

		int i = 0;
		while (i + 35 < sb.length() && (i = sb.lastIndexOf(" ", i + 35)) != -1) {
		    sb.replace(i, i + 1, "\n");
		}
		return Arrays.asList(sb.toString().split("\n"));
		
	}


	@EventHandler
	public void CraftItemEvent(CraftItemEvent e)
	{
		ItemStack result = e.getRecipe().getResult();
		if(!(result != null && result.hasItemMeta() && result.getItemMeta().hasDisplayName())) return;
		//Check if item is a RP Sensor.
		if((!result.getItemMeta().getDisplayName().equals(getInstance().rps.getItemMeta().getDisplayName()))) return;
		
		if(!e.getWhoClicked().hasPermission("rps.create"))
		{
			e.setResult(Result.DENY);
			e.setCancelled(true);
			e.getWhoClicked().sendMessage(getInstance().chatPrefix + langString("lang_restriction_craft"));
		}
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e)
	{
		if(e.getPlayer().isOp())
		{
			e.getPlayer().sendMessage(ChatColor.RED + getInstance().chatPrefix + langString("lang_update_notice"));
			e.getPlayer().sendMessage(ChatColor.GREEN + "https://www.spigotmc.org/resources/17965/");

		}
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e)
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
				playerWhoClicked.sendMessage(getInstance().chatPrefix + langString("lang_restriction_permission"));
				return;
			}
			if(displayName.startsWith(ChatColor.BLUE + "Invert Power: "))
			{
				//Invert Power
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().setInverted(selectedRPS, !selectedRPS.isInverted());
			}else if(displayName.startsWith(ChatColor.BLUE + "Owner Only Trigger: "))
			{
				//Owner Only Trigger
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().setownerOnlyTrigger(selectedRPS, !selectedRPS.isownerOnlyTrigger());
			}else if(displayName.startsWith(ChatColor.BLUE + "Owner Only Edit: "))
			{
				//Owner Only Trigger
				if(selectedRPS.getOwner().equals(playerWhoClicked.getUniqueId()))
				{
					playToggleSound(playerWhoClicked);
					getInstance().getSensorConfig().setownerOnlyEdit(selectedRPS, !selectedRPS.isownerOnlyEdit());
				}else{
					playRejectSound(playerWhoClicked);
					playerWhoClicked.sendMessage(getInstance().chatPrefix + langString("lang_restriction_owneronly_button"));
				}
			}else if(displayName.startsWith(ChatColor.BLUE + "Range"))
			{
				//Range
				int newRange = 0;
				if(e.getClick().isLeftClick())
				{
					playToggleSound(playerWhoClicked);
					newRange = (selectedRPS.getRange()+1) > getInstance().getgConfig().getMaxRange() ? 1 : selectedRPS.getRange()+1;
				}else if(e.getClick().isRightClick())
				{
					playToggleSound(playerWhoClicked);
					newRange = (selectedRPS.getRange()-1) < 1 ? getInstance().getgConfig().getMaxRange() : selectedRPS.getRange()-1;
				}
				getInstance().getSensorConfig().setRange(selectedRPS, newRange);
			}else if(displayName.startsWith(ChatColor.BLUE + "Player Entity"))
			{
				//Player Entity Trigger
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().addAcceptedEntity(selectedRPS, "PLAYER");
			}else if(displayName.startsWith(ChatColor.BLUE + "Dropped Item"))
			{
				//Dropped Item Trigger
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().addAcceptedEntity(selectedRPS, "DROPPED_ITEM");
			}else if(displayName.startsWith(ChatColor.BLUE + "Hostile Entities"))
			{
				//Hostile Entity Trigger
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().addAcceptedEntity(selectedRPS, "HOSTILE_ENTITY");
			}else if(displayName.startsWith(ChatColor.BLUE + "Peaceful Entities"))
			{
				//Peaceful Entity Trigger
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().addAcceptedEntity(selectedRPS, "PEACEFUL_ENTITY");
			}else if(displayName.startsWith(ChatColor.BLUE + "Invisible Entities"))
			{
				//Invisible Entity Trigger
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().addAcceptedEntity(selectedRPS, "INVISIBLE_ENTITY");
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
		if(!(getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(l)))) return;
		RPS selectedRPS = getInstance().getSensorConfig().getSensorList().get(RPSLocation.getSLoc(l));
		if(((selectedRPS.getOwner().equals(p.getUniqueId())) && selectedRPS.isownerOnlyEdit()) || (!selectedRPS.isownerOnlyEdit()))
		{
			showGUIMenu(p, selectedRPS);
		}else{
			p.sendMessage(getInstance().chatPrefix + langString("lang_restriction_owneronly"));
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
			itemMeta.setDisplayName(ChatColor.BLUE + buttonText + ChatColor.GREEN + langString("lang_button_true"));
		}else
		{
			itemMeta.removeEnchant(glow);
			itemMeta.setDisplayName(ChatColor.BLUE + buttonText + ChatColor.RED + langString("lang_button_false"));
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
			tempOOMeta.setDisplayName(ChatColor.BLUE + "Owner Only Trigger: " + ChatColor.GREEN + langString("lang_button_true"));
		}else
		{
			tempOOMeta.removeEnchant(glow);
			tempOOMeta.setDisplayName(ChatColor.BLUE + "Owner Only Trigger: " + ChatColor.RED + langString("lang_button_false"));
		}
		ownerOnlyTriggerButton.setItemMeta(tempOOMeta);
		tempInv.setItem(4, ownerOnlyTriggerButton);
		
	}

	private void SetupInvertedButton(Inventory tempInv, RPS selectedRPS) {
		ItemMeta tempIBMeta = invertedButton.getItemMeta();
		if(selectedRPS.isInverted())
		{
			invertedButton.setDurability((short)7);
			tempIBMeta.setDisplayName(ChatColor.BLUE + "Invert Power: " + ChatColor.GRAY + langString("lang_button_inverted"));
		}else
		{
			invertedButton.setDurability((short)14);
			tempIBMeta.setDisplayName(ChatColor.BLUE + "Invert Power: " + ChatColor.RED + langString("lang_button_notinverted"));
		}
		
		invertedButton.setItemMeta(tempIBMeta);
		tempInv.setItem(0, invertedButton);
	}
	
	public RedstoneProximitySensor getInstance()
	{
		return RedstoneProximitySensor.getInstance();
	}
	
	public String langString(String key)
	{
		return getInstance().getLang().get(key);
	}
}
