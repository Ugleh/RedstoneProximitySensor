package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
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
    private Glow glow;
    private String suffixOne;
    private String suffixTwo;

    public Trigger(String trigger_permission, ItemStack button_material, int slot_number, String button_title, String sensor_flag, String toggleOn, String toggleOff, List<String> loreTextWrapped) {
        PlayerListener pl = PlayerListener.instance;
        setFields(
                button_material
                , langString(button_title) + ": "
                , loreTextWrapped
                , sensor_flag
                , slot_number
                , RedstoneProximitySensor.getInstance().glow
                , langString(toggleOn).substring(0, 1).toUpperCase() + langString(toggleOn).substring(1)
                , langString(toggleOff).substring(0, 1).toUpperCase() + langString(toggleOff).substring(1)
                , trigger_permission
                , null
        );

        setupButtonMetaData();
        pl.guiMenu.setItem(this.slot, this.item);

    }

    public Trigger(String trigger_permission, ItemStack button_material, String button_title, String sensor_flag, String toggleOn, String toggleOff, List<String> loreTextWrapped, TriggerTemplate addon) {
        PlayerListener pl = RedstoneProximitySensor.getInstance().playerListener;
        TriggerCreator triggerCreator = TriggerCreator.getInstance();

        setFields(
                button_material
                , langString(button_title) + ": "
                , loreTextWrapped
                , sensor_flag
                , triggerCreator.getAvailableSlot()
                , RedstoneProximitySensor.getInstance().glow
                , langString(toggleOn).substring(0, 1).toUpperCase() + langString(toggleOn).substring(1)
                , langString(toggleOff).substring(0, 1).toUpperCase() + langString(toggleOff).substring(1)
                , trigger_permission
                , addon
        );

        setupButtonMetaData();
        pl.guiMenu.setItem(slot, button_material);

    }

    private void setupButtonMetaData() {
        ItemMeta itemMeta = this.item.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ChatColor.BLUE + this.displayNamePrefix);
            itemMeta.setLore(this.lore);
            this.item.setItemMeta(itemMeta);
        }

    }

    private void setFields(ItemStack button_material, String button_title, List<String> loreTextWrapped, String sensor_flag,
                           int slot, Glow glow, String sufOn, String sufOff, String trigger_permission, TriggerTemplate addon) {
        this.item = button_material;
        this.displayNamePrefix = button_title;
        this.lore = loreTextWrapped;
        this.flagName = sensor_flag;
        this.slot = slot;
        this.glow = glow;
        this.suffixOne = sufOn;
        this.suffixTwo = sufOff;
        this.perm = trigger_permission;
        this.addonTemplate = addon;

    }

    public void updateButtonStatus(RPS selectedRPS, Inventory tempInv) {
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta != null) {
            if (selectedRPS.getAcceptedTriggerFlags().contains(flagName)) {
                itemMeta.addEnchant(glow, 1, true);
                itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + ChatColor.GREEN + suffixOne);
            } else {
                itemMeta.removeEnchant(glow);
                itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + ChatColor.RED + suffixTwo);
            }
            item.setItemMeta(itemMeta);
            tempInv.setItem(slot, item);
        }
    }

    private String langString(String key) {
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
