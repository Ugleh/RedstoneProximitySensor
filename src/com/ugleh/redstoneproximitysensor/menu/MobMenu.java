package com.ugleh.redstoneproximitysensor.menu;

import com.ugleh.redstoneproximitysensor.util.Mobs;
import com.ugleh.redstoneproximitysensor.util.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class MobMenu {
    public Inventory mainMobMenu;
    public HashMap<Mobs.Nature, Inventory> subMenus = new HashMap<>();

    MobMenu() {
        createMainMenu();
    }

    private void createMainMenu() {
        mainMobMenu = Bukkit.createInventory(null, 8, ChatColor.BLUE + "Mobs by Behavior");
        for (Mobs.Nature nature : Mobs.Nature.values()) {
            mainMobMenu.addItem(createButtonItem(nature.getTitle(), Material.EMERALD));
            subMenus.put(nature, createSubMenu(nature));
        }

    }

    private Inventory createSubMenu(Mobs.Nature nature) {
        Inventory inventory = Bukkit.createInventory(null, 64, ChatColor.BLUE + nature.getTitle() + " Mobs");
        for(Mobs mobs : Mobs.values()) {
            if(mobs.getNature() == nature) {
                ItemStack skull = SkullCreator.itemFromBase64(mobs.getSkullBase64());
                ItemMeta skullmeta = skull.getItemMeta();
                skullmeta.setDisplayName(mobs.getName());
                skull.setItemMeta(skullmeta);
                inventory.addItem(skull);
            }
        }
        return inventory;
    }


    public ItemStack createButtonItem(String title, Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE + title);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
