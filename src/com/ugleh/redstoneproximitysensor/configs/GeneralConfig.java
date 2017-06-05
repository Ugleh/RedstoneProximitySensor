package com.ugleh.redstoneproximitysensor.configs;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

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
	
	float bukkitVersion;
	public GeneralConfig(RedstoneProximitySensor plugin) {
		this.plugin = plugin;
		
		String[] ver = Bukkit.getBukkitVersion().split("-");
		String[] checkperiods = ver[0].split("\\.");
		
		if(checkperiods.length > 2)
		{
		int i = ver[0].lastIndexOf(".");
		String[] vers =  {ver[0].substring(0, i), ver[0].substring(i)};
		bukkitVersion = Float.parseFloat(vers[0]);
		}else{
		bukkitVersion = Float.parseFloat(ver[0]);

		}
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
		
		peacefulMobs.add(EntityType.SQUID.name());
		if(bukkitVersion >= 1.10)
		{
			peacefulMobs.add(EntityType.POLAR_BEAR.name());
			
			if(bukkitVersion >= 1.11)
			{
				peacefulMobs.add(EntityType.LLAMA.name());
				peacefulMobs.add(EntityType.DONKEY.name());
				peacefulMobs.add(EntityType.MULE.name());
				peacefulMobs.add(EntityType.ZOMBIE_HORSE.name());
				
				if(bukkitVersion >= 1.12) //1.12 not yet released, so working with -pre's
				{
					hostileMobs.add(EntityType.PARROT.name());
				}
			}
		}
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
		hostileMobs.add(EntityType.ZOMBIE_VILLAGER.name());
		hostileMobs.add(EntityType.ELDER_GUARDIAN.name());
		hostileMobs.add(EntityType.IRON_GOLEM.name());
		if(bukkitVersion >= 1.9)
		{
			hostileMobs.add(EntityType.SHULKER.name());
			
			if(bukkitVersion >= 1.10)
			{
				hostileMobs.add(EntityType.STRAY.name());
				hostileMobs.add(EntityType.HUSK.name());
				
				if(bukkitVersion >= 1.11)
				{
					hostileMobs.add(EntityType.WITHER_SKELETON.name());
					hostileMobs.add(EntityType.VINDICATOR.name());
					hostileMobs.add(EntityType.EVOKER.name());
					hostileMobs.add(EntityType.VEX.name());
					
					if(bukkitVersion >= 1.12) //1.12 not yet released, so working with -pre's
					{
						hostileMobs.add(EntityType.ILLUSIONER.name());
					}
				}
			}
		}
		}

	private void grabSettings() {
		updateChecker = plugin.getConfig().getBoolean("rps.update-checker");
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
