package com.ugleh.redstoneproximitysensor.config;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GeneralConfig extends YamlConfiguration {
    public boolean useParticles = true;
    public boolean updateChecker = true;
    public boolean sqlite = false;
    RedstoneProximitySensor plugin;
    File file;
    int maxRange = 20;
    int defaultRange = 5;
    private boolean defaultInverted = false;
    private boolean defaultOwnerTrigger = true;
    private boolean deaultPlayerEntityTrigger = true;
    private boolean defaultHostileEntityTrigger = false;
    private boolean defaultPeacefulEntityTrigger = false;
    private boolean defaultNeutralEntityTrigger = false;
    private boolean defaultDroppedItemsTrigger = false;
    private boolean defaultInvisibleEntityTrigger = false;
    private boolean defaultVehcileEntityTrigger = false;
    private boolean defaultProjectileEntityTrigger = false;
    private List<String> supportedEntities = new ArrayList<String>();
    
    public HashMap<String, Integer> permissionLimiters = new HashMap<String, Integer>();
    public GeneralConfig(RedstoneProximitySensor plugin) {
        this.plugin = plugin;
        reloadConfig();


    }
    private void grabSettings() {
        updateChecker = plugin.getConfig().getBoolean("rps.update-checker");
        sqlite = plugin.getConfig().getBoolean("rps.sqlite", false);
        useParticles = plugin.getConfig().getBoolean("rps.use-particles");
        maxRange = plugin.getConfig().getInt("rps.maxRange");
        defaultRange = plugin.getConfig().getInt("rps.defaultRange");
        defaultInverted = plugin.getConfig().getBoolean("rps.defaultInverted");

        defaultOwnerTrigger = plugin.getConfig().getBoolean("rps.defaultOwnerTrigger");
        deaultPlayerEntityTrigger = plugin.getConfig().getBoolean("rps.defaultPlayerEntityTrigger");
        defaultHostileEntityTrigger = plugin.getConfig().getBoolean("rps.defaultHostileEntityTrigger");
        defaultPeacefulEntityTrigger = plugin.getConfig().getBoolean("rps.defaultPeacefulEntityTrigger");
        defaultNeutralEntityTrigger = plugin.getConfig().getBoolean("rps.defaultNeutralEntityTrigger");
        defaultDroppedItemsTrigger = plugin.getConfig().getBoolean("rps.defaultDroppedItemsTrigger");
        defaultInvisibleEntityTrigger = plugin.getConfig().getBoolean("rps.defaultInvisibleEntityTrigger");
        defaultVehcileEntityTrigger = plugin.getConfig().getBoolean("rps.defaultVehcileEntityTrigger");
        defaultProjectileEntityTrigger = plugin.getConfig().getBoolean("rps.defaultProjectileEntityTrigger");
        
        for(Mobs mob : Mobs.values()) {
        	supportedEntities.add(mob.getEntityTypeName());
        }
        grabLimitPermissions();

    }
    
    private void grabLimitPermissions()
    {
    	//If it is missing, add a default permission of infinite count to the list.
    	if(!plugin.getConfig().isSet("rps.limiter"))
    	{
        	permissionLimiters.put("rps.limiter.default", -1);
        	plugin.getServer().getPluginManager().addPermission(new Permission("rps.limiter.default", PermissionDefault.TRUE));
        	plugin.getConfig().set("rps.limiter.default.default", true);
        	plugin.getConfig().set("rps.limiter.default.amount", -1);
        	plugin.saveConfig();
    		return;
    	}
        for (String key : plugin.getConfig().getConfigurationSection("rps.limiter").getKeys(true)) {
    		//Check if subkey "default" exists, if it does grab the PermissionDefault of it, if not set to DEFAULT_PERMISSION.
        	PermissionDefault pd = Permission.DEFAULT_PERMISSION;
            	if(plugin.getConfig().isSet("rps.limiter."  + key + ".default"))
            	{
            		pd = PermissionDefault.getByName(plugin.getConfig().getString("rps.limiter."  + key + ".default"));    
            		if (pd == null)
            			throw new IllegalArgumentException("'default' key in RedstoneProximitySensor Config contained unknown value");
            	}
            	
            	plugin.getServer().getPluginManager().addPermission(new Permission("rps.limiter."  + key, pd));
            	
            	//Check if subkey "amount" exists, if it does grab the amount, if not set it to -1 (infinite)
            	int limiterAmount = -1;
            	if(plugin.getConfig().isSet("rps.limiter."  + key + ".amount")) {
            		limiterAmount = plugin.getConfig().getInt("rps.limiter."  + key + ".amount");
            	}
        	permissionLimiters.put("rps.limiter."  + key, limiterAmount);
        }
        
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(permissionLimiters.entrySet()); 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        });
        
        permissionLimiters = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
        	permissionLimiters.put(aa.getKey(), aa.getValue()); 
        }
        
        if(permissionLimiters.isEmpty())
        {
        	permissionLimiters.put("rps.limiter.default", -1);
        	plugin.getServer().getPluginManager().addPermission(new Permission("rps.limiter.default", Permission.DEFAULT_PERMISSION));
        }
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
        plugin.getConfig().addDefault("rps.update-checker", true);
        plugin.getConfig().addDefault("rps.sqlite", false);
        plugin.getConfig().addDefault("rps.defaultRange", 5);
        plugin.getConfig().addDefault("rps.defaultownerOnlyEdit", true);
        plugin.getConfig().addDefault("rps.defaultInverted", false);
        plugin.getConfig().addDefault("rps.defaultownerOnlyTrigger", false);
        plugin.getConfig().addDefault("rps.defaultPlayerEntityTrigger", true);
        plugin.getConfig().addDefault("rps.defaultHostileEntityTrigger", false);
        plugin.getConfig().addDefault("rps.defaultPeacefulEntityTrigger", false);
        plugin.getConfig().addDefault("defaultNeutralEntityTrigger", false);
        plugin.getConfig().addDefault("rps.defaultDroppedItemsTrigger", false);
        plugin.getConfig().addDefault("rps.defaultInvisibleEntityTrigger", false);
        plugin.getConfig().addDefault("rps.defaultVehcileEntityTrigger", false);
        plugin.getConfig().addDefault("rps.defaultProjectileEntityTrigger", false);

        plugin.getConfig().options().copyDefaults(false);
        plugin.saveConfig();
    }

    public boolean isSupportedEntity(EntityType entityType) {
    	return supportedEntities.contains(entityType.name());
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
    
    public boolean isDefaultNeutralEntityTrigger() {
    	return defaultNeutralEntityTrigger;
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

    public boolean isDefaultVehcileEntityTrigger() {
        return defaultVehcileEntityTrigger;
    }

    public boolean isDefaultProjectileEntityTrigger() {
        return defaultProjectileEntityTrigger;
    }
}
