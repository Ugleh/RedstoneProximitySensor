package com.ugleh.redstoneproximitysensor.trigger;

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
import java.util.UUID;

public class IndividualMob extends TriggerTemplate implements Listener{
    private Inventory mainMobMenu;
    private HashMap<Mobs.Nature, Inventory> subMenus = new HashMap<>();
    public HashMap<UUID, Inventory> userSelectedInventory = new HashMap<>();

    public String flagName = "INDIVIDUAL_MOB";

    public IndividualMob(PlayerListener playerListener) {
        String loreNode = "lang_button_im_lore";
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        int slotNumber = 13;
        String buttonTitle = "lang_button_individualmobtrigger";
        String triggerPermission = "button_individualmobtrigger";
        Material buttonMaterial = Material.CRAFTING_TABLE;
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, null, null, lore, this));
        createMainMenu();
    }


    public void openMobInventory(Player player) {
        player.openInventory(mainMobMenu);
    }

    private void createMainMenu() {
        mainMobMenu = Bukkit.createInventory(null, 9, ChatColor.BLUE + "Mobs by Behavior");
        for (Mobs.Nature nature : Mobs.Nature.values()) {
            mainMobMenu.addItem(createButtonItem(nature.getTitle(), Material.EMERALD));
            subMenus.put(nature, createSubMenu(nature));
        }

    }

    private Inventory createSubMenu(Mobs.Nature nature) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.BLUE + nature.getTitle() + " Mobs");
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
    public boolean buttonPressed(Boolean is_on, RPS affectedRPS, Player player) {
        Bukkit.broadcastMessage("Test");
        openMobInventory(player);
        return false;
    }

    @Override
    public void rpsCreated(RPS affectedRPS) {

    }

    @Override
    public void rpsRemoved(RPS affectedRPS) {

    }
}
