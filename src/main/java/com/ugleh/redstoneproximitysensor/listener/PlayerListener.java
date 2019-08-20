package com.ugleh.redstoneproximitysensor.listener;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.trigger.*;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.RPSLocation;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerListener implements Listener {
    public static PlayerListener instance;
    public Inventory guiMenu;
    public List<UUID> rpsIgnoreList = new ArrayList<>();
    public int menuSize = 27;
    private String invName;
    private ItemStack invertedButton;
    private ItemStack ownerOnlyEditButton;
    private ItemStack rangeButton;
    private List<Trigger> triggers = new ArrayList<>();
    private List<TriggerTemplate> triggerRunners = new ArrayList<>();
    private HashMap<String, String> permList = new HashMap<>();
    private HashMap<UUID, RPS> userSelectedRPS = new HashMap<>();
    HashMap<UUID, RPS> userCopiedRPS = new HashMap<>();
    public HashMap<UUID, Inventory> userSelectedInventory = new HashMap<>();

    public PlayerListener() {
        instance = this;
        invName = ChatColor.BLUE + langString("lang_main_inventoryname");
        createMenu();
    }

    private void createItem(ItemStack button, String perm, String langString, List<String> lore, int slot) {
        ItemMeta itemMeta = button.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + langString(langString));
        permList.put(itemMeta.getDisplayName(), perm);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        button.setItemMeta(itemMeta);
        guiMenu.setItem(slot, button);
    }

    private void createMenu() {
        guiMenu = Bukkit.createInventory(null, menuSize, invName);

        //Setting button, Invert Power
        List<String> lore = WordWrapLore(langString("lang_button_invertpower_lore"));
        createItem(invertedButton = new ItemStack(Material.RED_WOOL, 1, (short) 14), "button_invertpower", "lang_button_invertpower", lore, 0);

        //Setting button, Range
        lore = WordWrapLore(langString("lang_button_r_lore"));
        createItem(rangeButton = new ItemStack(Material.COMPASS, getInstance().getgConfig().getDefaultRange()), "button_range", "lang_button_range", lore, 1);

        //Setting button, Owner Only Edit
        lore = WordWrapLore(langString("lang_button_ooe_lore"));
        createItem(ownerOnlyEditButton = new ItemStack(Material.NAME_TAG, 1), "button_owneronlyedit", "lang_button_owneronlyedit", lore, 2);

        //Setting button, Copy Settings Button
        lore = WordWrapLore(langString("lang_button_c_lore"));
        createItem(new ItemStack(Material.PAPER, 1), "button_copy", "lang_button_copy", lore, 9);

        //Setting button, Copy Settings Button
        lore = WordWrapLore(langString("lang_button_p_lore"));
        createItem(new ItemStack(Material.INK_SAC, 1), "button_paste", "lang_button_paste", lore, 10);

        //Initiation of Triggers
        triggerRunners.add(new DroppedItem(this));
        triggerRunners.add(new HostileEntity(this));
        triggerRunners.add(new InvisibleEntity(this));
        triggerRunners.add(new NeutralEntity(this));
        triggerRunners.add(new Owner(this));
        triggerRunners.add(new PeacefulEntity(this));
        triggerRunners.add(new PlayerEntity(this));
        triggerRunners.add(new ProjectileEntity(this));
        triggerRunners.add(new VehicleEntity(this));
        //triggerRunners.add(new IndividualMob(this));
        triggerRunners.add(new Others(this));
    }


    public List<String> WordWrapLore(String rawString) {
        return RedstoneProximitySensor.getInstance().wordWrapLore(rawString);
    }


    @EventHandler
    public void CraftItemEvent(CraftItemEvent e) {
        ItemStack result = e.getRecipe().getResult();
        if (!(result.hasItemMeta() && result.getItemMeta().hasDisplayName())) return;
        //Check if item is a RP Sensor.
        if ((!result.getItemMeta().getDisplayName().equals(getInstance().rpsItemStack.getItemMeta().getDisplayName()))) return;

        if (!e.getWhoClicked().hasPermission("rps.create")) {
            e.setResult(Result.DENY);
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + langString("lang_restriction_craft"));
        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOp() && getInstance().needsUpdate) {
            player.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + langString("lang_update_notice"));
            player.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.POSITIVE_MESSAGE) + "https://www.spigotmc.org/resources/17965/");

        }
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {

        //Prevent Memory Leak
        userSelectedRPS.remove(e.getPlayer().getUniqueId());
        userSelectedInventory.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getWhoClicked().hasPermission("rps.menu")) return;
        if (!e.getView().getTitle().equals(invName)) return;
        RPS selectedRPS = userSelectedRPS.get(e.getWhoClicked().getUniqueId());

        //We understand that the inventory we are currently in is the RPS Menu so lets cancel any future click events now.
        e.setCancelled(true);
        //If it isn't a left click or a right click just return, we dont want players trying to do anything else but L or R click.
        if (!(e.isRightClick() || e.isLeftClick())) return;


        //Check if user clicked a menu item.
        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            Trigger selectedTrigger = null;
            for (Trigger t : this.triggers) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith(t.getItem().getItemMeta().getDisplayName())) {
                    selectedTrigger = t;

                }
            }
            String perm = null;
            if (selectedTrigger != null) {
                perm = selectedTrigger.getPerm();
            } else {
                for (String s : permList.keySet()) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith(s)) {
                        perm = permList.get(s);
                        break;
                    }
                }
            }
            String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
            Player playerWhoClicked = (Player) e.getWhoClicked();
            if (perm != null && (!playerWhoClicked.hasPermission("rps." + perm))) {
                playRejectSound(playerWhoClicked);
                playerWhoClicked.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + langString("lang_restriction_permission"));
                return;
            }
            if (displayName.startsWith(ChatColor.BLUE + langString("lang_button_invertpower"))) {
                //Invert Power
                playToggleSound(playerWhoClicked);
                getInstance().getSensorConfig().setInverted(selectedRPS, !selectedRPS.isInverted());
            } else if (displayName.startsWith(ChatColor.BLUE + langString("lang_button_owneronlyedit"))) {
                //Owner Only Trigger
                if (selectedRPS.getOwner().equals(playerWhoClicked.getUniqueId())) {
                    playToggleSound(playerWhoClicked);
                    getInstance().getSensorConfig().setOwnerOnlyEdit(selectedRPS, !selectedRPS.isOwnerOnlyEdit());
                } else {
                    playRejectSound(playerWhoClicked);
                    playerWhoClicked.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + langString("lang_restriction_owneronly_button"));
                }
            } else if (displayName.startsWith(ChatColor.BLUE + langString("lang_button_range"))) {
                //Range
                int newRange = selectedRPS.getRange();
                if (e.getClick().isLeftClick()) {
                    playToggleSound(playerWhoClicked);
                    newRange = (selectedRPS.getRange() + 1) > getInstance().getgConfig().getMaxRange() ? 1 : selectedRPS.getRange() + 1;
                } else if (e.getClick().isRightClick()) {
                    playToggleSound(playerWhoClicked);
                    newRange = (selectedRPS.getRange() - 1) < 1 ? getInstance().getgConfig().getMaxRange() : selectedRPS.getRange() - 1;
                }
                getInstance().getSensorConfig().setRange(selectedRPS, newRange);
            } else if (displayName.startsWith(ChatColor.BLUE + langString("lang_button_copy"))) {
                this.userCopiedRPS.put(playerWhoClicked.getUniqueId(), selectedRPS);
                playToggleSound(playerWhoClicked);
                playerWhoClicked.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + langString("lang_button_c_reply"));
            } else if (displayName.startsWith(ChatColor.BLUE + langString("lang_button_paste"))) {
                if (this.userCopiedRPS.containsKey(playerWhoClicked.getUniqueId())) {
                    selectedRPS.pasteSettings(this.userCopiedRPS.get(playerWhoClicked.getUniqueId()));
                    playToggleSound(playerWhoClicked);
                    playerWhoClicked.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + langString("lang_button_p_reply"));
                } else {
                    playRejectSound(playerWhoClicked);
                    playerWhoClicked.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + langString("lang_restriction_paste"));
                }
            }

            for (Trigger t : this.triggers) {
                if (displayName.startsWith(ChatColor.BLUE + t.getDisplayNamePrefix())) {
                    playToggleSound(playerWhoClicked);
                    getInstance().getSensorConfig().toggleAcceptedEntities(selectedRPS, t, playerWhoClicked);
                    break;
                }
            }
            showGUIMenu(playerWhoClicked, selectedRPS);

        }
    }


    void playRejectSound(Player p) {
        try {
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 0.3F);
        } catch (NoSuchFieldError error) {
            p.playSound(p.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.5F, 0.3F);
        }
    }

    void playToggleSound(Player p) {
        try {
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1F);
        } catch (NoSuchFieldError error) {
            p.playSound(p.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.5F, 1F);
        }
    }

    @EventHandler
    public void DisplayMenuEvent(PlayerInteractEvent e) {
    	if (e.getPlayer().isSneaking()) return;
        if (e.getClickedBlock() == null) return;
        if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Location l = e.getClickedBlock().getLocation();
        Player p = e.getPlayer();

        //Check if player is right clicking a block
        if (!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;

        //User Right clicked an RPS
        if (!(getInstance().getSensorConfig().getSensorList().containsKey(RPSLocation.getSLoc(l)))) return;

        e.setCancelled(true);
        RPS selectedRPS = getInstance().getSensorConfig().getSensorList().get(RPSLocation.getSLoc(l));

        // Second part of condition goes through because isOwnerOnlyEdit is TRUE, so we don't need to check if it is true again in second condition.
        if ((!selectedRPS.isOwnerOnlyEdit()) || (selectedRPS.getOwner().equals(p.getUniqueId()))) {
            showGUIMenu(p, selectedRPS);
        } else {
            p.sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + langString("lang_restriction_owneronly"));
        }
    }

    private void showGUIMenu(Player p, RPS selectedRPS) {
        userSelectedRPS.put(p.getUniqueId(), selectedRPS);
        if (!userSelectedInventory.containsKey(p.getUniqueId())) {
            Inventory tempMenu = Bukkit.createInventory(null, menuSize, invName);
            tempMenu.setContents(guiMenu.getContents());
            userSelectedInventory.put(p.getUniqueId(), tempMenu);
        }
        SetupInvertedButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
        SetupOwnerOnlyEditButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
        SetupRangeButton(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
        SetupAcceptedEntitiesButtons(userSelectedInventory.get(p.getUniqueId()), selectedRPS);
        p.openInventory(userSelectedInventory.get(p.getUniqueId()));
    }

    private void SetupOwnerOnlyEditButton(Inventory tempInv, RPS selectedRPS) {
        toggleButton(tempInv, ownerOnlyEditButton, selectedRPS.isOwnerOnlyEdit(), langString("lang_button_owneronlyedit") + ": ", 2);
    }

    private void toggleButton(Inventory tempInv, ItemStack button, boolean buttonStatus, String buttonText, int slot) {
        ItemMeta itemMeta = button.getItemMeta();
        if (buttonStatus) {
            button.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
            String suffix = langString("lang_button_true");
            suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
            Objects.requireNonNull(itemMeta, "ItemMeta doesn't exist for button.").setDisplayName(ChatColor.BLUE + buttonText + ChatColor.GREEN + suffix);
        } else {
            button.removeEnchantment(Enchantment.ARROW_DAMAGE);
            String suffix = langString("lang_button_false");
            suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1);
            Objects.requireNonNull(itemMeta, "ItemMeta doesn't exist for button.").setDisplayName(ChatColor.BLUE + buttonText + ChatColor.RED + suffix);
        }
        button.setItemMeta(itemMeta);
        tempInv.setItem(slot, button);
    }

    private void SetupAcceptedEntitiesButtons(Inventory tempInv, RPS selectedRPS) {

        for (Trigger b : this.triggers) {
            b.updateButtonStatus(selectedRPS, tempInv);
        }
    }

    public void addTrigger(Trigger trigger) {
        getInstance().getgConfig().setupPerm(trigger.getPerm());
        this.triggers.add(trigger);
    }

    private void SetupRangeButton(Inventory tempInv, RPS selectedRPS) {
        rangeButton.setAmount(selectedRPS.getRange());
        ItemMeta rangeBMeta = rangeButton.getItemMeta();
        Objects.requireNonNull(rangeBMeta, "ItemMeta doesn't exist for button.").setDisplayName(ChatColor.BLUE + langString("lang_button_range") + ": " + ChatColor.GOLD + selectedRPS.getRange());
        rangeButton.setItemMeta(rangeBMeta);
        tempInv.setItem(1, rangeButton);
    }

    private void SetupInvertedButton(Inventory tempInv, RPS selectedRPS) {
        ItemMeta tempIBMeta = invertedButton.getItemMeta();
        if (selectedRPS.isInverted()) {
            invertedButton.setType(Material.GRAY_WOOL);
            Objects.requireNonNull(tempIBMeta, "ItemMeta doesn't exist for button.").setDisplayName(ChatColor.BLUE + langString("lang_button_invertpower") + ": " + ChatColor.GRAY + langString("lang_button_inverted"));
        } else {
            invertedButton.setType(Material.RED_WOOL);
            Objects.requireNonNull(tempIBMeta, "ItemMeta doesn't exist for button.").setDisplayName(ChatColor.BLUE + langString("lang_button_invertpower") + ": " + ChatColor.RED + langString("lang_button_notinverted"));
        }

        invertedButton.setItemMeta(tempIBMeta);
        tempInv.setItem(0, invertedButton);
    }

    public RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }

    public String langString(String key) {
        return getInstance().getLang().get(key);
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public List<TriggerTemplate> getTriggerRunners() {
        return triggerRunners;
    }

    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
        return (getInstance().chatPrefix + getInstance().getColor(colorNode));
    }
}
