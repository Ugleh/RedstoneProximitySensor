package com.ugleh.redstoneproximitysensor;

import com.ugleh.redstoneproximitysensor.addons.TriggerCreator;
import com.ugleh.redstoneproximitysensor.command.CommandIgnoreRPS;
import com.ugleh.redstoneproximitysensor.command.CommandRPS;
import com.ugleh.redstoneproximitysensor.command.CommandRPSList;
import com.ugleh.redstoneproximitysensor.config.GeneralConfig;
import com.ugleh.redstoneproximitysensor.config.LanguageConfig;
import com.ugleh.redstoneproximitysensor.config.SensorConfig;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.listener.SensorListener;
import com.ugleh.redstoneproximitysensor.sqlite.Database;
import com.ugleh.redstoneproximitysensor.sqlite.SQLite;
import com.ugleh.redstoneproximitysensor.tabcomplete.TabCompleterRPS;
import com.ugleh.redstoneproximitysensor.util.Metrics;
import com.ugleh.redstoneproximitysensor.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
    public String chatPrefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "RPS" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED;
    public ItemStack rps;
    public boolean needsUpdate = false;

    //Listeners
    public PlayerListener playerListener;
    public SensorListener sensorListener;

    private GeneralConfig gConfig;
    private SensorConfig sensorConfig;
    private LanguageConfig languageConfig;
    private TriggerCreator triggerAddons;
    private Database db;

    public static RedstoneProximitySensor getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Init config.
        languageConfig = new LanguageConfig(this, "language.yml", "language.yml");
        chatPrefix = langStringColor("lang_chat_prefix");
        gConfig = new GeneralConfig(this);
        if(gConfig.isDisabling)
            return;

        if (this.getgConfig().isSqliteEnabled()) {
            this.db = new SQLite(this);
            this.db.load();

        }

        // Init Listeners
        this.getServer().getPluginManager().registerEvents(sensorListener = new SensorListener(), this);
        this.getServer().getPluginManager().registerEvents(playerListener = new PlayerListener(), this);
        gConfig.addTriggerFlagsToConfig();

        //Register addons
        setTriggerAddons(new TriggerCreator());

        // Add existing sensors back
        sensorConfig = new SensorConfig(this, "sensors.yml", "sensors.yml");

        if (gConfig.isUpdateCheckerEnabled())
            needsUpdate = new UpdateChecker(this.getVersion()).needsUpdate;

        // Setting command Executors.
        PluginCommand rps = getCommand("rps");
        PluginCommand ignorerps = getCommand("ignorerps");
        PluginCommand rpslist = getCommand("rpslist");

        rps.setExecutor(new CommandRPS());
        rps.setTabCompleter(new TabCompleterRPS());
        ignorerps.setExecutor(new CommandIgnoreRPS());
        rpslist.setExecutor(new CommandRPSList());

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
                p.sendMessage(prefixWithColor(ColorNode.NEGATIVE_MESSAGE) + langStringColor("lang_update_notice"));
                p.sendMessage(prefixWithColor(ColorNode.POSITIVE_MESSAGE) + "https://www.spigotmc.org/resources/17965/");

            }
        }
    }

    private void createRecipes() {
        rps = new ItemStack(Material.REDSTONE_TORCH, 1);
        rps.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        ItemMeta rpsMeta = rps.getItemMeta();
        rpsMeta.setDisplayName(ChatColor.RED + this.langStringColor("lang_main_itemname"));
        rpsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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

    public String getVersion() {
        return getDescription().getVersion();
    }

    public HashMap<String, String> getLang() {
        return languageConfig.getLanguageNodes();
    }

    public String getColor(ColorNode colorNode) {
        return ChatColor.translateAlternateColorCodes('&', languageConfig.getColorNodes().get("color_" + colorNode.name().toLowerCase()));
    }
    public enum ColorNode{
        POSITIVE_MESSAGE,
        NEGATIVE_MESSAGE,
        NEUTRAL_MESSAGE
    }
    public String langStringRaw(String key) {
        return getInstance().getLang().get(key);
    }
    public String langStringColor(String key) {
        return ChatColor.translateAlternateColorCodes('&', getInstance().getLang().get(key));
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public TriggerCreator getTriggerAddons() {
        return triggerAddons;
    }

    public void setTriggerAddons(TriggerCreator triggerAddons) {
        this.triggerAddons = triggerAddons;
    }

    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
        return (chatPrefix + getColor(colorNode));
    }
    public Database getDatabase() {
        return this.db;
    }
}
