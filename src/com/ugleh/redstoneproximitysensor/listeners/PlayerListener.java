package com.ugleh.redstoneproximitysensor.listeners;

import java.util.ArrayList;
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
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.utils.Trigger;
import com.ugleh.redstoneproximitysensor.utils.RPS;
import com.ugleh.redstoneproximitysensor.utils.RPSLocation;
public class PlayerListener implements Listener
{
	public Inventory guiMenu;
	private String invName;
	private ItemStack invertedButton;
	private ItemStack ownerOnlyEditButton;
	private ItemStack rangeButton;
	
	private ItemStack copyButton;
	private ItemStack pasteButton;

	private List<Trigger> triggers = new ArrayList<Trigger>();
	public List<UUID> rpsIgnoreList = new ArrayList<UUID>();
	private HashMap<String, String> permList = new HashMap<String, String>();
	private HashMap<UUID, RPS> userSelectedRPS = new HashMap<UUID, RPS>();
	private HashMap<UUID, RPS> userCopiedRPS = new HashMap<UUID, RPS>();
	private HashMap<UUID, Inventory> userSelectedInventory = new HashMap<UUID, Inventory>();
	public static PlayerListener instance;
	public int menuSize = 27;
	
	public PlayerListener()
	{
		instance = this;
		invName = ChatColor.BLUE + langString("lang_main_inventoryname");
		createMenu();
	}
	
	public PlayerListener PL()
	{
		return instance;
	}
	
