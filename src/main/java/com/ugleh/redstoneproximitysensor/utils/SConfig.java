package com.ugleh.redstoneproximitysensor.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.World;

public class SConfig extends YamlConfiguration {

    private File file;
    private String defaults;
    private JavaPlugin plugin;
    private HashMap<Location, RPS> sensorList = new HashMap<Location, RPS>();

    /**
     * Creates new PluginFile, without defaults
     * @param plugin - Your plugin
     * @param fileName - Name of the file
     */
    public SConfig(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    /**
     * Creates new PluginFile, with defaults
     * @param plugin - Your plugin
     * @param fileName - Name of the file
     * @param defaultsName - Name of the defaults
     */
    public SConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    /**
     * Reload configuration
     */
    public void reload() {

        if (!file.exists()) {

            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

            } catch (IOException exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("Error while creating file " + file.getName());
            }

        }

        try {
            load(file);

            if (defaults != null) {
                InputStreamReader reader = new InputStreamReader(plugin.getResource(defaults));
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                setDefaults(defaultsConfig);
                options().copyDefaults(true);

                reader.close();
                save();
            }
            grabSensors();
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());

        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());

        }

    }

    private void grabSensors() {
		if(!this.isConfigurationSection("sensors")) return;
		for(String uniqueID : this.getConfigurationSection("sensors").getKeys(false))
		{
			ConfigurationSection sensorSec = this.getConfigurationSection("sensors." + uniqueID);

            World w = Bukkit.getWorld(sensorSec.getString("location.world"));
            Double x = Double.parseDouble(sensorSec.getString("location.x"));
            Double y = Double.parseDouble(sensorSec.getString("location.y"));
            Double z = Double.parseDouble(sensorSec.getString("location.z"));
            Location location = new Location(w, x, y, z);
			this.addSensor(location, UUID.fromString(sensorSec.getString("owner")), UUID.fromString(uniqueID));
//			if(((Location)sensorSec.get("location")).getBlock().getType().equals(Material.REDSTONE_TORCH_OFF) || ((Location)sensorSec.get("location")).getBlock().getType().equals(Material.REDSTONE_TORCH_ON))
//			{
//			}else
//			{
//				this.removeSensor((Location)sensorSec.get("location"));
//			}
		}
	}

	/**
     * Save configuration
     */
    public void save() {

        try {
            options().indent(2);
            save(file);

        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while saving file " + file.getName());
        }

    }
	public void  addSensor(Location location, UUID placedBy, UUID id) {
		boolean inConfig = false;
		if(this.isConfigurationSection("sensors." + id.toString()))
			inConfig = true;
		if(location != null)
		{
			RPS tempRPS = new RPS(location, placedBy, id, inConfig);
			tempRPS.setCancelTask(Bukkit.getScheduler().runTaskTimer(plugin, tempRPS, 0L, 2L));
			sensorList.put(location, tempRPS);
			addToConfig(tempRPS);
			//return tempRPS;
		}
	}


	private void addToConfig(RPS tempRPS) {
		if(!this.isConfigurationSection("sensors." + tempRPS.getUniqueID()))
		{
			//Not in the config.
			ConfigurationSection rpsconfig = this.createSection("sensors." + tempRPS.getUniqueID());
			rpsconfig.set("location", tempRPS.getLocation());
			rpsconfig.set("inverted", tempRPS.isInverted());
			rpsconfig.set("range", tempRPS.getRange());
			rpsconfig.set("acceptedEntities", tempRPS.getAcceptedEntities());
			rpsconfig.set("owner", tempRPS.getOwner().toString());
			rpsconfig.set("ownerOnlyTrigger", tempRPS.isownerOnlyTrigger());
			rpsconfig.set("ownerOnlyEdit", tempRPS.isownerOnlyEdit());
			this.save();
		}else{
			tempRPS.setData(this.getBoolean("sensors." + tempRPS.getUniqueID() + ".ownerOnlyTrigger"), this.getBoolean("sensors." + tempRPS.getUniqueID() + ".inverted"),this.getInt("sensors." + tempRPS.getUniqueID() + ".range"), this.getStringList("sensors." + tempRPS.getUniqueID() + ".acceptedEntities"), this.getBoolean("sensors." + tempRPS.getUniqueID() + ".ownerOnlyEdit"));
		}

	}

	public HashMap<Location, RPS> getSensorList() {
		return sensorList;
	}

	public void removeSensor(Location location) {
		RPS brokenRPS = this.getSensorList().get(location);
		brokenRPS.cancelTask();
		String uniqueID = brokenRPS.getUniqueID();
		this.getSensorList().remove(location);
		this.set("sensors." + uniqueID, null);
		this.save();

	}

	public void setInverted(RPS selectedRPS, boolean b) {
		this.set("sensors." + selectedRPS.getUniqueID() + ".inverted", b);
		this.save();
		selectedRPS.setInverted(b);

	}

	public void setownerOnlyTrigger(RPS selectedRPS, boolean b) {
		this.set("sensors." + selectedRPS.getUniqueID() + ".ownerOnlyTrigger", b);
		this.save();
		selectedRPS.setownerOnlyTrigger(b);

	}

	public void setRange(RPS selectedRPS, int newRange) {
		this.set("sensors." + selectedRPS.getUniqueID() + ".range", newRange);
		this.save();
		selectedRPS.setRange(newRange);

	}

	public void addAcceptedEntity(RPS selectedRPS, String s) {
		List<String> acceptedEntitiesConfig = this.getStringList("sensors." + selectedRPS.getUniqueID() + ".acceptedEntities");
		if(acceptedEntitiesConfig.contains(s))
		{
			acceptedEntitiesConfig.remove(s);
		}else
		{
			acceptedEntitiesConfig.add(s);
		}
		this.set("sensors." + selectedRPS.getUniqueID() + ".acceptedEntities", acceptedEntitiesConfig);
		this.save();
		selectedRPS.setAcceptedEntities(acceptedEntitiesConfig);


	}

	public void setownerOnlyEdit(RPS selectedRPS, boolean b) {
		this.set("sensors." + selectedRPS.getUniqueID() + ".ownerOnlyEdit", b);
		this.save();
		selectedRPS.setOwnerOnlyEdit(b);

	}

}
