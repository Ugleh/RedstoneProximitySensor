package com.ugleh.redstonesensor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
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
	public static Boolean onlyOwner = null;
	public static String redstoneProximityRangeText = null;
	public static String redstoneProximityRangeNotifyText = null;
	public static String notRedstoneProximityRangeText = null;
	public Boolean updatechecker;
	public static Boolean outdated = false;
	private String currentVersion = "1.9.3";
	private String readurl = "https://raw.github.com/Ugleh/RedstoneSensor/master/version.txt";

	public static HashMap<Location, ArrayList<String>> redstoneList = new HashMap<Location, 	ArrayList<String>>();
	public static HashMap<Location, ArrayList<String>> notRedstoneList = new HashMap<Location, ArrayList<String>>();
public void onEnable(){
	
	getServer().getPluginManager().registerEvents(new RedstoneSensorListener(this), this);	
    if (!new File(getDataFolder(), "config.yml").exists()) {
    saveDefaultConfig();
    }
    
    //Basic config settings below to make sure they have certian configs and if they dont then add them.
	maxRange = getConfig().getInt("Config.max-range");
	defaultRange = getConfig().getInt("Config.default-range");
	onlyOwner = getConfig().getBoolean("Config.owner-only-change-range");
	redstoneProximityRangeText = getConfig().getString("Config.proximity-sensor-name");
	notRedstoneProximityRangeText = getConfig().getString("Config.not-proximity-sensor-name");
	redstoneProximityRangeNotifyText = getConfig().getString("Config.proximity-range-notify-text");
	updatechecker = getConfig().getBoolean("Config.update-checker");
    ArrayList<String> keys = new ArrayList<String>();
    keys.addAll(getConfig().getConfigurationSection("Config").getKeys(false));
	if(!keys.contains("update-checker")){
		updatechecker = true;
		getConfig().set("Config.update-checker", true);
	}
	if(!keys.contains("max-range")){
		maxRange = 10;
		getConfig().set("Config.max-range", 10);
	}
	if(!keys.contains("owner-only-change-rank")){
		onlyOwner = true;
		getConfig().set("Config.owner-only-change-range", true);
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
    	ArrayList<String> list = new ArrayList<String>();
    	list.add(String.valueOf(getConfig().getInt("Redstones."+key+".Range")));
    	list.add(String.valueOf(getConfig().getString("Redstones."+key+".Owner")));
    	notRedstoneList.put(new Location(getServer().getWorld(getConfig().getString("Redstones."+key+".World")), getConfig().getInt("Redstones."+key+".X"), getConfig().getInt("Redstones."+key+".Y"), getConfig().getInt("Redstones."+key+".Z")), list);

    }else{
    	ArrayList<String> list = new ArrayList<String>();
    	list.add(String.valueOf(getConfig().getInt("Redstones."+key+".Range")));
    	list.add(String.valueOf(getConfig().getString("Redstones."+key+".Owner")));
    	redstoneList.put(new Location(getServer().getWorld(getConfig().getString("Redstones."+key+".World")), getConfig().getInt("Redstones."+key+".X"), getConfig().getInt("Redstones."+key+".Y"), getConfig().getInt("Redstones."+key+".Z")), list);

    }    }
}
	
	//Crafting Recipe for the regular Proximity Sensor
	 ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta = rps.getItemMeta();
		rpsmeta.setDisplayName(ChatColor.RED + redstoneProximityRangeText);
		rps.setItemMeta(rpsmeta);
		ShapedRecipe rpsRecipe = new ShapedRecipe(rps);
		rpsRecipe.shape(" R "," R "," R ");
		rpsRecipe.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe);
	
	//Crafting Recipe for the Inverted or NOT Proximity Sensor.
	ItemStack rps2 = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta2 = rps2.getItemMeta();
		rpsmeta2.setDisplayName(ChatColor.RED + notRedstoneProximityRangeText);
		rps2.setItemMeta(rpsmeta2);
		ShapedRecipe rpsRecipe2 = new ShapedRecipe(rps2);
		rpsRecipe2.shape("   ","RRR","   ");
		rpsRecipe2.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe2);
	
outdated = true;
startUpdateCheck();
if(outdated){
	Bukkit.broadcastMessage("[RPS] "+ "Your version of Redstone Proximity Sensor is outdated");	
}
}


 
public void startUpdateCheck() {
if (updatechecker) {
try {
URL url = new URL(readurl);
BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
String str;
while ((str = br.readLine()) != null) {
if(str.startsWith(currentVersion)){
	outdated = false;
}
}
br.close();
} catch (IOException e) {
	Bukkit.broadcastMessage("The Update Checker URL is Invalid. Please let Ugleh know.");
}
}
}


}

