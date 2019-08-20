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
import java.util.*;
import java.util.Map.Entry;

import static com.ugleh.redstoneproximitysensor.RedstoneProximitySensor.*;

public class SensorConfig extends YamlConfiguration {

    private boolean sqlite;
    private File file;
    private String fileName;
    private JavaPlugin plugin;
    private HashMap<String, RPS> sensorList = new HashMap<>();

    public SensorConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.fileName = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.sqlite = instance.getgConfig().isSqliteEnabled();
        reload();
    }
    
    public void reload() {
        if (!sqlite) {

            if (!file.exists()) {

                try {
                    boolean mkdirs = file.getParentFile().mkdirs();
                    boolean fileCreated = file.createNewFile();
                    if((!fileCreated) || (!mkdirs)) {
                        plugin.getLogger().severe("Error while creating file " + file.getName());
                    }

                } catch (IOException exception) {
                    plugin.getLogger().severe(exception.getLocalizedMessage());
                    plugin.getLogger().severe("Error while creating file " + file.getName());
                }

            }

            try {
                load(file);

                if (fileName != null) {
                    InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(plugin.getResource(fileName), "Could not get RedstoneProximitySensor resources."));
                    FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                    setDefaults(defaultsConfig);
                    options().copyDefaults(true);

                    reader.close();
                    save();
                }

            } catch (IOException | InvalidConfigurationException exception) {
                plugin.getLogger().severe(exception.getLocalizedMessage());
                plugin.getLogger().severe("Error while loading file " + file.getName());

            }
        }

        grabSensors();
        createRunnable();

    }

    private void createRunnable() {
        new RPSRunnable(plugin);

    }

    private void grabSensors() {
        sensorList.clear();
        if (!sqlite) {
            // YML
            if (!this.isConfigurationSection("sensors")) return;
            for (String uniqueID : Objects.requireNonNull(this.getConfigurationSection("sensors"), "sensors could not be found.").getKeys(false)) {
                ConfigurationSection sensorSec = this.getConfigurationSection(String.format("sensors.%s", uniqueID));
                assert sensorSec != null;
                if (sensorSec.contains("location.world")) {
                    String worldName = sensorSec.getString("location.world");
                    Double x = Double.parseDouble(Objects.requireNonNull(sensorSec.getString("location.x"), "location.x missing in sensors.yml for ID " + uniqueID));
                    Double y = Double.parseDouble(Objects.requireNonNull(sensorSec.getString("location.y"), "location.y missing in sensors.yml for ID " + uniqueID));
                    Double z = Double.parseDouble(Objects.requireNonNull(sensorSec.getString("location.z"), "location.z missing in sensors.yml for ID " + uniqueID));
                    RPSLocation location = new RPSLocation(worldName, x, y, z);
                    this.addSensor(location, UUID.fromString(Objects.requireNonNull(sensorSec.getString("owner"), "owner missing in sensors.yml for ID " + uniqueID)), UUID.fromString(uniqueID));
                } else {
                    this.set(String.format("sensors.%s", uniqueID), null);
                    this.save();

                }

            }
        } else {
            // SQLite
            ArrayList<RPSData> sensorData = instance.getDatabase().addSensors();
            if (sensorData != null) {
                for (RPSData data : sensorData) {
                    this.addSensor(data.location, data.ownerID, data.rpsID);
                }
            }
        }
    }
    
    private void save() {

        try {
            options().indent(2);
            save(file);

        } catch (IOException exception) {
            plugin.getLogger().severe(exception.getLocalizedMessage());
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
            sensorList.put(location.getSLoc(), tempRPS);
            addToConfig(tempRPS);
            tellCustomTriggersRPSCreated(tempRPS);
            return tempRPS;
        } else {
            // SQLite

            boolean inConfig = instance.getDatabase().doesSensorExist(id.toString());
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
                rpsconfig.set("location.world", Objects.requireNonNull(tempRPS.getLocation().getWorld(), "Sensor "+ tempRPS.getUniqueID()+"'s World must not be null.").getName());
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
            if (instance.getDatabase().doesSensorExist(tempRPS.getUniqueID())) {
                instance.getDatabase().setDataOfSensor(tempRPS);
            } else {
                getInstance().getDatabase().setSensor(tempRPS.getUniqueID(),
                        tempRPS.getLocation(), tempRPS.isInverted(), tempRPS.getRange(), tempRPS.getAcceptedTriggerFlags(),
                        tempRPS.getOwner(), tempRPS.isOwnerOnlyEdit());
            }
        }

    }

    public Map<String, RPS> getSensorList() {
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
            getInstance().getDatabase().removeSensor(uniqueID);

        }

    }
    
    public boolean canPlaceLimiterCheck(Player player) {
    	int rpsCount = countPlayerSensors(player);
    	GeneralConfig gc = instance.getgConfig();
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
            instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".inverted", b);
            this.save();
        }
    }

    public void setRange(RPS selectedRPS, int newRange) {
        selectedRPS.setRange(newRange);
        if (sqlite) {
            instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".range", newRange);
            this.save();
        }

    }

    public void toggleAcceptedEntities(RPS selectedRPS, Trigger trigger, Player playerWhoClicked) {
        // Here we toggle the trigger
        String flag = trigger.getFlag();
        ArrayList<String> acceptedTriggerFlags = selectedRPS.getAcceptedTriggerFlags();

        boolean buttonToggledTo = (!acceptedTriggerFlags.contains(flag));
        boolean result = true;
        if (trigger.addonTemplate != null)
            result = trigger.addonTemplate.buttonPressed(buttonToggledTo, selectedRPS, playerWhoClicked);
        if(!result) return;
        if(buttonToggledTo)
            acceptedTriggerFlags.add(flag);
        else
            acceptedTriggerFlags.remove(flag);

        selectedRPS.setAcceptedEntities(acceptedTriggerFlags);

        if (sqlite) {
            instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".acceptedEntities", acceptedTriggerFlags);
            this.save();
        }

    }

    public void setOwnerOnlyEdit(RPS selectedRPS, boolean b) {
        selectedRPS.setOwnerOnlyEdit(b);

        if (sqlite) {
            instance.getDatabase().setSensor(selectedRPS);
        } else {
            this.set("sensors." + selectedRPS.getUniqueID() + ".ownerOnlyEdit", b);
            this.save();
        }

    }

    private void savePaste(String uniqueID, List<String> acceptedEntities, boolean inverted, boolean isownerOnlyEdit,
                           int range) {
        this.set("sensors." + uniqueID + ".acceptedEntities", acceptedEntities);
        this.set("sensors." + uniqueID + ".inverted", inverted);
        this.set("sensors." + uniqueID + ".ownerOnlyEdit", isownerOnlyEdit);
        this.set("sensors." + uniqueID + ".range", range);
        this.save();
    }

    public void savePaste(RPS rps) {
        if (sqlite) {
            instance.getDatabase().setSensor(rps);
        } else {
            this.savePaste(rps.getUniqueID(), rps.getAcceptedTriggerFlags(), rps.isInverted(), rps.isOwnerOnlyEdit(),
                    rps.getRange());
        }
    }

}