package com.ugleh.redstonesensor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
//import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RedstoneSensor extends JavaPlugin{
	public static Integer maxRange = null;
	public static Integer defaultRange = null;
	public static String redstoneProximityRangeText = null;
	public static String redstoneProximityRangeNotifyText = null;
	public static String notRedstoneProximityRangeText = null;
	
	public static HashMap<Location, Integer> redstoneList = new HashMap<Location, Integer>();
	public static HashMap<Location, Integer> notRedstoneList = new HashMap<Location, Integer>();
public void onEnable(){

	
	getServer().getPluginManager().registerEvents(new RedstoneSensorListener(this), this);	
    if (!new File(getDataFolder(), "config.yml").exists()) {
    saveDefaultConfig();
    }
    
    
	maxRange = getConfig().getInt("Config.max-range");
	defaultRange = getConfig().getInt("Config.default-range");
	redstoneProximityRangeText = getConfig().getString("Config.proximity-sensor-name");
	notRedstoneProximityRangeText = getConfig().getString("Config.not-proximity-sensor-name");
	redstoneProximityRangeNotifyText = getConfig().getString("Config.proximity-range-notify-text");

    ArrayList<String> keys = new ArrayList<String>();
    keys.addAll(getConfig().getConfigurationSection("Config").getKeys(false));
	if(!keys.contains("max-range")){
		maxRange = 10;
		getConfig().set("Config.max-range", 10);
	}
	if(!keys.contains("default-range")){
		defaultRange = 3;
		getConfig().set("Config.default-range", 3);
	}
	if(!keys.contains("proximity-sensor-name")){
		redstoneProximityRangeText = "Redstone Proximity Sensor";
		getConfig().set("Config.proximity-sensor-name", "Redstone Proximity Sensor");
	}
	if(!keys.contains("not-proximity-sensor-name")){
		notRedstoneProximityRangeText = "NOT Redstone Proximity Sensor";
		getConfig().set("Config.not-proximity-sensor-name", "NOT Redstone Proximity Sensor");
	}
	if(!keys.contains("proximity-range-notify-text")){
		redstoneProximityRangeNotifyText = "Proximity Range";
		getConfig().set("Config.proximity-range-notify-text", "Proximity Range");
	}
	try {
		getConfig().save(new File(getDataFolder(), "config.yml"));
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	if(!getConfig().toString().isEmpty()){
for(String key : getConfig().getConfigurationSection("Redstones").getKeys(false)) {
    if(("NOT").equalsIgnoreCase((getConfig().getString("Redstones."+key+".Type")))){
    	notRedstoneList.put(new Location(getServer().getWorld(getConfig().getString("Redstones."+key+".World")), getConfig().getInt("Redstones."+key+".X"), getConfig().getInt("Redstones."+key+".Y"), getConfig().getInt("Redstones."+key+".Z")), getConfig().getInt("Redstones."+key+".Range"));

    }else{
    	redstoneList.put(new Location(getServer().getWorld(getConfig().getString("Redstones."+key+".World")), getConfig().getInt("Redstones."+key+".X"), getConfig().getInt("Redstones."+key+".Y"), getConfig().getInt("Redstones."+key+".Z")), getConfig().getInt("Redstones."+key+".Range"));

    }    }
}
    
	 ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta = rps.getItemMeta();
		rpsmeta.setDisplayName(ChatColor.RED + redstoneProximityRangeText);
		rps.setItemMeta(rpsmeta);
       //ShapelessRecipe flashRecipe = new ShapelessRecipe(rps).addIngredient(1, Material.REDSTONE_TORCH_ON).addIngredient(1, Material.REDSTONE_TORCH_ON).addIngredient(1, Material.REDSTONE_TORCH_ON);
		ShapedRecipe rpsRecipe = new ShapedRecipe(rps);
		rpsRecipe.shape(" R "," R "," R ");
		//rpsRecipe.setIngredient('G', Material.AIR);
		rpsRecipe.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe);
		
	ItemStack rps2 = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta2 = rps2.getItemMeta();
		rpsmeta2.setDisplayName(ChatColor.RED + notRedstoneProximityRangeText);
		rps2.setItemMeta(rpsmeta2);
	    //ShapelessRecipe flashRecipe = new ShapelessRecipe(rps).addIngredient(1, Material.REDSTONE_TORCH_ON).addIngredient(1, Material.REDSTONE_TORCH_ON).addIngredient(1, Material.REDSTONE_TORCH_ON);
		ShapedRecipe rpsRecipe2 = new ShapedRecipe(rps2);
		rpsRecipe2.shape("   ","RRR","   ");
		//rpsRecipe2.setIngredient('G', Material.AIR);
		rpsRecipe2.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe2);
        //getServer().addRecipe(rpsRecipe);

		
    }

}

