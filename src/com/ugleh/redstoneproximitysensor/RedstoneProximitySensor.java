package com.ugleh.redstoneproximitysensor;

import com.ugleh.redstoneproximitysensor.addons.TriggerAddons;
import com.ugleh.redstoneproximitysensor.commands.CommandIgnoreRPS;
import com.ugleh.redstoneproximitysensor.commands.CommandRPS;
import com.ugleh.redstoneproximitysensor.configs.GeneralConfig;
import com.ugleh.redstoneproximitysensor.configs.LanguageConfig;
import com.ugleh.redstoneproximitysensor.configs.SensorConfig;
import com.ugleh.redstoneproximitysensor.listeners.PlayerListener;
import com.ugleh.redstoneproximitysensor.listeners.SensorListener;
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
	public String version;
	private GeneralConfig gConfig;
	private SensorConfig sensorConfig;
	public ItemStack rps;
	public final String chatPrefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "RPS" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED;
	public static RedstoneProximitySensor instance;
	private LanguageConfig languageConfig;
	private TriggerAddons triggerAddons;
	public boolean needsUpdate = false;
	public Glow glow;

	//Listeners
	public PlayerListener playerListener;
	public SensorListener sensorListener;
	
	
	@Override
	public void onEnable() {
		instance = this;
		version =  getDescription().getVersion();
		// Init configs.
		languageConfig = new LanguageConfig(this, "language.yml", "language.yml");
		gConfig = new GeneralConfig(this);
		
		//Create Glow
		glow = new Glow(new NamespacedKey(this, this.getDescription().getName()));
		
		// Init Listeners
		this.getServer().getPluginManager().registerEvents(sensorListener = new SensorListener(), this);
		this.getServer().getPluginManager().registerEvents(playerListener = new PlayerListener(), this);
		//Register addons
		setTriggerAddons(new TriggerAddons());
		
		// Add existing sensors back
		sensorConfig = new SensorConfig(this, "sensors.yml", "sensors.yml");
		
		if(gConfig.updateChecker)
			needsUpdate = new UpdateChecker(this.getVersion()).needsUpdate;
		// Setup Glow
		registerGlow();

		// Setting command Executors.
		this.getServer().getPluginCommand("rps").setExecutor(new CommandRPS(this));
		this.getServer().getPluginCommand("ignorerps").setExecutor(new CommandIgnoreRPS(this));

		

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

	@SuppressWarnings("deprecation")
	private void createRecipes() {
		rps = new ItemStack(Material.LEGACY_REDSTONE_TORCH_ON, 1);
		ItemMeta rpsMeta = rps.getItemMeta();
		rpsMeta.setDisplayName(ChatColor.RED + this.langString("lang_main_itemname"));
		Glow glow = new Glow(new NamespacedKey(this, this.getDescription().getName()));
		rpsMeta.addEnchant(glow, 1, true);
		rps.setItemMeta(rpsMeta);
		ShapedRecipe rpsRecipe;
		try {
			NamespacedKey key = new NamespacedKey(this, this.getDescription().getName());
			rpsRecipe = new ShapedRecipe(key, rps);

			} catch (NoClassDefFoundError error) {
				rpsRecipe = new ShapedRecipe(rps);
			}

		rpsRecipe.shape("-R-", "-R-", "-R-");
		rpsRecipe.setIngredient('R', Material.LEGACY_REDSTONE_TORCH_ON);
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
		return this.version;
	}

	public static RedstoneProximitySensor getInstance() {
		return instance;
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
	
}
