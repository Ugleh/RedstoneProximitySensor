package com.ugleh.redstonesensor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RedstoneSensor extends JavaPlugin{
public static HashMap<Location, Integer> redstoneList = new HashMap<Location, Integer>();
public void onEnable(){
	getServer().getPluginManager().registerEvents(new RedstoneSensorListener(this), this);	
    if (!new File(getDataFolder(), "config.yml").exists()) {
    saveDefaultConfig();
    }else{
		 try{

    	if(!getConfig().toString().isEmpty()){
    for(String key : getConfig().getConfigurationSection("Redstones").getKeys(false)) {
        redstoneList.put(new Location(getServer().getWorld(getConfig().getString("Redstones."+key+".World")), getConfig().getInt("Redstones."+key+".X"), getConfig().getInt("Redstones."+key+".Y"), getConfig().getInt("Redstones."+key+".Z")), getConfig().getInt("Redstones."+key+".Range"));

    }
    }
		 } catch(Exception e){
		 }
		 }
    
	 ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
		ItemMeta rpsmeta = rps.getItemMeta();
		ArrayList<String> rpsLore = new ArrayList<String>();
		rpsLore.add("Version 1.6");
		rpsmeta.setLore(rpsLore);
		rpsmeta.setDisplayName(ChatColor.RED + "Redstone Proximity Sensor");
		rps.setItemMeta(rpsmeta);
        ShapelessRecipe flashRecipe = new ShapelessRecipe(rps).addIngredient(1, Material.REDSTONE_TORCH_ON).addIngredient(1, Material.REDSTONE_TORCH_ON).addIngredient(1, Material.REDSTONE_TORCH_ON);
        getServer().addRecipe(flashRecipe);

		
    }

}
