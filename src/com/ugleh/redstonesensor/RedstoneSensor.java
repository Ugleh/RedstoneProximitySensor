package com.ugleh.redstonesensor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
//import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
public class RedstoneSensor extends JavaPlugin {
	public static Integer defaultRange = null;
	public static Integer maxRange = null;
	public static HashMap<String, ArrayList<String>> notRedstoneList = new HashMap<String, ArrayList<String>>();
	public static String notRedstoneProximityRangeText = null;
	public static Boolean onlyOwner = null;
	public static Boolean outdated = false;
	public static HashMap<String, ArrayList<String>> redstoneList = new HashMap<String, ArrayList<String>>();
	public static String redstoneProximityRangeNotifyText = null;
	public static String redstoneProximityRangeText = null;
	
	public static Map<String,String> unLoadedRedstoneList = new HashMap<String, String>();
	public static Map<String,String> unLoadedNotRedstoneList = new HashMap<String, String>();

	private String currentVersion = "1.9.9";

	private String readurl = "https://raw.github.com/Ugleh/RedstoneSensor/master/version.txt";
	public Boolean updatechecker;

	@Override
	public void onEnable() {

		getServer().getPluginManager().registerEvents(new RedstoneSensorListener(this), this);
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		// Basic config settings below to make sure they have certian configs
		// and if they dont then add them.
		maxRange = getConfig().getInt("Config.max-range");
		defaultRange = getConfig().getInt("Config.default-range");
		onlyOwner = getConfig().getBoolean("Config.owner-only-change-range");
		redstoneProximityRangeText = getConfig().getString("Config.proximity-sensor-name");
		notRedstoneProximityRangeText = getConfig().getString("Config.not-proximity-sensor-name");
		redstoneProximityRangeNotifyText = getConfig().getString("Config.proximity-range-notify-text");
		updatechecker = getConfig().getBoolean("Config.update-checker");
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(getConfig().getConfigurationSection("Config").getKeys(false));
		if (!keys.contains("update-checker")) {
			updatechecker = true;
			getConfig().set("Config.update-checker", true);
		}
		if (!keys.contains("max-range")) {
			maxRange = 10;
			getConfig().set("Config.max-range", 10);
		}
		if (!keys.contains("owner-only-change-rank")) {
			onlyOwner = true;
			getConfig().set("Config.owner-only-change-range", true);
		}
		if (!keys.contains("default-range")) {
			defaultRange = 3;
			getConfig().set("Config.default-range", 3);
		}
		if (!keys.contains("proximity-sensor-name")) {
			redstoneProximityRangeText = "Redstone Proximity Sensor";
			getConfig().set("Config.proximity-sensor-name", "Redstone Proximity Sensor");
		}
		if (!keys.contains("not-proximity-sensor-name")) {
			notRedstoneProximityRangeText = "NOT Redstone Proximity Sensor";
			getConfig().set("Config.not-proximity-sensor-name", "NOT Redstone Proximity Sensor");
		}
		if (!keys.contains("proximity-range-notify-text")) {
			redstoneProximityRangeNotifyText = "Proximity Range";
			getConfig().set("Config.proximity-range-notify-text", "Proximity Range");
		}
		try {
			getConfig().save(new File(getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!getConfig().toString().isEmpty()) {
			for (String key : getConfig().getConfigurationSection("Redstones").getKeys(false)) {
				if (("NOT").equalsIgnoreCase((getConfig().getString("Redstones." + key + ".Type")))) {
					ArrayList<String> list = new ArrayList<String>();
					String customrange = String.valueOf(getConfig().getString("Redstones." + key + ".CustomRange"));
					if (customrange.equals("null")) {
						list.add(String.valueOf(getConfig().getInt("Redstones." + key + ".Range")));
					} else {
						list.add("-999");
					}
					list.add(String.valueOf(getConfig().getString("Redstones." + key + ".Owner")));
					list.add(customrange);
					
					String listKey = getConfig().getString("Redstones." + key + ".World");
					listKey += "|";
					listKey += String.valueOf(getConfig().getInt("Redstones." + key + ".X"));
					listKey += "|";
					listKey += String.valueOf(getConfig().getInt("Redstones." + key + ".Y"));
					listKey += "|";
					listKey += String.valueOf(getConfig().getInt("Redstones." + key + ".Z"));
					notRedstoneList.put(listKey, list);
					} else {
					ArrayList<String> list = new ArrayList<String>();
					String customrange = String.valueOf(getConfig().getString("Redstones." + key + ".CustomRange"));
					if (customrange.equals("null")) {
						list.add(String.valueOf(getConfig().getInt("Redstones." + key + ".Range")));
					} else {
						list.add("-999");
					}
					list.add(String.valueOf(getConfig().getString("Redstones." + key + ".Owner")));
					list.add(customrange);
				//	if(getServer().getWorld(getConfig().getString("Redstones." + key + ".World")) == null){
						//getWorld is null
					//	unLoadedRedstoneList.put("Redstones." + key, getConfig().getString("Redstones." + key + ".World"));
					//}else{
					String listKey = getConfig().getString("Redstones." + key + ".World");
					listKey += "|";
					listKey += String.valueOf(getConfig().getInt("Redstones." + key + ".X"));
					listKey += "|";
					listKey += String.valueOf(getConfig().getInt("Redstones." + key + ".Y"));
					listKey += "|";
					listKey += String.valueOf(getConfig().getInt("Redstones." + key + ".Z"));
					redstoneList.put(listKey, list);

				//	}
				}
			}
		}

		// Crafting Recipe for the regular Proximity Sensor
		ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta = rps.getItemMeta();
		rpsmeta.setDisplayName(ChatColor.RED + redstoneProximityRangeText);
		rps.setItemMeta(rpsmeta);
		ShapedRecipe rpsRecipe = new ShapedRecipe(rps);
		rpsRecipe.shape(" R ", " R ", " R ");
		rpsRecipe.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe);

		// Crafting Recipe for the Inverted or NOT Proximity Sensor.
		ItemStack rps2 = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta2 = rps2.getItemMeta();
		rpsmeta2.setDisplayName(ChatColor.RED + notRedstoneProximityRangeText);
		rps2.setItemMeta(rpsmeta2);
		ShapedRecipe rpsRecipe2 = new ShapedRecipe(rps2);
		rpsRecipe2.shape("   ", "RRR", "   ");
		rpsRecipe2.setIngredient('R', Material.REDSTONE_TORCH_ON);
		this.getServer().addRecipe(rpsRecipe2);

		outdated = true;
		startUpdateCheck();
		if (outdated) {
			Bukkit.broadcastMessage("[RPS] " + "Your version of Redstone Proximity Sensor is outdated");
		}
	}

	public boolean playerWithin(Location l1, Location l2, Location pLoc) {
		int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
		int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
		int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
		int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
		int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
		int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		l1 = new Location(l1.getWorld(), x1, y1, z1);
		l2 = new Location(l2.getWorld(), x2, y2, z2);

		return pLoc.getBlockX() >= l1.getBlockX() && pLoc.getBlockX() <= l2.getBlockX() && pLoc.getBlockY() >= l1.getBlockY() && pLoc.getBlockY() <= l2.getBlockY() && pLoc.getBlockZ() >= l1.getBlockZ() && pLoc.getBlockZ() <= l2.getBlockZ();
	}

	public void startUpdateCheck() {
		if (updatechecker) {
			try {
				URL url = new URL(readurl);
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
				String str;
				while ((str = br.readLine()) != null) {
					if (str.startsWith(currentVersion)) {
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
