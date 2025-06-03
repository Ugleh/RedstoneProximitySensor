package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Trigger{
    public TriggerTemplate addonTemplate;
    private String flagName;
    private ItemStack item;
    private List<String> lore;
    private String displayNamePrefix;
    private int slot;
    private String perm;
    private String suffixOne;
    private String suffixTwo;

    public Trigger(String triggerPermission, ItemStack buttonMaterial, int slotNumber, String buttonTitle, String sensorFlag, String toggleOn, String toggleOff, List<String> loreTextWrapped, TriggerTemplate addon) {
        PlayerListener pl = PlayerListener.instance;
        setFields(
                buttonMaterial
                , langString(buttonTitle)
                , loreTextWrapped
                , sensorFlag
                , slotNumber
                , toggleOn
                , toggleOff
                , triggerPermission
                , addon
        );

        setupButtonMetaData();
        pl.guiMenu.setItem(this.slot, this.item);

    }

    public Trigger(String triggerPermission, ItemStack buttonMaterial, String buttonTitle, String sensorFlag, String toggleOn, String toggleOff, List<String> loreTextWrapped, TriggerTemplate addon) {
        PlayerListener pl = RedstoneProximitySensor.getInstance().playerListener;
        TriggerCreator triggerCreator = TriggerCreator.getInstance();

        setFields(
                buttonMaterial
                , langString(buttonTitle) + ": "
                , loreTextWrapped
                , sensorFlag
                , triggerCreator.getAvailableSlot()
                , toggleOn
                , toggleOff
                , triggerPermission
                , addon
        );

        setupButtonMetaData();
        pl.guiMenu.setItem(slot, buttonMaterial);

    }

    private void setupButtonMetaData() {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.BLUE + this.displayNamePrefix);
            itemMeta.setLore(this.lore);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            this.item.setItemMeta(itemMeta);
        }

    }

    private void setFields(ItemStack button_material, String button_title, List<String> loreTextWrapped, String sensor_flag,
                           int slot, String sufOn, String sufOff, String trigger_permission, TriggerTemplate addon) {
        this.item = button_material;
        this.displayNamePrefix = button_title;
        this.lore = loreTextWrapped;
        this.flagName = sensor_flag;
        this.slot = slot;
        if(sufOn != null)
            this.suffixOne = langString(sufOn).substring(0, 1).toUpperCase() + langString(sufOn).substring(1);
        if(sufOff != null)
            this.suffixTwo = langString(sufOff).substring(0, 1).toUpperCase() + langString(sufOff).substring(1);
        this.perm = trigger_permission;
        this.addonTemplate = addon;

    }

    public void updateButtonStatus(RPS selectedRPS, Inventory tempInv) {

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            if (selectedRPS.getAcceptedTriggerFlags().contains(flagName)) {
                itemMeta.addEnchant(Enchantment.POWER, 1, true);
                String suffix = (suffixOne == null) ? ("") : (": " + ChatColor.GREEN + suffixOne);
                itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + suffix);
            } else {
                itemMeta.removeEnchant(Enchantment.POWER);
                String suffix = (suffixTwo == null) ? ("") : (": " + ChatColor.RED + suffixTwo);
                itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + suffix);
            }
            item.setItemMeta(addonTemplate.updateButtonLore(selectedRPS, itemMeta));
            tempInv.setItem(slot, item);
        }
    }

    private String langString(String key) {
        if(key == null) return null;
        return RedstoneProximitySensor.getInstance().getLang().getOrDefault(key, key);
    }

    public String getDisplayNamePrefix() {
        return displayNamePrefix;
    }

    public String getFlag() {
        return flagName;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getPerm() {
        return perm;
    }
}
