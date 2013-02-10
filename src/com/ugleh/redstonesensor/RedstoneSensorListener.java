package com.ugleh.redstonesensor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RedstoneSensorListener implements Listener {
	private RedstoneSensor plugin;

	public RedstoneSensorListener(RedstoneSensor p) {
		plugin = p;
	}

	@EventHandler
	public void InventoryClick(InventoryClickEvent event) {
		if (!event.getWhoClicked().hasPermission("redstonesensor.create")) {
			ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta = rps.getItemMeta();
			rpsmeta.setDisplayName(ChatColor.RED
					+ RedstoneSensor.redstoneProximityRangeText);
			rps.setItemMeta(rpsmeta);

			ItemStack rps2 = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta2 = rps2.getItemMeta();
			rpsmeta2.setDisplayName(ChatColor.RED
					+ RedstoneSensor.notRedstoneProximityRangeText);
			rps2.setItemMeta(rpsmeta2);

			if ((event.getResult().equals(rps2) || (event.getResult()
					.equals(rps)))) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void BlockCreated(BlockPlaceEvent event) {
		if (event.getPlayer().hasPermission("redstonesensor.use")) {
			Block bk = event.getBlock();
			if ((ChatColor.RED + RedstoneSensor.redstoneProximityRangeText)
					.equals(event.getPlayer().getItemInHand().getItemMeta()
							.getDisplayName())) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(String.valueOf(RedstoneSensor.defaultRange));
				list.add(event.getPlayer().getName());
				RedstoneSensor.redstoneList.put(bk.getLocation(), list);
				String setname = "Redstones."
						+ bk.getLocation().getWorld().getName() + "-"
						+ bk.getLocation().getBlockX() + ""
						+ bk.getLocation().getBlockY() + ""
						+ bk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				plugin.getConfig().set(setname + ".Range", (int) 3);
				plugin.getConfig().set(setname + ".Owner",
						event.getPlayer().getName());
				plugin.getConfig().set(setname + ".X",
						bk.getLocation().getBlockX());
				plugin.getConfig().set(setname + ".Y",
						bk.getLocation().getBlockY());
				plugin.getConfig().set(setname + ".Z",
						bk.getLocation().getBlockZ());
				plugin.getConfig().set(setname + ".World",
						bk.getLocation().getWorld().getName());
				try {
					plugin.getConfig().save(
							new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if ((ChatColor.RED + RedstoneSensor.notRedstoneProximityRangeText)
					.equals(event.getPlayer().getItemInHand().getItemMeta()
							.getDisplayName())) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(String.valueOf(RedstoneSensor.defaultRange));
				list.add(event.getPlayer().getName());

				RedstoneSensor.notRedstoneList.put(bk.getLocation(), list);
				String setname = "Redstones."
						+ bk.getLocation().getWorld().getName() + "-"
						+ bk.getLocation().getBlockX() + ""
						+ bk.getLocation().getBlockY() + ""
						+ bk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				plugin.getConfig().set(setname + ".Range", (int) 3);
				plugin.getConfig().set(setname + ".Owner",
						event.getPlayer().getName());
				plugin.getConfig().set(setname + ".Type", "NOT");
				plugin.getConfig().set(setname + ".X",
						bk.getLocation().getBlockX());
				plugin.getConfig().set(setname + ".Y",
						bk.getLocation().getBlockY());
				plugin.getConfig().set(setname + ".Z",
						bk.getLocation().getBlockZ());
				plugin.getConfig().set(setname + ".World",
						bk.getLocation().getWorld().getName());
				try {
					plugin.getConfig().save(
							new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerMove(PlayerMoveEvent event) {

		String worldname = event.getPlayer().getWorld().getName();
		Player player = event.getPlayer();
		Iterator<Entry<Location, ArrayList<String>>> it = RedstoneSensor.redstoneList
				.entrySet().iterator();

		while (it.hasNext()) {
			Entry<Location, ArrayList<String>> entry = it.next();
			Location key = entry.getKey();
			Block blk = key.getBlock();
			if (blk.getType() == Material.REDSTONE_TORCH_ON
					|| blk.getType() == Material.REDSTONE_TORCH_OFF) {
				Integer value = Integer.valueOf(entry.getValue().get(0));
				if (worldname == key.getWorld().getName()) {
					if ((player.getWorld() == key.getWorld())
							&& (key.distance(player.getLocation()) <= value)) {
						if (blk.getType() == Material.REDSTONE_TORCH_OFF)
							blk.setType(Material.REDSTONE_TORCH_ON);
					} else {
						if (blk.getType() == Material.REDSTONE_TORCH_ON)
							blk.setType(Material.REDSTONE_TORCH_OFF);
					}
				}

			} else {
				it.remove();
				String setname = "Redstones."
						+ blk.getLocation().getWorld().getName() + "-"
						+ blk.getLocation().getBlockX() + ""
						+ blk.getLocation().getBlockY() + ""
						+ blk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				try {
					plugin.getConfig().save(
							new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		Iterator<Entry<Location, ArrayList<String>>> it2 = RedstoneSensor.notRedstoneList
				.entrySet().iterator();

		while (it2.hasNext()) {
			Entry<Location, ArrayList<String>> entry = it2.next();
			Location key = entry.getKey();
			Block blk = key.getBlock();
			if (blk.getType() == Material.REDSTONE_TORCH_ON
					|| blk.getType() == Material.REDSTONE_TORCH_OFF) {
				ArrayList<String> mappie = entry.getValue();
				Integer value = Integer.valueOf(mappie.get(0));
				if (worldname == key.getWorld().getName()) {
					if ((player.getWorld() == key.getWorld())
							&& (key.distance(player.getLocation()) <= value)) {
						if (blk.getType() == Material.REDSTONE_TORCH_OFF)
							blk.setType(Material.REDSTONE_TORCH_ON);
					} else {
						if (blk.getType() == Material.REDSTONE_TORCH_ON)
							blk.setType(Material.REDSTONE_TORCH_OFF);
					}
				}
			} else {
				it2.remove();
				String setname = "Redstones."
						+ blk.getLocation().getWorld().getName() + "-"
						+ blk.getLocation().getBlockX() + ""
						+ blk.getLocation().getBlockY() + ""
						+ blk.getLocation().getBlockZ();
				plugin.getConfig().set(setname, null);
				try {
					plugin.getConfig().save(
							new File(plugin.getDataFolder(), "config.yml"));
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@EventHandler
	public void RedEvent(BlockRedstoneEvent event) {
		Block blk = event.getBlock();
		for (Entry<Location, ArrayList<String>> entry : RedstoneSensor.redstoneList
				.entrySet()) {
			Location key = entry.getKey();
			if (blk.getLocation().equals(key)) {
				event.setNewCurrent(event.getOldCurrent());
			}
		}
		for (Entry<Location, ArrayList<String>> entry : RedstoneSensor.notRedstoneList
				.entrySet()) {
			Location key = entry.getKey();
			if (blk.getLocation().equals(key)) {
				event.setNewCurrent(event.getOldCurrent());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	// must be high priority
	public void RemoveRedstone(BlockBreakEvent event) throws IOException {
		Block blk = event.getBlock();
		if ((blk.getType() == Material.REDSTONE_TORCH_ON)
				|| (blk.getType() == Material.REDSTONE_TORCH_OFF)) {
			Iterator<Entry<Location, ArrayList<String>>> it = RedstoneSensor.redstoneList
					.entrySet().iterator();

			while (it.hasNext()) {
				Entry<Location, ArrayList<String>> item = it.next();
				Location key = item.getKey();
				if (blk.getLocation().equals(key)) {
					it.remove();
					String setname = "Redstones."
							+ blk.getLocation().getWorld().getName() + "-"
							+ blk.getLocation().getBlockX() + ""
							+ blk.getLocation().getBlockY() + ""
							+ blk.getLocation().getBlockZ();
					plugin.getConfig().set(setname, null);
					plugin.getConfig().save(
							new File(plugin.getDataFolder(), "config.yml"));
					ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF,
							1);
					ItemMeta rpsmeta = rps.getItemMeta();
					rpsmeta.setDisplayName(ChatColor.RED
							+ RedstoneSensor.redstoneProximityRangeText);
					rps.setItemMeta(rpsmeta);
					blk.setType(Material.AIR);
					blk.getWorld().dropItemNaturally(blk.getLocation(), rps);
					event.setCancelled(true);
				}
			}
			Iterator<Entry<Location, ArrayList<String>>> it2 = RedstoneSensor.notRedstoneList
					.entrySet().iterator();
			while (it2.hasNext()) {
				Entry<Location, ArrayList<String>> item = it2.next();
				Location key = item.getKey();
				if (blk.getLocation().equals(key)) {
					it2.remove();
					String setname = "Redstones."
							+ blk.getLocation().getWorld().getName() + "-"
							+ blk.getLocation().getBlockX() + ""
							+ blk.getLocation().getBlockY() + ""
							+ blk.getLocation().getBlockZ();
					plugin.getConfig().set(setname, null);
					plugin.getConfig().save(
							new File(plugin.getDataFolder(), "config.yml"));
					ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF,
							1);
					ItemMeta rpsmeta = rps.getItemMeta();
					rpsmeta.setDisplayName(ChatColor.RED
							+ RedstoneSensor.notRedstoneProximityRangeText);
					rps.setItemMeta(rpsmeta);
					blk.setType(Material.AIR);// because you canceled it, you
												// have to do it manually
					blk.getWorld().dropItemNaturally(blk.getLocation(), rps);// drops
																				// custom
																				// item
					event.setCancelled(true); // Cancel the event because you've
												// done it yourself.
					// event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerRightClick(PlayerInteractEvent event) throws IOException {
		if (event.getPlayer().hasPermission("redstonesensor.use")) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block blk = event.getClickedBlock();
				if ((blk.getType() == Material.REDSTONE_TORCH_ON)
						|| (blk.getType() == Material.REDSTONE_TORCH_OFF)) {
					for (Entry<Location, ArrayList<String>> entry : RedstoneSensor.redstoneList
							.entrySet()) {
						Location key = entry.getKey();
						Integer value = Integer
								.valueOf(entry.getValue().get(0));
						String playername = entry.getValue().get(1);
						if (((RedstoneSensor.onlyOwner) && (playername
								.equals(event.getPlayer().getName())))
								|| (!RedstoneSensor.onlyOwner)
								|| (playername.equals("null"))) {
							if (blk.getLocation().equals(key)) {
								Integer newvalue = RedstoneSensor.defaultRange;
								if (value == RedstoneSensor.maxRange) {
									newvalue = 1;
									ArrayList<String> list = new ArrayList<String>();
									list.add(String.valueOf(newvalue));
									list.add(entry.getValue().get(1));
									entry.setValue(list);
								} else {
									newvalue = value + 1;
									ArrayList<String> list = new ArrayList<String>();
									list.add(String.valueOf(newvalue));
									list.add(entry.getValue().get(1));
									entry.setValue(list);
									entry.setValue(list);
								}
								String setname = "Redstones."
										+ blk.getLocation().getWorld()
												.getName() + "-"
										+ blk.getLocation().getBlockX() + ""
										+ blk.getLocation().getBlockY() + ""
										+ blk.getLocation().getBlockZ();
								plugin.getConfig().set(setname + ".Range",
										newvalue);
								plugin.getConfig().save(
										new File(plugin.getDataFolder(),
												"config.yml"));

								event.getPlayer()
										.sendMessage(
												ChatColor.GOLD
														+ RedstoneSensor.redstoneProximityRangeNotifyText
														+ ": " + ChatColor.RED
														+ newvalue.toString());
							}
						}
					}
					for (Entry<Location, ArrayList<String>> entry : RedstoneSensor.notRedstoneList
							.entrySet()) {
						Location key = entry.getKey();
						Integer value = Integer
								.valueOf(entry.getValue().get(0));
						if (blk.getLocation().equals(key)) {
							Integer newvalue = RedstoneSensor.defaultRange;
							if (value == RedstoneSensor.maxRange) {
								newvalue = 1;
								ArrayList<String> list = new ArrayList<String>();
								list.add(String.valueOf(newvalue));
								list.add(entry.getValue().get(1));
								entry.setValue(list);
							} else {
								newvalue = value + 1;
								ArrayList<String> list = new ArrayList<String>();
								list.add(String.valueOf(newvalue));
								list.add(entry.getValue().get(1));
								entry.setValue(list);
								entry.setValue(list);
							}
							String setname = "Redstones."
									+ blk.getLocation().getWorld().getName()
									+ "-" + blk.getLocation().getBlockX() + ""
									+ blk.getLocation().getBlockY() + ""
									+ blk.getLocation().getBlockZ();
							plugin.getConfig()
									.set(setname + ".Range", newvalue);
							plugin.getConfig().save(
									new File(plugin.getDataFolder(),
											"config.yml"));

							event.getPlayer()
									.sendMessage(
											ChatColor.GOLD
													+ RedstoneSensor.redstoneProximityRangeNotifyText
													+ ": " + ChatColor.RED
													+ newvalue.toString());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void ItemDrop(ItemSpawnEvent event) {
		Location newloc = new Location(event.getLocation().getWorld(), event
				.getLocation().getBlockX(), event.getLocation().getBlockY(),
				event.getLocation().getBlockZ());

		if (RedstoneSensor.redstoneList.containsKey(newloc)) {

			RedstoneSensor.redstoneList.remove(newloc);
			ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta = rps.getItemMeta();
			rpsmeta.setDisplayName(ChatColor.RED
					+ RedstoneSensor.redstoneProximityRangeText);
			rps.setItemMeta(rpsmeta);
			event.getLocation().getBlock().getWorld()
					.dropItem(event.getLocation(), rps);

			String setname = "Redstones." + newloc.getWorld().getName() + "-"
					+ newloc.getBlockX() + "" + newloc.getBlockY() + ""
					+ newloc.getBlockZ();
			plugin.getConfig().set(setname, null);
			try {
				plugin.getConfig().save(
						new File(plugin.getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			event.setCancelled(true);
		}
		if (RedstoneSensor.notRedstoneList.containsKey(newloc)) {

			RedstoneSensor.notRedstoneList.remove(newloc);
			ItemStack rps = new ItemStack(Material.REDSTONE_TORCH_OFF, 1);
			ItemMeta rpsmeta = rps.getItemMeta();
			rpsmeta.setDisplayName(ChatColor.RED
					+ RedstoneSensor.notRedstoneProximityRangeText);
			rps.setItemMeta(rpsmeta);
			event.getLocation().getBlock().getWorld()
					.dropItem(event.getLocation(), rps);

			String setname = "Redstones." + newloc.getWorld().getName() + "-"
					+ newloc.getBlockX() + "" + newloc.getBlockY() + ""
					+ newloc.getBlockZ();
			plugin.getConfig().set(setname, null);
			try {
				plugin.getConfig().save(
						new File(plugin.getDataFolder(), "config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			event.setCancelled(true);
		}
	}

	@EventHandler
	public boolean PlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
			throws IOException {
		String cmd = event.getMessage();
		Player player = event.getPlayer();
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		List<String> arguments = new ArrayList<String>(Arrays.asList(cmd
				.split(" ")));
		if (arguments.size() > 0) {
			if ((arguments.get(0).matches("rps|redstonesensor|rsensor"))
					&& (player.hasPermission("redstonesensor.commands"))) {
				if (arguments.size() == 1) {

					player.sendMessage(ChatColor.RED + "[RPS] "
							+ ChatColor.GREEN
							+ "/rps [maxrange/defaultrange/onlyowner/reload]");
				}
				if (arguments.size() == 2) {
					if (("reload").equalsIgnoreCase(arguments.get(1))) {
						plugin.getServer().getPluginManager()
								.disablePlugin(plugin);
						plugin.getServer().getPluginManager()
								.enablePlugin(plugin);
						player.sendMessage(ChatColor.RED + "[RPS] "
								+ ChatColor.GREEN + "Reloaded!");

					} else if (("defaultrange").equalsIgnoreCase(arguments
							.get(1))) {
						player.sendMessage(ChatColor.RED + "[RPS] "
								+ ChatColor.GREEN
								+ "/rps defaultrange <number>");
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						player.sendMessage(ChatColor.RED + "[RPS] "
								+ ChatColor.GREEN + "/rps maxrange <number>");
					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						player.sendMessage(ChatColor.RED + "[RPS] "
								+ ChatColor.GREEN
								+ "/rps onlyowner <true/false>");
					}
				} else if (arguments.size() == 3) {
					if (("defaultrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.defaultRange = Integer.valueOf(arguments
								.get(2));
						player.sendMessage(ChatColor.RED
								+ "[RPS] "
								+ ChatColor.GREEN
								+ "Default range set to "
								+ String.valueOf(Integer.valueOf(arguments
										.get(2))));
						plugin.getConfig().set("Config.default-range",
								RedstoneSensor.defaultRange);
						plugin.getConfig().save(
								new File(plugin.getDataFolder(), "config.yml"));
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.maxRange = Integer.valueOf(arguments
								.get(2));
						player.sendMessage(ChatColor.RED
								+ "[RPS] "
								+ ChatColor.GREEN
								+ "Max range set to "
								+ String.valueOf(Integer.valueOf(arguments
										.get(2))));
						plugin.getConfig().set("Config.max-range",
								RedstoneSensor.maxRange);
						plugin.getConfig().save(
								new File(plugin.getDataFolder(), "config.yml"));
					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.onlyOwner = Boolean.valueOf(arguments
								.get(2));
						player.sendMessage(ChatColor.RED
								+ "[RPS] "
								+ ChatColor.GREEN
								+ "Owner Only Right Click set to "
								+ String.valueOf(Boolean.valueOf(arguments
										.get(2))));
						plugin.getConfig().set(
								"Config.owner-only-change-range",
								RedstoneSensor.onlyOwner);
						plugin.getConfig().save(
								new File(plugin.getDataFolder(), "config.yml"));

					}
				}
			}
		}
		return true;
	}

	@EventHandler
	public boolean ServerCommand(ServerCommandEvent event) throws IOException {
		String cmd = event.getCommand();
		if (cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		List<String> arguments = new ArrayList<String>(Arrays.asList(cmd
				.split(" ")));
		if (arguments.size() > 0) {
			if (arguments.get(0).matches("rps|redstonesensor|rsensor")) {
				if (arguments.size() == 1) {

					System.out.print("[RPS] "
							+ "/rps [maxrange/defaultrange/onlyowner/reload]");
				}
				if (arguments.size() == 2) {
					if (("reload").equalsIgnoreCase(arguments.get(1))) {
						plugin.getServer().getPluginManager()
								.disablePlugin(plugin);
						plugin.getServer().getPluginManager()
								.enablePlugin(plugin);
						System.out.print("[RPS] " + "Reloaded!");

					} else if (("defaultrange").equalsIgnoreCase(arguments
							.get(1))) {
						System.out.print("[RPS] "
								+ "/rps defaultrange <number>");
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						System.out.print("[RPS] " + "/rps maxrange <number>");
					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						System.out.print("[RPS] "
								+ "/rps onlyowner <true/false>");
					}
				} else if (arguments.size() == 3) {
					if (("defaultrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.defaultRange = Integer.valueOf(arguments
								.get(2));
						System.out.print("[RPS] "
								+ "Default range set to "
								+ String.valueOf(Integer.valueOf(arguments
										.get(2))));
						plugin.getConfig().set("Config.default-range",
								RedstoneSensor.defaultRange);
						plugin.getConfig().save(
								new File(plugin.getDataFolder(), "config.yml"));
					} else if (("maxrange").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.maxRange = Integer.valueOf(arguments
								.get(2));
						System.out.print("[RPS] "
								+ "Max range set to "
								+ String.valueOf(Integer.valueOf(arguments
										.get(2))));
						plugin.getConfig().set("Config.max-range",
								RedstoneSensor.maxRange);
						plugin.getConfig().save(
								new File(plugin.getDataFolder(), "config.yml"));

					} else if (("onlyowner").equalsIgnoreCase(arguments.get(1))) {
						RedstoneSensor.onlyOwner = Boolean.valueOf(arguments
								.get(2));
						System.out.print("[RPS] "
								+ "Only Owner Can Change Range set to "
								+ String.valueOf(Boolean.valueOf(arguments
										.get(2))));
						plugin.getConfig().set(
								"Config.owner-only-change-range",
								RedstoneSensor.onlyOwner);
						plugin.getConfig().save(
								new File(plugin.getDataFolder(), "config.yml"));

					}
				}
			}
		}
		return true;
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().isOp() && RedstoneSensor.outdated) {
			event.getPlayer()
					.sendMessage(
							ChatColor.DARK_PURPLE
									+ "The version of "
									+ ChatColor.DARK_RED
									+ "Redstone Proximity Sensor"
									+ ChatColor.DARK_PURPLE
									+ " that this server is running is out of date. Please consider updating to the latest version at "
									+ ChatColor.ITALIC
									+ ChatColor.GREEN
									+ "http://dev.bukkit.org/server-mods/redstonesensor/");
		}
	}
	
}