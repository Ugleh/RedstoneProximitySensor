package com.ugleh.redstoneproximitysensor.trigger;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.Mobs;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.SkullCreator;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class IndividualMob extends TriggerTemplate implements Listener{
    public Inventory mainMobMenu;
    public HashMap<Mobs.Nature, Inventory> subMenus = new HashMap<>();
    public HashMap<String, Inventory> rpsMobInventory = new HashMap<>();

    public String flagName = "INDIVIDUAL_MOB";
    private Material buttonMaterial = Material.CRAFTING_TABLE;
    private String triggerPermission = "button_individualmobtrigger";
    private String buttonTitle = "lang_button_individualmobtrigger";
    private String loreNode = "lang_button_im_lore";
    private int slotNumber = 13;

    public IndividualMob(PlayerListener playerListener) {
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, "lang_button_true", "lang_button_false", lore));
    }


    public void openMobInventory(Player player) {
        player.openInventory(mainMobMenu);
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
                ItemMeta skullMeta = skull.getItemMeta();
                skullMeta.setDisplayName(mobs.getName());
                skull.setItemMeta(skullMeta);
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

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e) {
        return null;
    }

    @Override
    public boolean buttonPressed(Boolean is_on, RPS affectedRPS) {
        return true;
    }

    @Override
    public void rpsCreated(RPS affectedRPS) {

    }

    @Override
    public void rpsRemoved(RPS affectedRPS) {

    }
}
