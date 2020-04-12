package com.ugleh.redstoneproximitysensor.trigger;

import com.cryptomorin.xseries.XMaterial;
import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.addons.TriggerTemplate;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.Mobs;
import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.SkullCreator;
import com.ugleh.redstoneproximitysensor.util.Trigger;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class IndividualMob extends TriggerTemplate implements Listener{
    private Inventory mainMobMenu;
    private HashMap<Mobs.Nature, Inventory> subMenus = new HashMap<>();
    public HashMap<UUID, RPS> lastRPSSelected = new HashMap<>();
    private List<String> startingLore;

    public String flagName = "INDIVIDUAL_MOB";

    public IndividualMob(PlayerListener playerListener) {
        RedstoneProximitySensor.getInstance().getServer().getPluginManager().registerEvents(this, getInstance());
        String loreNode = "lang_button_im_lore";
        startingLore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        int slotNumber = 13;
        String buttonTitle = "lang_button_individualmobtrigger";
        String triggerPermission = "button_individualmobtrigger";
        Material buttonMaterial = XMaterial.CRAFTING_TABLE.parseMaterial();
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, null, null, startingLore, this));
        createMainMenu();
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!e.getWhoClicked().hasPermission("rps.menu")) return;
        if(e.getView().getTopInventory().equals(mainMobMenu)) {
            clickMainMenu(e);
            e.setCancelled(true);
        }
        if(subMenus.containsValue(e.getView().getTopInventory())) {
            clickSubMenu(e);
            e.setCancelled(true);
        }
    }

    private void clickSubMenu(InventoryClickEvent e) {

        if(e.getCurrentItem().hasItemMeta()) {
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            assert itemMeta != null;
            String buttonPressed = itemMeta.getDisplayName();
            Player player = (Player) e.getWhoClicked();
            if(buttonPressed.equals(ChatColor.YELLOW + langStringColor("lang_general_menu_back"))) {
                player.openInventory(mainMobMenu);
            }else {
                String formattedButtonName = buttonPressed.replace(" ", "_").toUpperCase();
                if(Mobs.getMobNames().contains(formattedButtonName)) {
                    RPS sensor = lastRPSSelected.get(player.getUniqueId());
                    getInstance().getSensorConfig().toggleIndividualMobs(sensor, player, formattedButtonName);
                    playToggleSound(player);
                    if(sensor.getIndividualMobs().size() > 0) {
                        ArrayList<String> newAcceptedTriggerFlags = sensor.getAcceptedTriggerFlags();
                        newAcceptedTriggerFlags.add(flagName);
                        sensor.setAcceptedTriggerFlags(newAcceptedTriggerFlags);
                    }else {
                        ArrayList<String> newAcceptedTriggerFlags = sensor.getAcceptedTriggerFlags();
                        newAcceptedTriggerFlags.remove(flagName);
                        sensor.setAcceptedTriggerFlags(newAcceptedTriggerFlags);
                    }
                }
            }
        }
    }
    private void clickMainMenu(InventoryClickEvent e) {
        if(e.getCurrentItem().hasItemMeta()) {
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            assert itemMeta != null;
            String buttonPressed = itemMeta.getDisplayName();
            Player player = (Player) e.getWhoClicked();
            if(buttonPressed.equals(ChatColor.YELLOW + langStringColor("lang_general_menu_back"))) {
                player.openInventory(getInstance().playerListener.userSelectedInventory.get(player.getUniqueId()));
                getInstance().playerListener.showGUIMenu(player, lastRPSSelected.get(player.getUniqueId()));
            }else {
                for(Mobs.Nature nature : Mobs.Nature.values()) {
                    if(buttonPressed.equals(nature.getTitle())) {
                        player.openInventory(subMenus.get(nature));
                        break;
                    }
                }
            }
        }
    }

    private void openMobInventory(Player player) {
        player.openInventory(mainMobMenu);
    }

    private void createMainMenu() {
        mainMobMenu = Bukkit.createInventory(null, 18, ChatColor.BLUE + langStringColor("lang_mobs_behavior_title"));
        int i = 0;
        for (Mobs.Nature nature : Mobs.Nature.values()) {
            ItemStack skull = SkullCreator.itemFromBase64(Mobs.getMobs(nature)[0].getSkullBase64());
            ItemMeta skullMeta = skull.getItemMeta();
            assert skullMeta != null;
            skullMeta.setDisplayName(nature.getTitle());
            skullMeta.setLore(getInstance().wordWrapLore(nature.getDesc()));
            skull.setItemMeta(skullMeta);
            mainMobMenu.setItem(i, skull);
            i = i + 2;
            subMenus.put(nature, createSubMenu(nature));
        }
        ItemStack backButton = XMaterial.SUNFLOWER.parseItem();
        ItemMeta itemMeta = backButton.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.YELLOW + langStringColor("lang_general_menu_back"));
        backButton.setItemMeta(itemMeta);
        mainMobMenu.setItem(13, backButton);

    }

    private Inventory createSubMenu(Mobs.Nature nature) {
        int invSize = (int) (9*(Math.ceil(Math.abs(Mobs.getMobs(nature).length/9)))) + 18;
        Inventory inventory = Bukkit.createInventory(null, invSize, ChatColor.BLUE + nature.getTitle() + " " + langStringColor("lang_mobs_title_suffix"));
        for(Mobs mobs : Mobs.values()) {

            if(entityTypeContains(mobs.getEntityTypeName()) && mobs.getNature() == nature) {
                ItemStack skull = SkullCreator.itemFromBase64(mobs.getSkullBase64());
                ItemMeta skullMeta = skull.getItemMeta();
                assert skullMeta != null;
                skullMeta.setDisplayName(mobs.getName());
                skull.setItemMeta(skullMeta);
                inventory.addItem(skull);
            }
        }
        ItemStack backButton = new ItemStack(XMaterial.SUNFLOWER.parseMaterial());
        ItemMeta itemMeta = backButton.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.YELLOW + langStringColor("lang_general_menu_back"));
        backButton.setItemMeta(itemMeta);
        inventory.setItem(invSize - 5, backButton);
        return inventory;
    }

    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity entity) {
        if(rps.getIndividualMobs().contains(entity.getType().toString()))
        {
            return TriggerCreator.TriggerResult.TRIGGERED;
        }else
        {
            return TriggerCreator.TriggerResult.NOT_TRIGGERED;
        }
    }

    @Override
    public boolean buttonPressed(Boolean is_on, RPS affectedRPS, Player player) {
        lastRPSSelected.put(player.getUniqueId(), affectedRPS);
        Bukkit.getScheduler().runTaskLater(getInstance(), () -> player.openInventory(mainMobMenu), 1L);
        openMobInventory(player);
        return (affectedRPS.getIndividualMobs().size() > 0);
    }

    @Override
    public void rpsCreated(RPS affectedRPS) {

    }

    @Override
    public void rpsRemoved(RPS affectedRPS) {

    }


    public static boolean entityTypeContains(String test) {

        for (EntityType entityType : EntityType.values()) {
            if (entityType.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    @Override

    public ItemMeta updateButtonLore(RPS selectedRPS, ItemMeta itemMeta) {
        List<String> newLore = new ArrayList<>();
        newLore.addAll(startingLore);
        for (String individualMob : selectedRPS.getIndividualMobs()) {
            String addedLore = ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + WordUtils.capitalizeFully(individualMob).replace("_", " ");
            newLore.add(addedLore);
        }
        itemMeta.setLore(newLore);

        if(selectedRPS.getIndividualMobs().size() > 0) {
            itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        }
            return itemMeta;
    }
    private RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }


    void playToggleSound(Player p) {
        try {
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5F, 1F);
        } catch (NoSuchFieldError error) {
            p.playSound(p.getLocation(), Sound.valueOf("ORB_PICKUP"), 0.5F, 1F);
        }
    }

    private String langStringColor(String string) {
        return getInstance().langStringColor(string);
    }

}
