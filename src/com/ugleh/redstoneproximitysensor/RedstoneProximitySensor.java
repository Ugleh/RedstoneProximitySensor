package com.ugleh.redstoneproximitysensor;

import com.ugleh.redstoneproximitysensor.addons.TriggerAddons;
import com.ugleh.redstoneproximitysensor.commands.CommandIgnoreRPS;
import com.ugleh.redstoneproximitysensor.commands.CommandRPS;
import com.ugleh.redstoneproximitysensor.commands.CommandRPSList;
import com.ugleh.redstoneproximitysensor.configs.GeneralConfig;
import com.ugleh.redstoneproximitysensor.configs.LanguageConfig;
import com.ugleh.redstoneproximitysensor.configs.SensorConfig;
import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;
import com.ugleh.redstoneproximitysensor.listeners.SensorListener;
import com.ugleh.redstoneproximitysensor.sqlite.Database;
import com.ugleh.redstoneproximitysensor.sqlite.SQLite;
import com.ugleh.redstoneproximitysensor.utils.Glow;
import com.ugleh.redstoneproximitysensor.utils.Metrics;
import com.ugleh.redstoneproximitysensor.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;

public class RedstoneProximitySensor extends JavaPlugin {
    public static RedstoneProximitySensor instance;
    public final String chatPrefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "RPS" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED;
    public ItemStack rps;
    public boolean needsUpdate = false;
    public Glow glow;
    //Listeners
    public PlayerListener playerListener;
    public SensorListener sensorListener;
    private GeneralConfig gConfig;
    private SensorConfig sensorConfig;
    private LanguageConfig languageConfig;
    private TriggerAddons triggerAddons;
    private Database db;

    public static RedstoneProximitySensor getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;


        // Init configs.
        languageConfig = new LanguageConfig(this, "language.yml", "language.yml");
        gConfig = new GeneralConfig(this);

        if (this.getgConfig().sqlite) {
            this.db = new SQLite(this);
            this.db.load();

        }
        //Create Glow
        glow = new Glow(new NamespacedKey(this, this.getDescription().getName()));

        // Init Listeners
        this.getServer().getPluginManager().registerEvents(sensorListener = new SensorListener(), this);
        this.getServer().getPluginManager().registerEvents(playerListener = new PlayerListener(), this);
        //Register addons
        setTriggerAddons(new TriggerAddons());

        // Add existing sensors back
        sensorConfig = new SensorConfig(this, "sensors.yml", "sensors.yml");

        if (gConfig.updateChecker)
            needsUpdate = new UpdateChecker(this.getVersion()).needsUpdate;
        // Setup Glow
        registerGlow();

        // Setting command Executors.
        this.getServer().getPluginCommand("rps").setExecutor(new CommandRPS());
        this.getServer().getPluginCommand("ignorerps").setExecutor(new CommandIgnoreRPS());
        this.getServer().getPluginCommand("rpslist").setExecutor(new CommandRPSList());


        // Others

        createRecipes();
        initUpdateAlert();
        initMetrics();
        this.getLogger().log(Level.INFO, "Plugin has started!");
        this.getLogger().log(Level.INFO, "RPS's Loaded: " + getSensorConfig().getSensorList().size());
    }

    private void initMetrics() {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
    }

    private void initUpdateAlert() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp() && this.needsUpdate) {
                p.sendMessage(getInstance().chatPrefix + ChatColor.DARK_RED + langString("lang_update_notice"));
                p.sendMessage(getInstance().chatPrefix + ChatColor.GREEN + "https://www.spigotmc.org/resources/17965/");

            }
        }
    }

    private void createRecipes() {
        rps = new ItemStack(Material.REDSTONE_TORCH, 1);
        ItemMeta rpsMeta = rps.getItemMeta();
        rpsMeta.setDisplayName(ChatColor.RED + this.langString("lang_main_itemname"));
        Glow glow = new Glow(new NamespacedKey(this, this.getDescription().getName()));
        rpsMeta.addEnchant(glow, 1, true);
        rps.setItemMeta(rpsMeta);
        ShapedRecipe rpsRecipe;
        NamespacedKey key = new NamespacedKey(this, this.getDescription().getName());
        rpsRecipe = new ShapedRecipe(key, rps);
        rpsRecipe.shape("-R-", "-R-", "-R-");
        rpsRecipe.setIngredient('R', Material.REDSTONE_TORCH);
        this.getServer().addRecipe(rpsRecipe);

    }

    @Override
    public void onDisable() {

    }

    public GeneralConfig getgConfig() {
        return gConfig;
    }

    public SensorConfig getSensorConfig() {
        return sensorConfig;
    }

    private void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(new NamespacedKey(this, this.getDescription().getName()));
            Enchantment.registerEnchantment(glow);
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getVersion() {
        return getDescription().getVersion();
    }

    public HashMap<String, String> getLang() {
        return languageConfig.getLanguageNodes();
    }

    public String langString(String key) {
        return getInstance().getLang().get(key);
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public TriggerAddons getTriggerAddons() {
        return triggerAddons;
    }

    public void setTriggerAddons(TriggerAddons triggerAddons) {
        this.triggerAddons = triggerAddons;
    }


    public Database getDatabase() {
        return this.db;
    }
}
