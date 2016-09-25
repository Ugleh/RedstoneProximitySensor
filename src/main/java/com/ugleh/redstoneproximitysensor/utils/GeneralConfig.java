package com.ugleh.redstoneproximitysensor.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class GeneralConfig extends YamlConfiguration{
	RedstoneProximitySensor plugin;
	File file;

	int maxRange = 20;
	int defaultRange = 5;
	boolean defaultownerOnlyTrigger = true;
	boolean defaultInverted = false;
	List<String> hostileMobs = new ArrayList<String>();
	List<String> peacefulMobs = new ArrayList<String>();
	
	public GeneralConfig(RedstoneProximitySensor plugin) {
		this.plugin = plugin;
		generateHostileMobsList();
		generatePeacefulMobs();
		reloadConfig();

		
	}

	private void generatePeacefulMobs() {
		peacefulMobs.add(EntityType.BAT.name());
		peacefulMobs.add(EntityType.CHICKEN.name());
		peacefulMobs.add(EntityType.COW.name());
		peacefulMobs.add(EntityType.MUSHROOM_COW.name());
		peacefulMobs.add(EntityType.PIG.name());
		peacefulMobs.add(EntityType.RABBIT.name());
		peacefulMobs.add(EntityType.SHEEP.name());
		peacefulMobs.add(EntityType.SQUID.name());
		peacefulMobs.add(EntityType.VILLAGER.name());
		peacefulMobs.add(EntityType.HORSE.name());
		peacefulMobs.add(EntityType.WOLF.name());
		peacefulMobs.add(EntityType.OCELOT.name());
		
	}

	private void generateHostileMobsList() {
		hostileMobs.add(EntityType.BLAZE.name());
		hostileMobs.add(EntityType.CREEPER.name());
		hostileMobs.add(EntityType.GUARDIAN.name());
		hostileMobs.add(EntityType.ENDERMITE.name());
		hostileMobs.add(EntityType.GHAST.name());
		hostileMobs.add(EntityType.MAGMA_CUBE.name());
		hostileMobs.add(EntityType.SILVERFISH.name());
		hostileMobs.add(EntityType.SKELETON.name());
		hostileMobs.add(EntityType.SLIME.name());
		hostileMobs.add(EntityType.WITCH.name());
		hostileMobs.add(EntityType.WITHER.name());
		hostileMobs.add(EntityType.ENDER_DRAGON.name());
		hostileMobs.add(EntityType.ZOMBIE.name());
		hostileMobs.add(EntityType.ENDERMAN.name());
		hostileMobs.add(EntityType.CAVE_SPIDER.name());
		hostileMobs.add(EntityType.SPIDER.name());
		hostileMobs.add(EntityType.PIG_ZOMBIE.name());
		hostileMobs.add(EntityType.SNOWMAN.name());
	}

	private void grabSettings() {
		maxRange = plugin.getConfig().getInt("rps.maxRange");
		defaultRange = plugin.getConfig().getInt("rps.defaultRange");
		defaultownerOnlyTrigger = plugin.getConfig().getBoolean("rps.defaultownerOnlyTrigger");
		defaultInverted = plugin.getConfig().getBoolean("rps.defaultInverted");
		
	}

	public void reloadConfig() {
		try {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdirs();
			}
			file = new File(plugin.getDataFolder(), "config.yml");
			if (!file.exists()) {
				plugin.getLogger().info("Config.yml not found, creating!");
				plugin.saveDefaultConfig();
			}
			grabSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int getMaxRange() {
		return maxRange;
	}

	public int getDefaultRange() {
		return defaultRange;
	}

	public boolean isDefaultownerOnlyTrigger() {
		return defaultownerOnlyTrigger;
	}

	public boolean isDefaultInverted() {
		return defaultInverted;
	}

}
