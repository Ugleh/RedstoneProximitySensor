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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IndividualMob extends TriggerTemplate implements Listener{
    private Inventory mainMobMenu;
    private HashMap<Mobs.Nature, Inventory> subMenus = new HashMap<>();
    public HashMap<UUID, RPS> lastRPSSelected = new HashMap<>();

    public String flagName = "INDIVIDUAL_MOB";

    public IndividualMob(PlayerListener playerListener) {
        RedstoneProximitySensor.getInstance().getServer().getPluginManager().registerEvents(this, RedstoneProximitySensor.getInstance());
        String loreNode = "lang_button_im_lore";
        List<String> lore = playerListener.WordWrapLore(playerListener.langString(loreNode));
        int slotNumber = 13;
        String buttonTitle = "lang_button_individualmobtrigger";
        String triggerPermission = "button_individualmobtrigger";
        Material buttonMaterial = Material.CRAFTING_TABLE;
        playerListener.addTrigger(new Trigger(triggerPermission, new ItemStack(buttonMaterial, 1), slotNumber, buttonTitle, flagName, null, null, lore, this));
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

        if(Objects.requireNonNull(e.getCurrentItem(), "ItemMeta cannot be null.").hasItemMeta()) {
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            assert itemMeta != null;
            String buttonPressed = itemMeta.getDisplayName();
            Player player = (Player) e.getWhoClicked();
            if(buttonPressed.equals(ChatColor.YELLOW + langStringColor("lang_general_menu_back"))) {
                player.openInventory(mainMobMenu);
            }else {
                //TODO: Toggle Mob Selected.
            }
        }
    }
    private void clickMainMenu(InventoryClickEvent e) {
        if(Objects.requireNonNull(e.getCurrentItem(), "ItemMeta cannot be null.").hasItemMeta()) {
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            assert itemMeta != null;
            String buttonPressed = itemMeta.getDisplayName();
            Player player = (Player) e.getWhoClicked();
            if(buttonPressed.equals(ChatColor.YELLOW + langStringColor("lang_general_menu_back"))) {
                player.openInventory(RedstoneProximitySensor.getInstance().playerListener.userSelectedInventory.get(player.getUniqueId()));
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
            skullMeta.setLore(RedstoneProximitySensor.getInstance().wordWrapLore(nature.getDesc()));
            skull.setItemMeta(skullMeta);
            mainMobMenu.setItem(i, skull);
            i = i + 2;
            subMenus.put(nature, createSubMenu(nature));
        }
        ItemStack backButton = new ItemStack(Material.SUNFLOWER);
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
            if(mobs.getNature() == nature) {
                ItemStack skull = SkullCreator.itemFromBase64(mobs.getSkullBase64());
                ItemMeta skullMeta = skull.getItemMeta();
                assert skullMeta != null;
                skullMeta.setDisplayName(mobs.getName());
                skull.setItemMeta(skullMeta);
                inventory.addItem(skull);
            }
        }
        ItemStack backButton = new ItemStack(Material.SUNFLOWER);
        ItemMeta itemMeta = backButton.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.YELLOW + langStringColor("lang_general_menu_back"));
        backButton.setItemMeta(itemMeta);
        inventory.setItem(invSize - 5, backButton);
        return inventory;
    }


    public ItemStack createButtonItem(String title, Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.BLUE + title);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String langStringColor(String string) {
        return RedstoneProximitySensor.getInstance().langStringColor(string);
    }
    @Override
    public TriggerCreator.TriggerResult checkTrigger(RPS rps, Entity e) {
        return null;
    }

    @Override
    public boolean buttonPressed(Boolean is_on, RPS affectedRPS, Player player) {
        lastRPSSelected.put(player.getUniqueId(), affectedRPS);
        Bukkit.getScheduler().runTaskLater(RedstoneProximitySensor.getInstance(), () -> player.openInventory(mainMobMenu), 1L);
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
