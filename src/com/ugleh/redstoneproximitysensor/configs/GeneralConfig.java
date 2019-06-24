package com.ugleh.redstoneproximitysensor.configs;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneralConfig extends YamlConfiguration{
	RedstoneProximitySensor plugin;
	File file;

	int maxRange = 20;
	int defaultRange = 5;
	public boolean useParticles = true;
	public boolean updateChecker = true;
	public boolean sqlite = false;
	private boolean defaultInverted = false;
	private boolean defaultOwnerTrigger = true;
	private boolean deaultPlayerEntityTrigger = true;
	private boolean defaultHostileEntityTrigger = false;
	private boolean defaultPeacefulEntityTrigger = false;
	private boolean defaultDroppedItemsTrigger = false;
	private boolean defaultInvisibleEntityTrigger = false;
	private boolean defaultVehcileEntityTrigger = false;
	private boolean defaultProjectileEntityTrigger = false;
	
	List<String> hostileMobs = new ArrayList<String>();
	List<String> peacefulMobs = new ArrayList<String>();
	
	public GeneralConfig(RedstoneProximitySensor plugin) {
		this.plugin = plugin;
		
		generateHostileMobsList();
		generatePeacefulMobs();
		reloadConfig();

		
	}

	private void generatePeacefulMobs() {
		
		peacefulMobs.add("WANDERING_TRADER");
		peacefulMobs.add("PANDA");
		peacefulMobs.add("BAT");
		peacefulMobs.add("CAT");
		peacefulMobs.add("FOX");
		peacefulMobs.add("CHICKEN");
		peacefulMobs.add("COW");
		peacefulMobs.add("MUSHROOM_COW");
		peacefulMobs.add("PIG");
		peacefulMobs.add("RABBIT");
		peacefulMobs.add("SHEEP");
		peacefulMobs.add("SQUID");
		peacefulMobs.add("VILLAGER");
		peacefulMobs.add("HORSE");
		peacefulMobs.add("WOLF");
		peacefulMobs.add("OCELOT");
		peacefulMobs.add("SQUID");
		peacefulMobs.add("POLAR_BEAR");
		peacefulMobs.add("LLAMA");
		peacefulMobs.add("DONKEY");
		peacefulMobs.add("MULE");
		peacefulMobs.add("ZOMBIE_HORSE");
		peacefulMobs.add("PARROT");
 		peacefulMobs.add("TURTLE");
		peacefulMobs.add("TROPICAL_FISH");
		peacefulMobs.add("ARMOR_STAND");
		peacefulMobs.add("COD");
		peacefulMobs.add("DOLPHIN");
		peacefulMobs.add("EGG");
		peacefulMobs.add("SALMON");
		peacefulMobs.add("PUFFERFISH");
		peacefulMobs.add("GIANT"); 
	}

	private void generateHostileMobsList() 
	{
		hostileMobs.add("PILLAGER");
		hostileMobs.add("RAVAGER");
		hostileMobs.add("BLAZE");
		hostileMobs.add("CREEPER");
		hostileMobs.add("GUARDIAN");
		hostileMobs.add("ENDERMITE");
		hostileMobs.add("GHAST");
		hostileMobs.add("MAGMA_CUBE");
		hostileMobs.add("SILVERFISH");
		hostileMobs.add("SKELETON");
		hostileMobs.add("SLIME");
		hostileMobs.add("WITCH");
		hostileMobs.add("WITHER");
		hostileMobs.add("ENDER_DRAGON");
		hostileMobs.add("ZOMBIE");
		hostileMobs.add("ENDERMAN");
		hostileMobs.add("CAVE_SPIDER");
		hostileMobs.add("SPIDER");
		hostileMobs.add("PIG_ZOMBIE");
		hostileMobs.add("SNOWMAN");
		hostileMobs.add("ZOMBIE_VILLAGER");
		hostileMobs.add("ELDER_GUARDIAN");
		hostileMobs.add("IRON_GOLEM");
		hostileMobs.add("SHULKER");
		hostileMobs.add("STRAY");
		hostileMobs.add("HUSK");
		hostileMobs.add("WITHER_SKELETON");
		hostileMobs.add("VINDICATOR");
		hostileMobs.add("EVOKER");
		hostileMobs.add("VEX");
		hostileMobs.add("ILLUSIONER");
 		hostileMobs.add("DROWNED");
		hostileMobs.add("PHANTOM");
	}

	private void grabSettings() {
		updateChecker = plugin.getConfig().getBoolean("rps.update-checker");
		sqlite = plugin.getConfig().getBoolean("rps.sqlite", false);
		useParticles = plugin.getConfig().getBoolean("rps.use-particles");
		maxRange = plugin.getConfig().getInt("rps.maxRange");
		defaultRange = plugin.getConfig().getInt("rps.defaultRange");
		defaultInverted = plugin.getConfig().getBoolean("rps.defaultInverted");
		
		defaultOwnerTrigger = plugin.getConfig().getBoolean("rps.defaultOwnerTrigger");;
		deaultPlayerEntityTrigger = plugin.getConfig().getBoolean("rps.defaultPlayerEntityTrigger");;
		defaultHostileEntityTrigger = plugin.getConfig().getBoolean("rps.defaultHostileEntityTrigger");;
		defaultPeacefulEntityTrigger = plugin.getConfig().getBoolean("rps.defaultPeacefulEntityTrigger");;
		defaultDroppedItemsTrigger = plugin.getConfig().getBoolean("rps.defaultDroppedItemsTrigger");;
		defaultInvisibleEntityTrigger = plugin.getConfig().getBoolean("rps.defaultInvisibleEntityTrigger");;
		defaultVehcileEntityTrigger = plugin.getConfig().getBoolean("rps.defaultVehcileEntityTrigger");;
		defaultProjectileEntityTrigger = plugin.getConfig().getBoolean("rps.defaultProjectileEntityTrigger");;
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
			createDefaults();
			grabSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void createDefaults() {
		plugin.getConfig().addDefault("rps.maxRange", 20);
		plugin.getConfig().addDefault("rps.use-particles", true);
		plugin.getConfig().addDefault("rps.sqlite", false);
		plugin.getConfig().addDefault("rps.defaultRange", 5);
		plugin.getConfig().addDefault("rps.defaultownerOnlyEdit", true);
		plugin.getConfig().addDefault("rps.defaultInverted", false);
		plugin.getConfig().addDefault("rps.defaultownerOnlyTrigger", false);
		plugin.getConfig().addDefault("rps.defaultPlayerEntityTrigger", true);
		plugin.getConfig().addDefault("rps.defaultHostileEntityTrigger", false);
		plugin.getConfig().addDefault("rps.defaultPeacefulEntityTrigger", false);
		plugin.getConfig().addDefault("rps.defaultDroppedItemsTrigger", false);
		plugin.getConfig().addDefault("rps.defaultInvisibleEntityTrigger", false);
		plugin.getConfig().addDefault("rps.defaultVehcileEntityTrigger", false);
		plugin.getConfig().addDefault("rps.defaultProjectileEntityTrigger", false);

		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();		
	}

	public int getMaxRange() {
		return maxRange;
	}

	public int getDefaultRange() {
		return defaultRange;
	}

	public boolean isDefaultOwnerTrigger() {
		return defaultOwnerTrigger;
	}

	public boolean isDefaultInverted() {
		return defaultInverted;
	}
	
	public RedstoneProximitySensor getPlugin() {
		return plugin;
	}

	public File getFile() {
		return file;
	}

	public boolean isDeaultPlayerEntityTrigger() {
		return deaultPlayerEntityTrigger;
	}
	public boolean useParticles() {
		return useParticles;
	}

	public boolean isDefaultHostileEntityTrigger() {
		return defaultHostileEntityTrigger;
	}

	public boolean isDefaultPeacefulEntityTrigger() {
		return defaultPeacefulEntityTrigger;
	}

	public boolean isDefaultDroppedItemsTrigger() {
		return defaultDroppedItemsTrigger;
	}

	public boolean isDefaultInvisibleEntityTrigger() {
		return defaultInvisibleEntityTrigger;
	}
	
	public boolean isSQLite() {
		return sqlite;
	}

	public List<String> getHostileMobs() {
		return hostileMobs;
	}

	public List<String> getPeacefulMobs() {
		return peacefulMobs;
	}

	public boolean isDefaultVehcileEntityTrigger() {
		return defaultVehcileEntityTrigger;
	}

	public boolean isDefaultProjectileEntityTrigger() {
		return defaultProjectileEntityTrigger;
	}
}
