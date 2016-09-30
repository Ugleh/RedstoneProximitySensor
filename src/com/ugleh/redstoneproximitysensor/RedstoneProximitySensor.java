package com.ugleh.redstoneproximitysensor;

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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

public class RedstoneProximitySensor extends JavaPlugin{
    public final String version = "2.1.2";
    private GeneralConfig gConfig;
    private SensorConfig sensorConfig;
	public ItemStack rps;
	public final String chatPrefix = ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "RPS" + ChatColor.DARK_PURPLE + "] " + ChatColor.RED ;
    public static RedstoneProximitySensor instance;
    private LanguageConfig languageConfig;
	public boolean needsUpdate;

	@Override
	public void onEnable()
	{
	    
		instance = this;	
		
		//Init configs.
		languageConfig = new LanguageConfig(this, "language.yml", "language.yml");
		gConfig = new GeneralConfig(this);
		sensorConfig = new SensorConfig(this, "sensors.yml","sensors.yml");
		
		//Check for update
		needsUpdate = new UpdateChecker(this.getVersion()).needsUpdate;

		//Setup Glow
		registerGlow();
		
		//Setting command Executors.
		this.getServer().getPluginCommand("rps").setExecutor(new CommandRPS(this));
		
		//Init Listeners
		this.getServer().getPluginManager().registerEvents(new SensorListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		//Others
		createRecipes();
		initUpdateAlert();
		
		initMetrics();
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
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.isOp() && this.needsUpdate)
			{
				p.sendMessage(getInstance().chatPrefix + ChatColor.DARK_RED + langString("lang_update_notice"));
				p.sendMessage(getInstance().chatPrefix + ChatColor.GREEN + "https://www.spigotmc.org/resources/17965/");

			}
		}
	}

	private void createRecipes() {
		rps = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
		ItemMeta rpsMeta = rps.getItemMeta();
		rpsMeta.setDisplayName(ChatColor.RED + "Redstone Proximity Sensor");
		Glow glow = new Glow(1234);
		rpsMeta.addEnchant(glow, 1, true);
		rps.setItemMeta(rpsMeta);
        ShapedRecipe rpsRecipe;
        rpsRecipe = new ShapedRecipe(rps);
		rpsRecipe.shape("-R-","-R-","-R-");
		rpsRecipe.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe);
		
	}
	
	
	@Override
	public void onDisable()
	{
		
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(70);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

	public String getVersion()
	{
		return this.version;
	}
    public static RedstoneProximitySensor getInstance()
    {
    	return instance;
    }

    public HashMap<String, String> getLang()
    {
    	return languageConfig.getLanguageNodes();
    }
    public String langString(String key)
	{
		return getInstance().getLang().get(key);
	}


	public LanguageConfig getLanguageConfig() {
		return languageConfig;
	}
}