	private void createItem(ItemStack button, String perm, String langString, List<String> lore, int slot)
	{
		ItemMeta itemMeta = button.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + langString(langString));
		permList.put(itemMeta.getDisplayName(), perm);
		itemMeta.setLore(lore);
		button.setItemMeta(itemMeta);
		guiMenu.setItem(slot, button);
	}
	private void createMenu()
	{
		guiMenu = Bukkit.createInventory(null, menuSize, invName);
		
		//Setting button, Invert Power
		List<String> lore = WordWrapLore(langString("lang_button_invertpower_lore"));
		createItem(invertedButton = new ItemStack(Material.WOOL, 1, (short) 14), "button_invertpower", "lang_button_invertpower", lore, 0);
		
		//Setting button, Range
		lore = WordWrapLore(langString("lang_button_r_lore"));
		createItem(rangeButton = new ItemStack(Material.COMPASS, getInstance().getgConfig().getDefaultRange()), "button_range", "lang_button_range", lore, 1);		
		
		//Setting button, Owner Only Edit
		lore = WordWrapLore(langString("lang_button_ooe_lore"));
		createItem(ownerOnlyEditButton = new ItemStack(Material.NAME_TAG, 1), "button_owneronlyedit", "lang_button_owneronlyedit", lore, 2);		
		
		//Setting button, Copy Settings Button
		lore = WordWrapLore(langString("lang_button_c_lore"));
		createItem(setCopyButton(new ItemStack(Material.PAPER, 1)), "button_copy", "lang_button_copy", lore, 9);		

		//Setting button, Copy Settings Button
		lore = WordWrapLore(langString("lang_button_p_lore"));
		createItem(setPasteButton(new ItemStack(Material.INK_SACK, 1)), "button_paste", "lang_button_paste", lore, 10);		

		//Trigger button, Owner Trigger
		lore = WordWrapLore(langString("lang_button_ot_lore"));
		addTrigger(new Trigger("button_ownertrigger", new ItemStack(Material.SKULL_ITEM, 1, (short)3), 4, "lang_button_ownertrigger", "OWNER", "lang_button_true", "lang_button_false", lore));

		//Trigger button, Player Entity Trigger
		lore = WordWrapLore(langString("lang_button_pet1_lore"));
		addTrigger(new Trigger("button_playerentitytrigger", new ItemStack(Material.DIAMOND_SWORD, 1), 5, "lang_button_playerentitytrigger", "PLAYER", "lang_button_true", "lang_button_false", lore));
		
		//Trigger button, Hostile Entity Trigger
		lore = WordWrapLore(langString("lang_button_het_lore"));
		addTrigger(new Trigger("button_hostileentitiestrigger", new ItemStack(Material.SKULL_ITEM, 1, (short)2), 6, "lang_button_hostileentitytrigger", "HOSTILE_ENTITY", "lang_button_true", "lang_button_false", lore));

		//Trigger button, Peaceful Entity Trigger
		lore = WordWrapLore(langString("lang_button_pet2_lore"));
		addTrigger(new Trigger("button_peacefulentitiestrigger", new ItemStack(Material.COOKED_BEEF, 1), 7, "lang_button_peacefulentitytrigger", "PEACEFUL_ENTITY", "lang_button_true", "lang_button_false", lore));		
		
		//Trigger button, Dropped Items Trigger
		lore = WordWrapLore(langString("lang_button_dit_lore"));
		addTrigger(new Trigger("button_droppeditemtrigger", new ItemStack(Material.PUMPKIN_SEEDS, 1), 8, "lang_button_droppeditemtrigger", "DROPPED_ITEM", "lang_button_true", "lang_button_false", lore));		
		
		//Trigger button, Invisible Entity Trigger
		lore = WordWrapLore(langString("lang_button_iet_lore"));
		addTrigger(new Trigger("button_invisibleentitiestrigger", new ItemStack(Material.FERMENTED_SPIDER_EYE, 1), 17, "lang_button_invisibleentitytrigger", "INVISIBLE_ENTITY", "lang_button_true", "lang_button_false", lore));		

		//Trigger button, Invisible Entity Trigger
		lore = WordWrapLore(langString("lang_button_pt_lore"));
		addTrigger(new Trigger("button_projectiletrigger", new ItemStack(Material.ARROW, 1), 16, "lang_button_projectiletrigger", "PROJECTILE_ENTITY", "lang_button_true", "lang_button_false", lore));		

		//Trigger button, Invisible Entity Trigger
		lore = WordWrapLore(langString("lang_button_vt_lore"));
		addTrigger(new Trigger("button_vehicletrigger", new ItemStack(Material.MINECART, 1), 15, "lang_button_vehicletrigger", "VEHICLE_ENTITY", "lang_button_true", "lang_button_false", lore));		
	}
	


	public List<String> WordWrapLore(String string)
	{
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
		if(e.getPlayer().isOp() && getInstance().needsUpdate)
		{
			e.getPlayer().sendMessage(getInstance().chatPrefix + ChatColor.RED + langString("lang_update_notice"));
			e.getPlayer().sendMessage(getInstance().chatPrefix + ChatColor.GREEN + "https://www.spigotmc.org/resources/17965/");

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
		if(!e.getWhoClicked().hasPermission("rps.menu")) return;
		if(!(e.getInventory().getName().equals(invName) || e.getClickedInventory().getName().equals(invName))) return;
		RPS selectedRPS = userSelectedRPS.get(e.getWhoClicked().getUniqueId());
		
		//We understand that the inventory we are currently in is the RPS Menu so lets cancel any future click events now.
		e.setCancelled(true);
		//If it isn't a left click or a right click just return, we dont want players trying to do anything else but L or R click.
        if(!(e.isRightClick() || e.isLeftClick())) return;
		
		
		//Check if user clicked a menu item.
		if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName())
		{
			Trigger selectedTrigger = null;
			for(Trigger t : this.triggers)
			{
				if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith(t.getItem().getItemMeta().getDisplayName()))
				{
					selectedTrigger = t;

				}
			}
			String perm = null;
			if(selectedTrigger != null)
			{
				perm = selectedTrigger.getPerm();
			}else
			{
				for(String s : permList.keySet())
				{
					if(e.getCurrentItem().getItemMeta().getDisplayName().startsWith(s))
					{
						perm = permList.get(s);
						break;
					}
				}
			}
			String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
			Player playerWhoClicked = (Player) e.getWhoClicked();
			if(perm != null && (!playerWhoClicked.hasPermission("rps." + perm)))
			{
				playRejectSound(playerWhoClicked);
				playerWhoClicked.sendMessage(getInstance().chatPrefix + langString("lang_restriction_permission"));
				return;
			}
			if(displayName.startsWith(ChatColor.BLUE + langString("lang_button_invertpower")))
			{
				//Invert Power
				playToggleSound(playerWhoClicked);
				getInstance().getSensorConfig().setInverted(selectedRPS, !selectedRPS.isInverted());
			}else if(displayName.startsWith(ChatColor.BLUE + langString("lang_button_owneronlyedit")))
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
			}else if(displayName.startsWith(ChatColor.BLUE + langString("lang_button_range")))
			{
				//Range
				int newRange = selectedRPS.getRange();
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
			}else if(displayName.startsWith(ChatColor.BLUE + langString("lang_button_copy")))
			{
				this.userCopiedRPS.put(playerWhoClicked.getUniqueId(), selectedRPS);
				playToggleSound(playerWhoClicked);
				playerWhoClicked.sendMessage(getInstance().chatPrefix + langString("lang_button_c_reply"));
			}else if(displayName.startsWith(ChatColor.BLUE + langString("lang_button_paste")))
			{
				if(this.userCopiedRPS.containsKey(playerWhoClicked.getUniqueId()))
				{
					selectedRPS.pasteSettings(this.userCopiedRPS.get(playerWhoClicked.getUniqueId()));
					playToggleSound(playerWhoClicked);
					playerWhoClicked.sendMessage(getInstance().chatPrefix + langString("lang_button_p_reply"));
				}else
				{
					playRejectSound(playerWhoClicked);
					playerWhoClicked.sendMessage(getInstance().chatPrefix + langString("lang_restriction_paste"));
				}
			}
			
			for(Trigger t : this.triggers)
			{
				if(displayName.startsWith(ChatColor.BLUE + t.getDisplayNamePrefix()))
				{
					playToggleSound(playerWhoClicked);
					getInstance().getSensorConfig().toggleAcceptedEntities(selectedRPS, t);
					break;
				}
			}
			showGUIMenu((Player)playerWhoClicked, selectedRPS);

		}
	}
	

	private void playRejectSound(Player p)
	{
		try {
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
			} catch (NoSuchFieldError error) {
				p.playSound(p.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.5F, 0.3F);
			}	}
	
	private void playToggleSound(Player p)
	{
		try {
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1F);
			} catch (NoSuchFieldError error) {
				p.playSound(p.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.5F, 1F);
			}
	}

	@EventHandler
	public void DisplayMenuEvent(PlayerInteractEvent e)
	{
		if(e.getClickedBlock() == null) return;
		try {
			if(e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
			} catch (NoSuchMethodError error) {
			  // ignore (older version)
			}
		Location l = e.getClickedBlock().getLocation();
		Player p = e.getPlayer();
		
		//Check if player is right clicking a block
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		//User Right clicked an RPS
		if(!(getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(l)))) return;
		e.setCancelled(true);
		RPS selectedRPS = getInstance().getSensorConfig().getSensorList().get(RPSLocation.getSLoc(l));
		if(((selectedRPS.getOwner().equals(p.getUniqueId())) && selectedRPS.isownerOnlyEdit()) || (!selectedRPS.isownerOnlyEdit()))
		{
			showGUIMenu(p, selectedRPS);
		}else{
			p.sendMessage(getInstance().chatPrefix + langString("lang_restriction_owneronly"));
		}
	}

	private void showGUIMenu(Player p, RPS selectedRPS)
	{
		
		userSelectedRPS.put(p.getUniqueId(), selectedRPS);
		if(!userSelectedInventory.containsKey(p.getUniqueId()))
		{Inventory tempMenu = Bukkit.createInventory(null, menuSize, invName);
		tempMenu.setContents(guiMenu.getContents());
		userSelectedInventory.put(p.getUniqueId(), tempMenu);	
		}
		SetupInvertedButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupOwnerOnlyEditButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupRangeButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		SetupAcceptedEntitiesButtons(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
		p.openInventory(userSelectedInventory.get(p.getUniqueId()));
	}

	private void SetupOwnerOnlyEditButton(Inventory tempInv, RPS selectedRPS)
	{
		toggleButton(tempInv, ownerOnlyEditButton, selectedRPS.isownerOnlyEdit(), langString("lang_button_owneronlyedit") + ": ", 2);
	}

	private void toggleButton(Inventory tempInv, ItemStack button, boolean buttonStatus, String buttonText, int slot)
	{
		ItemMeta itemMeta = button.getItemMeta();
		if(buttonStatus)
		{
			itemMeta.addEnchant(getInstance().glow, 1, true);
			String suffix = langString("lang_button_true");
			suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
			itemMeta.setDisplayName(ChatColor.BLUE + buttonText + ChatColor.GREEN + suffix);
		}
		else
		{
			itemMeta.removeEnchant(getInstance().glow);
			String suffix = langString("lang_button_false");
			suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
			itemMeta.setDisplayName(ChatColor.BLUE + buttonText + ChatColor.RED + suffix);
		}
		button.setItemMeta(itemMeta);
		tempInv.setItem(slot, button);
	}

	private void SetupAcceptedEntitiesButtons(Inventory tempInv, RPS selectedRPS)
	{

		for(Trigger b : this.triggers)
		{
			b.updateButtonStatus(selectedRPS, tempInv);
		}
	}
	
	public void addTrigger(Trigger trigger)
	{
		this.triggers.add(trigger);
	}
	private void SetupRangeButton(Inventory tempInv, RPS selectedRPS)
	{
		rangeButton.setAmount(selectedRPS.getRange());
		ItemMeta rangeBMeta = rangeButton.getItemMeta();
		rangeBMeta.setDisplayName(ChatColor.BLUE + langString("lang_button_range") + ": " + ChatColor.GOLD + selectedRPS.getRange());
		rangeButton.setItemMeta(rangeBMeta);
		tempInv.setItem(1, rangeButton);

	}

	private void SetupInvertedButton(Inventory tempInv, RPS selectedRPS)
	{
		ItemMeta tempIBMeta = invertedButton.getItemMeta();
		if(selectedRPS.isInverted())
		{
			invertedButton.setDurability((short)7);
			tempIBMeta.setDisplayName(ChatColor.BLUE + langString("lang_button_invertpower") + ": " + ChatColor.GRAY + langString("lang_button_inverted"));
		}else
		{
			invertedButton.setDurability((short)14);
			tempIBMeta.setDisplayName(ChatColor.BLUE + langString("lang_button_invertpower") + ": " + ChatColor.RED + langString("lang_button_notinverted"));
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


	public ItemStack getCopyButton() {
		return copyButton;
	}
	
	public List<Trigger> getTriggers()
	{
		return triggers;
	}
	public ItemStack setCopyButton(ItemStack copyButton)
	{
		this.copyButton = copyButton;
		return copyButton;
	}


	public ItemStack getPasteButton()
	{
		return pasteButton;
	}


	public ItemStack setPasteButton(ItemStack pasteButton)
	{
		this.pasteButton = pasteButton;
		return pasteButton;
	}
}
