package com.ugleh.redstoneproximitysensor.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LanguageConfig extends YamlConfiguration {

    private File file;
    private String defaults;
    private JavaPlugin plugin;
    private HashMap<String, String> languageNodes = new HashMap<String, String>();
    private HashMap<String, String> colorNodes = new HashMap<String, String>();

    public LanguageConfig(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, fileName);
    }

    public LanguageConfig(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

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
            loadLanguageNodes();
            loadColorNodes();

        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());

        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Error while loading file " + file.getName());

        }

    }

    private void loadLanguageNodes() {
        if (!this.isConfigurationSection("language")) return;
        for (String languageID : this.getConfigurationSection("language").getKeys(false)) {
            String langString = this.getString("language." + languageID);
            languageNodes.put(languageID, langString);
        }
    }

    private void loadColorNodes() {
        if (!this.isConfigurationSection("color")) return;
        for (String colorID : this.getConfigurationSection("color").getKeys(false)) {
            String colorString = this.getString("color." + colorID);
            colorNodes.put(colorID, colorString);
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

    public HashMap<String, String> getLanguageNodes() {
        return languageNodes;
    }

    public HashMap<String, String> getColorNodes() {
        return colorNodes;
    }
}