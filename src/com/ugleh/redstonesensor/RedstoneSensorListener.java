package com.ugleh.redstonesensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RedstoneSensorListener implements Listener{
	private RedstoneSensor plugin;
	 
	public RedstoneSensorListener(RedstoneSensor p) {
	    plugin = p;
	}

	@EventHandler
	public void BlockCreated(BlockPlaceEvent event){
		if((event.getPlayer().hasPermission("redstonesensor.use")) || event.getPlayer().hasPermission("redstonesensor.*")){
			Block bk = event.getBlock();
			if((ChatColor.RED + "Redstone Proximity Sensor").equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())){
				 RedstoneSensor.redstoneList.put(bk.getLocation(), 3);
				String setname = "Redstones."+bk.getLocation().getWorld().getName() + "-" + bk.getLocation().getBlockX() + "" + bk.getLocation().getBlockY() + "" + bk.getLocation().getBlockZ();
				 plugin.getConfig().set(setname, null);
				 plugin.getConfig().set(setname+".Range", (int)3);
				 plugin.getConfig().set(setname+".X", bk.getLocation().getBlockX());
				 plugin.getConfig().set(setname+".Y", bk.getLocation().getBlockY());
				 plugin.getConfig().set(setname+".Z", bk.getLocation().getBlockZ());
				 plugin.getConfig().set(setname+".World", bk.getLocation().getWorld().getName());
				 try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 }
	}
	
	@EventHandler
	public void PlayerMove(PlayerMoveEvent event){
		String worldname = event.getPlayer().getWorld().getName();
	    for (Entry<Location, Integer> entry : RedstoneSensor.redstoneList.entrySet()) {
	    	//World w = event.getPlayer().getWorld();
			Location key = entry.getKey();
			Block blk = key.getBlock();
	    	//Entity ent = w.spawnEntity(key, EntityType.ARROW);
		    Integer value = entry.getValue();
		    if(worldname == key.getWorld().getName()){
		    	Boolean towel = false; 
		    	for(Player player : Bukkit.getServer().getOnlinePlayers()){
					if((key.distance(player.getLocation()) <= value)){
						towel = true;
						continue;
					}
		    	}
		    	if(towel == true){
				if(blk.getType() != Material.REDSTONE_TORCH_ON)
				blk.setType(Material.REDSTONE_TORCH_ON);
			}else{
			if(blk.getType() != Material.REDSTONE_TORCH_OFF)
				blk.setType(Material.REDSTONE_TORCH_OFF);
			}
		    }
	    }
	}
	

	@EventHandler
	public void RedEvent(BlockRedstoneEvent event){
		Block blk = event.getBlock();
		for (Entry<Location, Integer> entry : RedstoneSensor.redstoneList.entrySet()) {
			Location key = entry.getKey();
		if((blk.getLocation().getBlockX() == key.getBlockX()) && (blk.getLocation().getBlockY() == key.getBlockY()) && (blk.getLocation().getBlockZ() == key.getBlockZ())){
		event.setNewCurrent(event.getOldCurrent());
		}
}
	}
	
	@EventHandler
	public void RemoveRedstone(BlockBreakEvent event) throws IOException{
		Block blk = event.getBlock();
		if((blk.getType() == Material.REDSTONE_TORCH_ON) || (blk.getType() == Material.REDSTONE_TORCH_OFF)){
			Iterator<Entry<Location, Integer>> it = RedstoneSensor.redstoneList.entrySet().iterator();
			
			while (it.hasNext())
			{
			   Entry<Location, Integer> item = it.next();
			   Location key = item.getKey();
				if((blk.getLocation().getBlockX() == key.getBlockX()) && (blk.getLocation().getBlockY() == key.getBlockY()) && (blk.getLocation().getBlockZ() == key.getBlockZ())){
					it.remove();
					 String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
					 plugin.getConfig().set(setname, null);
					 plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
					 event.getBlock().setType(Material.AIR);
					 //ItemStack redwool = new ItemStack( Material.WOOL, 1, (byte)14 );
					 //ItemStack wood = new ItemStack( Material.WOOD, 2);
					 //key.getBlock().getWorld().dropItem(key, redwool);
					 //key.getBlock().getWorld().dropItem(key, wood);
					 ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
						ItemMeta rpsmeta = rps.getItemMeta();
						ArrayList<String> rpsLore = new ArrayList<String>();
						rpsLore.add("Version 1.6");
						rpsmeta.setLore(rpsLore);
						rpsmeta.setDisplayName(ChatColor.RED + "Redstone Proximity Sensor");
						rps.setItemMeta(rpsmeta);
						key.getBlock().getWorld().dropItem(key, rps);
					 event.setCancelled(true);
				}
			}
			}
		}
	
	@EventHandler
	public void PlayerRightClick(PlayerInteractEvent event) throws IOException{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
		Block blk = event.getClickedBlock();
		if((blk.getType() == Material.REDSTONE_TORCH_ON) || (blk.getType() == Material.REDSTONE_TORCH_OFF)){
		for (Entry<Location, Integer> entry : RedstoneSensor.redstoneList.entrySet()) {
			Location key = entry.getKey();
			Integer value = entry.getValue();
		if((blk.getLocation().getBlockX() == key.getBlockX()) && (blk.getLocation().getBlockY() == key.getBlockY()) && (blk.getLocation().getBlockZ() == key.getBlockZ())){
			Integer newvalue = 3;
			if(value == 10){
				newvalue = 1;
				entry.setValue(newvalue);
				}else{
				newvalue = value+1;
					entry.setValue(newvalue);
			}
			 String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
			 plugin.getConfig().set(setname+".Range", newvalue);
			 plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));

			event.getPlayer().sendMessage(ChatColor.GOLD + "Proximity Range: " + ChatColor.RED + newvalue.toString());
		}
		}
		}
	}
	}
	
	
	@EventHandler
	public void ItemDrop(ItemSpawnEvent event){
		Location newloc = new Location(event.getLocation().getWorld(),event.getLocation().getBlockX(),event.getLocation().getBlockY(),event.getLocation().getBlockZ());
		if(RedstoneSensor.redstoneList.containsKey(newloc)){
			RedstoneSensor.redstoneList.remove(newloc);
			 ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
				ItemMeta rpsmeta = rps.getItemMeta();
				ArrayList<String> rpsLore = new ArrayList<String>();
				rpsLore.add("Version 1.6");
				rpsmeta.setLore(rpsLore);
				rpsmeta.setDisplayName(ChatColor.RED + "Redstone Proximity Sensor");
				rps.setItemMeta(rpsmeta);
				event.getLocation().getBlock().getWorld().dropItem(event.getLocation(), rps);
			
			 String setname = "Redstones." + newloc.getWorld().getName() + "-" + newloc.getBlockX() + "" + newloc.getBlockY() + "" + newloc.getBlockZ();
			 plugin.getConfig().set(setname, null);
			 try {
				plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		event.setCancelled(true);
		}
	}
	}