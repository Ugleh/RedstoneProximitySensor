package com.ugleh.redstoneproximitysensor.utils;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.AddonTemplate;
import com.ugleh.redstoneproximitysensor.addons.TriggerAddons;
import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Trigger {
    public AddonTemplate addonTemplate;
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
        setFields(
                button_material
                , langString(button_title) + ": "
                , loreTextWrapped
                , sensor_flag
                , slot_number
                , RedstoneProximitySensor.getInstance().glow
                , langString(toggle_off).substring(0, 1).toUpperCase() + langString(toggle_off).substring(1)
                , langString(toggle_on).substring(0, 1).toUpperCase() + langString(toggle_on).substring(1)
                , trigger_permission
                , null
        );

        SetupButtonData();
        pl.guiMenu.setItem(this.slot, this.item);

    }

    public Trigger(String trigger_permission, ItemStack button_material, String button_title, String sensor_flag, String toggle_off, String toggle_on, List<String> loreTextWrapped, AddonTemplate addon) {
        PlayerListener pl = RedstoneProximitySensor.getInstance().playerListener;
        TriggerAddons ta = TriggerAddons.getInstance();

        setFields(
                button_material
                , langString(button_title) + ": "
                , loreTextWrapped
                , sensor_flag
                , ta.getNextAvaliableSlot()
                , RedstoneProximitySensor.getInstance().glow
                , langString(toggle_off).substring(0, 1).toUpperCase() + langString(toggle_off).substring(1)
                , langString(toggle_on).substring(0, 1).toUpperCase() + langString(toggle_on).substring(1)
                , trigger_permission
                , addon
        );

        SetupButtonData();
        pl.guiMenu.setItem(slot, button_material);

    }

    private void SetupButtonData() {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + this.displayNamePrefix);
        itemMeta.setLore(this.lore);
        this.item.setItemMeta(itemMeta);

    }

    private void setFields(ItemStack button_material, String button_title, List<String> loreTextWrapped, String sensor_flag,
                           int slot, Glow glow, String suf_off, String suf_on, String trigger_permission, AddonTemplate addon) {
        this.item = button_material;
        this.displayNamePrefix = button_title;
        this.lore = loreTextWrapped;
        this.flagName = sensor_flag;
        this.slot = slot;
        this.glow = glow;
        this.suffixOne = suf_off;
        this.suffixTwo = suf_on;
        this.perm = trigger_permission;
        this.addonTemplate = addon;

    }

    public void updateButtonStatus(RPS selectedRPS, Inventory tempInv) {
        ItemMeta itemMeta = item.getItemMeta();
        if (selectedRPS.getAcceptedEntities().contains(flagName)) {
            itemMeta.addEnchant(glow, 1, true);
            itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + ChatColor.GREEN + suffixOne);
        } else {
            itemMeta.removeEnchant(glow);
            itemMeta.setDisplayName(ChatColor.BLUE + displayNamePrefix + ChatColor.RED + suffixTwo);
        }
        item.setItemMeta(itemMeta);
        tempInv.setItem(slot, item);
    }

    private String langString(String key) {
        if (RedstoneProximitySensor.getInstance().getLang().containsKey(key)) {
            return RedstoneProximitySensor.getInstance().getLang().get(key);

        } else {
            return key;
        }
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
