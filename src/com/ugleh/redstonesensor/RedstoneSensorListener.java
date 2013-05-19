package com.ugleh.redstonesensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RedstoneSensorListener implements Listener {
	public Location nullLocation = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0);
	public HashMap<String, ArrayList<Location>> playerLocationStorage = new HashMap<String, ArrayList<Location>>();
	private RedstoneSensor plugin;

	public RedstoneSensorListener(RedstoneSensor p) {
		plugin = p; 
	}
	
	@EventHandler
	public void BlockCreated(BlockPlaceEvent event) {
		if (event.getPlayer().hasPermission("redstonesensor.use")) {
			Block bk = event.getBlock();
			if ((ChatColor.RED + RedstoneSensor.redstoneProximityRangeText).equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(String.valueOf(RedstoneSensor.defaultRange));
				list.add(event.getPlayer().getName());
				list.add("null");
				String listKey = bk.getLocation().getWorld().getName();
				listKey += "|";
				listKey += String.valueOf(bk.getLocation().getBlockX());
				listKey += "|";
				listKey += String.valueOf(bk.getLocation().getBlockY());
				listKey += "|";
				listKey += String.valueOf(bk.getLocation().getBlockZ());
 
				RedstoneSensor.redstoneList.put(listKey, list);
				RedstoneSensor.redstoneList.put(listKey, list);
				String setname = "Redstones." + bk.getLocation().getWorld().getName() + "-" + bk.getLocation().getBlockX() + "" + bk.getLocation().getBlockY() + "" + bk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				plugin.getConfig().set(setname + ".Range", 3);
				plugin.getConfig().set(setname + ".CustomRange", null);
				plugin.getConfig().set(setname + ".Owner", event.getPlayer().getName());
				plugin.getConfig().set(setname + ".X", bk.getLocation().getBlockX());
				plugin.getConfig().set(setname + ".Y", bk.getLocation().getBlockY());
				plugin.getConfig().set(setname + ".Z", bk.getLocation().getBlockZ());
				plugin.getConfig().set(setname + ".World", bk.getLocation().getWorld().getName());
				try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if ((ChatColor.RED + RedstoneSensor.notRedstoneProximityRangeText).equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(String.valueOf(RedstoneSensor.defaultRange));
				list.add(event.getPlayer().getName());
				list.add("null");
				String listKey = bk.getLocation().getWorld().getName();
				listKey += "|";
				listKey += String.valueOf(bk.getLocation().getBlockX());
				listKey += "|";
				listKey += String.valueOf(bk.getLocation().getBlockY());
				listKey += "|";
				listKey += String.valueOf(bk.getLocation().getBlockZ());

				RedstoneSensor.notRedstoneList.put(listKey, list);
				RedstoneSensor.notRedstoneList.put(listKey, list);
				String setname = "Redstones." + bk.getLocation().getWorld().getName() + "-" + bk.getLocation().getBlockX() + "" + bk.getLocation().getBlockY() + "" + bk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				plugin.getConfig().set(setname + ".Range", 3);
				plugin.getConfig().set(setname + ".CustomRange", null);
				plugin.getConfig().set(setname + ".Owner", event.getPlayer().getName());
				plugin.getConfig().set(setname + ".Type", "NOT");
				plugin.getConfig().set(setname + ".X", bk.getLocation().getBlockX());
				plugin.getConfig().set(setname + ".Y", bk.getLocation().getBlockY());
				plugin.getConfig().set(setname + ".Z", bk.getLocation().getBlockZ());
				plugin.getConfig().set(setname + ".World", bk.getLocation().getWorld().getName());
				try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler
	public void InventoryClick(InventoryClickEvent event) {
		if (!event.getWhoClicked().hasPermission("redstonesensor.create")) {
			ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta = rps.getItemMeta();
			rpsmeta.setDisplayName(ChatColor.RED + RedstoneSensor.redstoneProximityRangeText);
			rps.setItemMeta(rpsmeta);

			ItemStack rps2 = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta2 = rps2.getItemMeta();
			rpsmeta2.setDisplayName(ChatColor.RED + RedstoneSensor.notRedstoneProximityRangeText);
			rps2.setItemMeta(rpsmeta2);

			if ((event.getResult().equals(rps2) || (event.getResult().equals(rps)))) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void ItemDrop(ItemSpawnEvent event) {
		Location newloc = new Location(event.getLocation().getWorld(), event.getLocation().getBlockX(), event.getLocation().getBlockY(), event.getLocation().getBlockZ());

		if (RedstoneSensor.redstoneList.containsKey(newloc)) {

			RedstoneSensor.redstoneList.remove(newloc);
			ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta = rps.getItemMeta();
			rpsmeta.setDisplayName(ChatColor.RED + RedstoneSensor.redstoneProximityRangeText);
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
		if (RedstoneSensor.notRedstoneList.containsKey(newloc)) {

			RedstoneSensor.notRedstoneList.remove(newloc);
			ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta = rps.getItemMeta();
			rpsmeta.setDisplayName(ChatColor.RED + RedstoneSensor.notRedstoneProximityRangeText);
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

	@EventHandler
	public boolean PlayerCommandPreprocess(PlayerCommandPreprocessEvent event) throws IOException {
		String cmd = event.getMessage();
		Player player = event.getPlayer();
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		List<String> arguments = new ArrayList<String>(Arrays.asList(cmd.split(" ")));
		if (arguments.size() > 0) {
			if ((arguments.get(0).matches("rps|redstonesensor|rsensor")) && (player.hasPermission("redstonesensor.commands"))) {
				if (arguments.size() == 1) {

					player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "/rps [maxrange/defaultrange/onlyowner/reload/wand/setrange]");
				}
				if (arguments.size() == 2) {
					if (("reload").equalsIgnoreCase(arguments.get(1))) {
						plugin.getServer().getPluginManager().disablePlugin(plugin);
						plugin.getServer().getPluginManager().enablePlugin(plugin);
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "Reloaded!");

					} else if (("defaultrange").equalsIgnoreCase(arguments.get(1))) {
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "/rps defaultrange <number>");
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "/rps maxrange <number>");
					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "/rps onlyowner <true/false>");
					} else if (("wand").equalsIgnoreCase(arguments.get(1))) {
						if (!player.hasPermission("redstonesensor.customrange")) {
							player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "You do not have permission to use that command");
						} else {
							ItemStack rps2 = new ItemStack(Material.STICK, 1);
							ItemMeta rpsmeta2 = rps2.getItemMeta();
							rpsmeta2.setDisplayName(ChatColor.RED + "RPS Wand");
							rps2.setItemMeta(rpsmeta2);
							player.getInventory().addItem(rps2);
							player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "RPS Wand Given. Left clicking a block with the RPS Wand marks that block as the first corner of the cuboid you wish to select. A right-click chooses the second corner.");
						}
					} else if (("setrange").equalsIgnoreCase(arguments.get(1))) {
						if (!player.hasPermission("redstonesensor.customrange")) {
							player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "You do not have permission to use that command");

						} else {
							if (playerLocationStorage.containsKey(event.getPlayer().getName())) {
								if (playerLocationStorage.get(event.getPlayer().getName()).get(0).equals(nullLocation) || playerLocationStorage.get(event.getPlayer().getName()).get(1).equals(nullLocation)) {
									player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "Your RPS Wand Selection is not complete. You are missing a corner.");

								} else {
									// Set Range here
									Block blk = event.getPlayer().getTargetBlock(null, 15);
									if (!(blk.getType().equals(Material.REDSTONE_TORCH_OFF) || blk.getType().equals(Material.REDSTONE_TORCH_ON))) {
										if (blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_TORCH_OFF) || blk.getRelative(BlockFace.UP).getType().equals(Material.REDSTONE_TORCH_ON)) {
											blk = blk.getRelative(BlockFace.UP);
										}
									}

									Location lok = blk.getLocation();
									String lk = lok.getWorld().getName();
									lk += "|";
									lk += String.valueOf(lok.getBlockX());
									lk += "|";
									lk += String.valueOf(lok.getBlockY());
									lk += "|";
									lk += String.valueOf(lok.getBlockZ());
									
									if (RedstoneSensor.redstoneList.containsKey(lk)) {
										// Add new range
										ArrayList<String> list = new ArrayList<String>();
										String setname = "Redstones." + lok.getWorld().getName() + "-" + lok.getBlockX() + "" + lok.getBlockY() + "" + lok.getBlockZ();
										Location l1 = playerLocationStorage.get(event.getPlayer().getName()).get(0);
										Location l2 = playerLocationStorage.get(event.getPlayer().getName()).get(1);
										int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
										int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
										int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
										int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
										int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
										int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
										l1 = new Location(l1.getWorld(), x1, y1, z1);
										l2 = new Location(l2.getWorld(), x2, y2, z2);
										String l1String = String.valueOf(l1.getBlockX()) + ":" + String.valueOf(l1.getBlockY()) + ":" + String.valueOf(l1.getBlockZ()) + ":" + String.valueOf(l1.getWorld().getName());
										String l2String = String.valueOf(l2.getBlockX()) + ":" + String.valueOf(l2.getBlockY()) + ":" + String.valueOf(l2.getBlockZ()) + ":" + String.valueOf(l2.getWorld().getName());
										plugin.getConfig().set(setname + ".Range", -999);
										plugin.getConfig().set(setname + ".CustomRange", l1String + ";" + l2String);
										list.add("-999");
										list.add(RedstoneSensor.redstoneList.get(lk).get(1));
										list.add(l1String + ";" + l2String);
										String listKey = lok.getWorld().getName();
										listKey += "|";
										listKey += String.valueOf(lok.getBlockX());
										listKey += "|";
										listKey += String.valueOf(lok.getBlockY());
										listKey += "|";
										listKey += String.valueOf(lok.getBlockZ());

										RedstoneSensor.redstoneList.put(listKey, list);
										RedstoneSensor.redstoneList.put(listKey, list);

										player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "New custom range set.");

										try {
											plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
										} catch (IOException e) {
											e.printStackTrace();
										}
										// End Add new range
									} else if (RedstoneSensor.notRedstoneList.containsKey(lk)) {
										// Add new range
										ArrayList<String> list = new ArrayList<String>();
										String setname = "Redstones." + lok.getWorld().getName() + "-" + lok.getBlockX() + "" + lok.getBlockY() + "" + lok.getBlockZ();
										Location l1 = playerLocationStorage.get(event.getPlayer().getName()).get(0);
										Location l2 = playerLocationStorage.get(event.getPlayer().getName()).get(1);
										int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
										int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
										int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
										int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
										int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
										int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
										l1 = new Location(l1.getWorld(), x1, y1, z1);
										l2 = new Location(l2.getWorld(), x2, y2, z2);
										String l1String = String.valueOf(l1.getBlockX()) + ":" + String.valueOf(l1.getBlockY()) + ":" + String.valueOf(l1.getBlockZ()) + ":" + String.valueOf(l1.getWorld().getName());
										String l2String = String.valueOf(l2.getBlockX()) + ":" + String.valueOf(l2.getBlockY()) + ":" + String.valueOf(l2.getBlockZ()) + ":" + String.valueOf(l2.getWorld().getName());
										plugin.getConfig().set(setname + ".Range", -999);
										plugin.getConfig().set(setname + ".CustomRange", l1String + ";" + l2String);
										list.add("-999");
										list.add(RedstoneSensor.notRedstoneList.get(lk).get(1));
										list.add(l1String + ";" + l2String);
										String listKey = lok.getWorld().getName();
										listKey += "|";
										listKey += String.valueOf(lok.getBlockX());
										listKey += "|";
										listKey += String.valueOf(lok.getBlockY());
										listKey += "|";
										listKey += String.valueOf(lok.getBlockZ());

										RedstoneSensor.notRedstoneList.put(listKey, list);
										RedstoneSensor.notRedstoneList.put(listKey, list);

										player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "New custom range set.");

										try {
											plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
										} catch (IOException e) {
											e.printStackTrace();
										}
									} else {
										player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "The block your aiming at is not a RPS. Try aiming at the block under the RPS, or the RPS itself.");

									}
								}
							} else {
								player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "You must first create a selection with the RPS Wand. Type '/rps wand' to get it");

							}
						}
					}
				} else if (arguments.size() == 3) {
					if (("defaultrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.defaultRange = Integer.valueOf(arguments.get(2));
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "Default range set to " + String.valueOf(Integer.valueOf(arguments.get(2))));
						plugin.getConfig().set("Config.default-range", RedstoneSensor.defaultRange);
						plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.maxRange = Integer.valueOf(arguments.get(2));
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "Max range set to " + String.valueOf(Integer.valueOf(arguments.get(2))));
						plugin.getConfig().set("Config.max-range", RedstoneSensor.maxRange);
						plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.onlyOwner = Boolean.valueOf(arguments.get(2));
						player.sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "Owner Only Right Click set to " + String.valueOf(Boolean.valueOf(arguments.get(2))));
						plugin.getConfig().set("Config.owner-only-change-range", RedstoneSensor.onlyOwner);
						plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));

					}
				}
			}
		}
		return true;
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		if(RedstoneSensor.updatechecker){
		if (event.getPlayer().isOp() && RedstoneSensor.outdated) {
			event.getPlayer().sendMessage(
					ChatColor.DARK_PURPLE + "The version of " + ChatColor.DARK_RED + "Redstone Proximity Sensor" + ChatColor.DARK_PURPLE + " that this server is running is out of date. Please consider updating to the latest version at " + ChatColor.ITALIC + ChatColor.GREEN
							+ "http://dev.bukkit.org/server-mods/redstonesensor/");
		}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerMove(PlayerMoveEvent event) {
		Iterator<Entry<String, ArrayList<String>>> it = RedstoneSensor.redstoneList.entrySet().iterator();
		while (it.hasNext()) {

			Entry< String, ArrayList<String>> entry = it.next();

			ArrayList<String> keys = new  ArrayList<String>(Arrays.asList(entry.getKey().split("\\|")));
			if(plugin.getServer().getWorld(keys.get(0)) == null)
				continue;
			Location key = new Location(plugin.getServer().getWorld(keys.get(0)),Integer.parseInt(keys.get(1)), Integer.parseInt(keys.get(2)), Integer.parseInt(keys.get(3)));
			Block blk = key.getBlock();
			if (blk.getType() == Material.REDSTONE_TORCH_ON || blk.getType() == Material.REDSTONE_TORCH_OFF) {
				Integer value = Integer.valueOf(entry.getValue().get(0));
				Location l1 = nullLocation;
				Location l2 = nullLocation;
				String customrange = entry.getValue().get(2);
				if (!customrange.equals("null")) {

					String[] firstloc = customrange.split(";")[0].split(":");
					String[] secondloc = customrange.split(";")[1].split(":");
					l1 = new Location(plugin.getServer().getWorld(firstloc[3]), Integer.valueOf(firstloc[0]), Integer.valueOf(firstloc[1]), Integer.valueOf(firstloc[2]));
					l2 = new Location(plugin.getServer().getWorld(secondloc[3]), Integer.valueOf(secondloc[0]), Integer.valueOf(secondloc[1]), Integer.valueOf(secondloc[2]));
				}
				Boolean playerinside = false;
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {

					if ((player.getWorld().getName() == key.getWorld().getName()) && (((value != -999) && (key.distance(player.getLocation()) <= value)) || ((value == -999) && playerWithin(l1, l2, player.getLocation())))) {
						playerinside = true;
						continue;
					}
				}
				if (playerinside == true) {
					if (blk.getType() == Material.REDSTONE_TORCH_OFF){
						BlockRedstoneEvent e = new BlockRedstoneEvent(blk, 15, 0);
						Bukkit.getServer().getPluginManager().callEvent(e);
						blk.setType(Material.REDSTONE_TORCH_ON);
						
					}
				} else {
					if (blk.getType() == Material.REDSTONE_TORCH_ON){
						blk.setType(Material.REDSTONE_TORCH_OFF);
					}
				}

			} else {
				it.remove();
				String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		Iterator<Entry<String, ArrayList<String>>> it2 = RedstoneSensor.notRedstoneList.entrySet().iterator();

		while (it2.hasNext()) {

			Entry< String, ArrayList<String>> entry = it2.next();

			ArrayList<String> keys = new  ArrayList<String>(Arrays.asList(entry.getKey().split("\\|")));
			if(plugin.getServer().getWorld(keys.get(0)) == null)
				continue;
			Location key = new Location(plugin.getServer().getWorld(keys.get(0)),Integer.parseInt(keys.get(1)), Integer.parseInt(keys.get(2)), Integer.parseInt(keys.get(3)));
			Block blk = key.getBlock();
			if (blk.getType() == Material.REDSTONE_TORCH_ON || blk.getType() == Material.REDSTONE_TORCH_OFF) {
				Integer value = Integer.valueOf(entry.getValue().get(0));
				Location l1 = nullLocation;
				Location l2 = nullLocation;
				String customrange = entry.getValue().get(2);
				if (!customrange.equals("null")) {

					String[] firstloc = customrange.split(";")[0].split(":");
					String[] secondloc = customrange.split(";")[1].split(":");
					l1 = new Location(plugin.getServer().getWorld(firstloc[3]), Integer.valueOf(firstloc[0]), Integer.valueOf(firstloc[1]), Integer.valueOf(firstloc[2]));
					l2 = new Location(plugin.getServer().getWorld(secondloc[3]), Integer.valueOf(secondloc[0]), Integer.valueOf(secondloc[1]), Integer.valueOf(secondloc[2]));
				}
				Boolean playerinside = false;
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {

					if ((player.getWorld().getName() == key.getWorld().getName()) && (((value != -999) && (key.distance(player.getLocation()) <= value)) || ((value == -999) && playerWithin(l1, l2, player.getLocation())))) {
						playerinside = true;
						continue;
					}
				}
				if (playerinside == true) {
					if (blk.getType() == Material.REDSTONE_TORCH_ON)
						blk.setType(Material.REDSTONE_TORCH_OFF);
				} else {
					if (blk.getType() == Material.REDSTONE_TORCH_OFF)
						blk.setType(Material.REDSTONE_TORCH_ON);
				}

			} else {
				it2.remove();
				String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				try {
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerRightClick(PlayerInteractEvent event) throws IOException {
		if (event.getPlayer().hasPermission("redstonesensor.use")) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block blk = event.getClickedBlock();
				String theList = blk.getWorld().getName();
				theList += "|";
				theList += String.valueOf(blk.getX());
				theList += "|";
				theList += String.valueOf(blk.getY());
				theList += "|";
				theList += String.valueOf(blk.getZ());
				if ((blk.getType() == Material.REDSTONE_TORCH_ON) || (blk.getType() == Material.REDSTONE_TORCH_OFF)) {
					if (RedstoneSensor.redstoneList.containsKey(theList)) {
						Integer value = Integer.valueOf(RedstoneSensor.redstoneList.get(theList).get(0));
						String playername = RedstoneSensor.redstoneList.get(theList).get(1);
						if (((RedstoneSensor.onlyOwner) && (playername.equals(event.getPlayer().getName()))) || (!RedstoneSensor.onlyOwner) || (playername.equals("null"))) {
							if (!value.toString().equals("-999")) {
								Integer newvalue = RedstoneSensor.defaultRange;
								if (value == RedstoneSensor.maxRange) {
									newvalue = 1;
								} else {
									newvalue = value + 1;
								}
								RedstoneSensor.redstoneList.get(theList).set(0, String.valueOf(newvalue));
								RedstoneSensor.redstoneList.get(theList).set(1, playername);
								RedstoneSensor.redstoneList.get(theList).set(2, "null");

								String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
								plugin.getConfig().set(setname + ".Range", newvalue);
								plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));

								event.getPlayer().sendMessage(ChatColor.GOLD + RedstoneSensor.redstoneProximityRangeNotifyText + ": " + ChatColor.RED + newvalue.toString());
							} else {
								event.getPlayer().sendMessage(ChatColor.GOLD + "This RPS has a Custom Range and can not be changed via right clicking.");
							}
						}

					}
					if (RedstoneSensor.notRedstoneList.containsKey(theList)) {
						Integer value = Integer.valueOf(RedstoneSensor.notRedstoneList.get(theList).get(0));
						String playername = RedstoneSensor.notRedstoneList.get(theList).get(1);
						if (((RedstoneSensor.onlyOwner) && (playername.equals(event.getPlayer().getName()))) || (!RedstoneSensor.onlyOwner) || (playername.equals("null"))) {
							if (!value.toString().equals("-999")) {
								Integer newvalue = RedstoneSensor.defaultRange;
								if (value == RedstoneSensor.maxRange) {
									newvalue = 1;
								} else {
									newvalue = value + 1;
								}
								RedstoneSensor.notRedstoneList.get(theList).set(0, String.valueOf(newvalue));
								RedstoneSensor.notRedstoneList.get(theList).set(1, playername);
								RedstoneSensor.notRedstoneList.get(theList).set(2, "null");

								String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
								plugin.getConfig().set(setname + ".Range", newvalue);
								plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));

								event.getPlayer().sendMessage(ChatColor.GOLD + RedstoneSensor.redstoneProximityRangeNotifyText + ": " + ChatColor.RED + newvalue.toString());
							} else {
								event.getPlayer().sendMessage(ChatColor.GOLD + "This RPS has a Custom Range and can not be changed via right clicking.");
							}
						}

					}
				}
			}
		}

		// Player Location Set Check
		if (!(event.getPlayer().getItemInHand().getTypeId() == 0)) {
			if ((ChatColor.RED + "RPS Wand").equals(event.getPlayer().getItemInHand().getItemMeta().getDisplayName())) {
				// User has RPS Wand out.
				if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					ArrayList<Location> locations = new ArrayList<Location>();
					locations.add(event.getClickedBlock().getLocation()); // Left click
					if (playerLocationStorage.containsKey(event.getPlayer().getName()))
						locations.add(playerLocationStorage.get(event.getPlayer().getName()).get(1)); // Right click
					else
						locations.add(nullLocation); // Right Click

					event.getPlayer().sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "First corner set (" + event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getLocation().getBlockZ() + ")");
					playerLocationStorage.put(event.getPlayer().getName(), locations);
				} else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					ArrayList<Location> locations = new ArrayList<Location>();
					if (playerLocationStorage.containsKey(event.getPlayer().getName()))
						locations.add(playerLocationStorage.get(event.getPlayer().getName()).get(0)); // Left click
					else
						locations.add(nullLocation); // Right Click

					locations.add(event.getClickedBlock().getLocation()); // Right click
					event.getPlayer().sendMessage(ChatColor.RED + "[RPS] " + ChatColor.GREEN + "Second corner set (" + event.getClickedBlock().getX() + "," + event.getClickedBlock().getY() + "," + event.getClickedBlock().getLocation().getBlockZ() + ")");
					playerLocationStorage.put(event.getPlayer().getName(), locations);

				}
				if (playerLocationStorage.containsKey(event.getPlayer().getName())) {
					if ((!playerLocationStorage.get(event.getPlayer().getName()).get(0).equals(nullLocation)) && (!playerLocationStorage.get(event.getPlayer().getName()).get(1).equals(nullLocation))) {
						event.getPlayer().sendMessage(ChatColor.RED + "[RPS] " + ChatColor.AQUA + "Both corners have been set. Aim at an RPS and type '/rps setrange' to set its new range to the current cuboid area.");
					}
				}
				event.setCancelled(true);
			}
		}
	}

	public boolean playerWithin(Location l1, Location l2, Location pLoc) {
		if (l1.equals(nullLocation) || l2.equals(nullLocation)) {
			return false;
		} else {
			return pLoc.getBlockX() >= l1.getBlockX() && pLoc.getBlockX() <= l2.getBlockX() && pLoc.getBlockY() >= l1.getBlockY() && pLoc.getBlockY() <= l2.getBlockY() && pLoc.getBlockZ() >= l1.getBlockZ() && pLoc.getBlockZ() <= l2.getBlockZ();
		}
	}

	@EventHandler
	public void RedEvent(BlockRedstoneEvent event) {
		Block blk = event.getBlock();
		for (Entry<String, ArrayList<String>> entry : RedstoneSensor.redstoneList.entrySet()) {
			ArrayList<String> keys = new  ArrayList<String>(Arrays.asList(entry.getKey().split("\\|")));
			if(plugin.getServer().getWorld(keys.get(0)) == null)
				continue;
			Location key = new Location(plugin.getServer().getWorld(keys.get(0)),Integer.parseInt(keys.get(1)), Integer.parseInt(keys.get(2)), Integer.parseInt(keys.get(3)));
			if (blk.getLocation().equals(key)) {
				event.setNewCurrent(event.getOldCurrent());
			}
		}
		for (Entry<String, ArrayList<String>> entry : RedstoneSensor.notRedstoneList.entrySet()) {
			ArrayList<String> keys = new  ArrayList<String>(Arrays.asList(entry.getKey().split("\\|")));
			if(plugin.getServer().getWorld(keys.get(0)) == null)
				continue;
			Location key = new Location(plugin.getServer().getWorld(keys.get(0)),Integer.parseInt(keys.get(1)), Integer.parseInt(keys.get(2)), Integer.parseInt(keys.get(3)));
			if (blk.getLocation().equals(key)) {
				event.setNewCurrent(event.getOldCurrent());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	// must be high priority
	public void RemoveRedstone(BlockBreakEvent event) throws IOException {
		Block blk = event.getBlock();
		if ((blk.getType() == Material.REDSTONE_TORCH_ON) || (blk.getType() == Material.REDSTONE_TORCH_OFF)) {
			Iterator<Entry<String, ArrayList<String>>> it = RedstoneSensor.redstoneList.entrySet().iterator();

			while (it.hasNext()) {
				Entry<String, ArrayList<String>> item = it.next();
				ArrayList<String> keys = new  ArrayList<String>(Arrays.asList(item.getKey().split("\\|")));
				if(plugin.getServer().getWorld(keys.get(0)) == null)
					continue;
				Location key = new Location(plugin.getServer().getWorld(keys.get(0)),Integer.parseInt(keys.get(1)), Integer.parseInt(keys.get(2)), Integer.parseInt(keys.get(3)));
				if (blk.getLocation().equals(key)) {
					it.remove();
					String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
					plugin.getConfig().set(setname, null);
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
					ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
					ItemMeta rpsmeta = rps.getItemMeta();
					rpsmeta.setDisplayName(ChatColor.RED + RedstoneSensor.redstoneProximityRangeText);
					rps.setItemMeta(rpsmeta);
					blk.setType(Material.AIR);
					blk.getWorld().dropItemNaturally(blk.getLocation(), rps);
					event.setCancelled(true);
				}
			}
			Iterator<Entry<String, ArrayList<String>>> it2 = RedstoneSensor.notRedstoneList.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<String, ArrayList<String>> item = it2.next();
				ArrayList<String> keys = new  ArrayList<String>(Arrays.asList(item.getKey().split("\\|")));
				if(plugin.getServer().getWorld(keys.get(0)) == null)
					continue;
				Location key = new Location(plugin.getServer().getWorld(keys.get(0)),Integer.parseInt(keys.get(1)), Integer.parseInt(keys.get(2)), Integer.parseInt(keys.get(3)));
				if (blk.getLocation().equals(key)) {
					it2.remove();
					String setname = "Redstones." + blk.getLocation().getWorld().getName() + "-" + blk.getLocation().getBlockX() + "" + blk.getLocation().getBlockY() + "" + blk.getLocation().getBlockZ();
					plugin.getConfig().set(setname, null);
					plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
					ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
					ItemMeta rpsmeta = rps.getItemMeta();
					rpsmeta.setDisplayName(ChatColor.RED + RedstoneSensor.notRedstoneProximityRangeText);
					rps.setItemMeta(rpsmeta);
					blk.setType(Material.AIR);
					blk.getWorld().dropItemNaturally(blk.getLocation(), rps);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public boolean ServerCommand(ServerCommandEvent event) throws IOException {
		String cmd = event.getCommand();
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		List<String> arguments = new ArrayList<String>(Arrays.asList(cmd.split(" ")));
		if (arguments.size() > 0) {
			if (arguments.get(0).matches("rps|redstonesensor|rsensor")) {
				if (arguments.size() == 1) {

					System.out.print("[RPS] " + "/rps [maxrange/defaultrange/onlyowner/reload]");
				}
				if (arguments.size() == 2) {
					if (("reload").equalsIgnoreCase(arguments.get(1))) {
						plugin.getServer().getPluginManager().disablePlugin(plugin);
						plugin.getServer().getPluginManager().enablePlugin(plugin);
						System.out.print("[RPS] " + "Reloaded!");

					} else if (("defaultrange").equalsIgnoreCase(arguments.get(1))) {
						System.out.print("[RPS] " + "/rps defaultrange <number>");
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						System.out.print("[RPS] " + "/rps maxrange <number>");
					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						System.out.print("[RPS] " + "/rps onlyowner <true/false>");
					}
				} else if (arguments.size() == 3) {
					if (("defaultrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.defaultRange = Integer.valueOf(arguments.get(2));
						System.out.print("[RPS] " + "Default range set to " + String.valueOf(Integer.valueOf(arguments.get(2))));
						plugin.getConfig().set("Config.default-range", RedstoneSensor.defaultRange);
						plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.maxRange = Integer.valueOf(arguments.get(2));
						System.out.print("[RPS] " + "Max range set to " + String.valueOf(Integer.valueOf(arguments.get(2))));
						plugin.getConfig().set("Config.max-range", RedstoneSensor.maxRange);
						plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));

					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.onlyOwner = Boolean.valueOf(arguments.get(2));
						System.out.print("[RPS] " + "Only Owner Can Change Range set to " + String.valueOf(Boolean.valueOf(arguments.get(2))));
						plugin.getConfig().set("Config.owner-only-change-range", RedstoneSensor.onlyOwner);
						plugin.getConfig().save(new File(plugin.getDataFolder(), "config.yml"));

					}
				}
			}
		}
		return true;
	}
}