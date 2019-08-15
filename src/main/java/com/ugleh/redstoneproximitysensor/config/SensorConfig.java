package com.ugleh.redstoneproximitysensor.config;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import com.ugleh.redstoneproximitysensor.listener.PlayerListener;
import com.ugleh.redstoneproximitysensor.util.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class SensorConfig extends YamlConfiguration {

    Boolean sqlite = false;
    private File file;
    private String defaults;
    private JavaPlugin plugin;
    private HashMap<String, RPS> sensorList = new HashMap<String, RPS>();
    
    public SensorConfig(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, fileName);
    }
    
    public SensorConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.sqlite = RedstoneProximitySensor.instance.getgConfig().isSqliteEnabled();
        reload();
    }
    
    public void reload() {
        if (!sqlite) {

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

            } catch (IOException exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("Error while loading file " + file.getName());

            } catch (InvalidConfigurationException exception) {
                exception.printStackTrace();
                plugin.getLogger().severe("Error while loading file " + file.getName());

            }
        }

        grabSensors();
        createRunnable();

    }

    private void createRunnable() {
        @SuppressWarnings("unused")
        RPSRunnable runnable = new RPSRunnable((RedstoneProximitySensor) plugin);

    }

    public void grabSensors() {
        sensorList.clear();
        if (!sqlite) {
            // YML
            if (!this.isConfigurationSection("sensors"))
                return;
            for (String uniqueID : this.getConfigurationSection("sensors").getKeys(false)) {
                ConfigurationSection sensorSec = this.getConfigurationSection("sensors." + uniqueID);
                if (sensorSec.contains("location.world")) {
                    String worldName = sensorSec.getString("location.world");
                    Double x = Double.parseDouble(sensorSec.getString("location.x"));
                    Double y = Double.parseDouble(sensorSec.getString("location.y"));
                    Double z = Double.parseDouble(sensorSec.getString("location.z"));
                    RPSLocation location = new RPSLocation(worldName, x, y, z);
                    this.addSensor(location, UUID.fromString(sensorSec.getString("owner")), UUID.fromString(uniqueID));
                } else {
                    this.set("sensors." + uniqueID, null);
                    this.save();

                }

            }
        } else {
            // SQLite
            ArrayList<RPSData> sensorData = RedstoneProximitySensor.instance.getDatabase().addSensors();
            if (sensorData != null) {
                for (RPSData data : sensorData) {
                    this.addSensor(data.location, data.ownerID, data.rpsID);

                }
            }
        }
    }
    
    public void save() {

        try {
            options().indent(2);
            save(file);

        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while saving file " + file.getName());
        }

    }

    public RPS addSensor(RPSLocation location, UUID placedBy, UUID id) {
        if (location == null)
            return null;

        if (!sqlite) {
            // YML
            boolean inConfig = false;
            if (this.isConfigurationSection("sensors." + id.toString()))
                inConfig = true;
            RPS tempRPS = new RPS((RedstoneProximitySensor) plugin, location, placedBy, id, inConfig);
            // tempRPS.setCancelTask(Bukkit.getScheduler().runTaskTimer(plugin,
            // tempRPS, 0L, 2L));
            sensorList.put(location.getSLoc(), tempRPS);
            addToConfig(tempRPS);
            tellCustomTriggersRPSCreated(tempRPS);
            return tempRPS;
        } else {
            // SQLite

            Boolean inConfig = RedstoneProximitySensor.instance.getDatabase().doesSensorExist(id.toString());
            RPS tempRPS = new RPS((RedstoneProximitySensor) plugin, location, placedBy, id, inConfig);
            sensorList.put(location.getSLoc(), tempRPS);
            addToConfig(tempRPS);
            tellCustomTriggersRPSCreated(tempRPS);
            return tempRPS;

        }
    }

    private void addToConfig(RPS tempRPS) {
        if (!sqlite) {
            if (!this.isConfigurationSection("sensors." + tempRPS.getUniqueID())) {
                // Sensor is not in the YML config, so add it.
                ConfigurationSection rpsconfig = this.createSection("sensors." + tempRPS.getUniqueID());
                rpsconfig.set("location.world", tempRPS.getLocation().getWorld().getName());
                rpsconfig.set("location.x", tempRPS.getLocation().getX());
                rpsconfig.set("location.y", tempRPS.getLocation().getY());
                rpsconfig.set("location.z", tempRPS.getLocation().getZ());
                rpsconfig.set("inverted", tempRPS.isInverted());
                rpsconfig.set("range", tempRPS.getRange());
                rpsconfig.set("acceptedEntities", tempRPS.getAcceptedTriggerFlags());
                rpsconfig.set("owner", tempRPS.getOwner().toString());
                rpsconfig.set("ownerOnlyEdit", tempRPS.isOwnerOnlyEdit());
                this.save();
            } else {
                // Sensor is in the YML config, so set the config data.
                //FIX PLAYER -> PLAYER_ENTITY and VEHCILE_ENTITY -> VEHICLE_ENTITY
                if(this.getStringList("sensors." + tempRPS.getUniqueID() + ".acceptedEntities").contains("PLAYER")) {
                    List<String> stringList = this.getStringList("sensors." + tempRPS.getUniqueID() + ".acceptedEntities");
                    stringList.remove("PLAYER");
                    stringList.add("PLAYER_ENTITY");
                    this.set("sensors." + tempRPS.getUniqueID() + ".acceptedEntities", stringList);
                    this.save();
                }

                if(this.getStringList("sensors." + tempRPS.getUniqueID() + ".acceptedEntities").contains("VEHCILE_ENTITY")) {
                    List<String> stringList = this.getStringList("sensors." + tempRPS.getUniqueID() + ".acceptedEntities");
                    stringList.remove("VEHCILE_ENTITY");
                    stringList.add("VEHICLE_ENTITY");
                    this.set("sensors." + tempRPS.getUniqueID() + ".acceptedEntities", stringList);
                    this.save();
                }

                tempRPS.setData(this.getBoolean("sensors." + tempRPS.getUniqueID() + ".inverted"),
                        this.getInt("sensors." + tempRPS.getUniqueID() + ".range"),
                        new ArrayList<>(
                                this.getStringList("sensors." + tempRPS.getUniqueID() + ".acceptedEntities")),
                        this.getBoolean("sensors." + tempRPS.getUniqueID() + ".ownerOnlyEdit"));
            }
        } else {
            // SQLite

            // Already in database, set data
            if (RedstoneProximitySensor.instance.getDatabase().doesSensorExist(tempRPS.getUniqueID())) {
                RedstoneProximitySensor.instance.getDatabase().setDataOfSensor(tempRPS);
            } else {
                RedstoneProximitySensor.getInstance().getDatabase().setSensor(tempRPS.getUniqueID(),
                        tempRPS.getLocation(), tempRPS.isInverted(), tempRPS.getRange(), tempRPS.getAcceptedTriggerFlags(),
                        tempRPS.getOwner(), tempRPS.isOwnerOnlyEdit());
            }
        }

    }

    public HashMap<String, RPS> getSensorList() {
        return sensorList;
    }

    public void removeSensor(String string) {
        RPS brokenRPS = this.getSensorList().get(string);
        tellCustomTriggersRPSBroke(brokenRPS);
        String uniqueID = brokenRPS.getUniqueID();

        if (!sqlite) {
            // YML
            this.getSensorList().remove(string);
            this.set("sensors." + uniqueID, null);
            this.save();
        } else {
            // SQLite
            RedstoneProximitySensor.getInstance().getDatabase().removeSensor(uniqueID);

        }

    }
    
    public boolean canPlaceLimiterCheck(Player player) {
    	int rpsCount = countPlayerSensors(player);
    	GeneralConfig gc = RedstoneProximitySensor.instance.getgConfig();
    	if(player.isOp())
    		return true;
    	
    	for(Entry<String, Integer> limiter : gc.permissionLimiters.entrySet()) {
    		if(player.hasPermission(limiter.getKey()) && (limiter.getValue() == -1 || rpsCount < limiter.getValue()))
    			return true;
    	}    	
    	return false;
    }
    private int countPlayerSensors(Player player) {
    	int count = 0;
        for (Entry<String, RPS> rpsEntry : sensorList.entrySet()) {
        	if(rpsEntry.getValue().getOwner().equals(player.getUniqueId()))
        		count++;
        }
        return count;
    }

    private void tellCustomTriggersRPSBroke(RPS brokenRPS) {
        for (Trigger t : PlayerListener.instance.getTriggers()) {
            if (t.addonTemplate != null) {
                t.addonTemplate.rpsRemoved(brokenRPS);
            }
        }
    }

    private void tellCustomTriggersRPSCreated(RPS createdRPS) {
        for (Trigger t : PlayerListener.instance.getTriggers()) {
            if (t.addonTemplate != null) {
                t.addonTemplate.rpsCreated(createdRPS);
            }
        }
    }

    public void setInverted(RPS selectedRPS, boolean b) {
        selectedRPS.setInverted(b);

        if (sqlite) {
            RedstoneProximitySensor.instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".inverted", b);
            this.save();
        }
    }

    public void setRange(RPS selectedRPS, int newRange) {
        selectedRPS.setRange(newRange);
        if (sqlite) {
            RedstoneProximitySensor.instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".range", newRange);
            this.save();
        }

    }

    public void toggleAcceptedEntities(RPS selectedRPS, Trigger trigger) {
        // Here we toggle the trigger
        String flag = trigger.getFlag();
        ArrayList<String> acceptedTriggerFlags = selectedRPS.getAcceptedTriggerFlags();

        boolean buttonToggledTo = (!acceptedTriggerFlags.contains(flag));
        boolean result = true;
        if (trigger.addonTemplate != null)
            result = trigger.addonTemplate.buttonPressed(buttonToggledTo, selectedRPS);
        if(!result) return;
        if(buttonToggledTo)
            acceptedTriggerFlags.add(flag);
        else
            acceptedTriggerFlags.remove(flag);

        selectedRPS.setAcceptedEntities(acceptedTriggerFlags);

        if (sqlite) {
            RedstoneProximitySensor.instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".acceptedEntities", acceptedTriggerFlags);
            this.save();
        }

    }

    public void setownerOnlyEdit(RPS selectedRPS, boolean b) {
        selectedRPS.setOwnerOnlyEdit(b);

        if (sqlite) {
            RedstoneProximitySensor.instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".ownerOnlyEdit", b);
            this.save();
        }

    }

    public void savePaste(String uniqueID, List<String> acceptedEntities, boolean inverted, boolean isownerOnlyEdit,
                          int range) {
        this.set("sensors." + uniqueID + ".acceptedEntities", acceptedEntities);
        this.set("sensors." + uniqueID + ".inverted", inverted);
        this.set("sensors." + uniqueID + ".ownerOnlyEdit", isownerOnlyEdit);
        this.set("sensors." + uniqueID + ".range", range);
        this.save();
    }

    public void savePaste(RPS rps) {
        if (sqlite) {
            RedstoneProximitySensor.instance.getDatabase().setSensor(rps);
        } else {
            this.savePaste(rps.getUniqueID(), rps.getAcceptedTriggerFlags(), rps.isInverted(), rps.isOwnerOnlyEdit(),
                    rps.getRange());
        }
    }

}