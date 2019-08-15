package com.ugleh.redstoneproximitysensor.util;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

public class UpdateChecker {
    final String versionUrl = "https://raw.githubusercontent.com/Ugleh/RedstoneProximitySensor/master/version";
    public String latestVersion;
    public String latestDesc;
    public boolean needsUpdate = false;

    public UpdateChecker(String version) {
        try {
            URL url = new URL(versionUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null)
                if (line.startsWith("#")) {
                    latestVersion = line.substring(1, line.length());
                } else if (line.startsWith("^")) {
                    latestDesc = line.substring(1, line.length());
                }
            br.close();
            is.close();

        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEGATIVE_MESSAGE) + "Could not connect to Update Checker.");
            e.printStackTrace();
        }
        if (!getInstance().getVersion().equals(this.latestVersion)) {
            needsUpdate = true;
            Bukkit.getConsoleSender().sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().getLang().get("lang_update_notice"));
            Bukkit.getConsoleSender().sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().getLang().get("lang_update_latest") + ": " + ChatColor.GREEN + this.latestVersion);
            Bukkit.getConsoleSender().sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) +  getInstance().getLang().get("lang_update_desc") + ": " + ChatColor.GREEN + this.latestDesc);
            Bukkit.getConsoleSender().sendMessage(prefixWithColor(RedstoneProximitySensor.ColorNode.NEUTRAL_MESSAGE) + getInstance().getLang().get("lang_update_link") + ": " + ChatColor.GREEN + "https://www.spigotmc.org/resources/redstone-proximity-sensor.17965/");

        }
    }
    private String prefixWithColor(RedstoneProximitySensor.ColorNode colorNode) {
        return (getInstance().chatPrefix + getInstance().getColor(colorNode));
    }
    private static RedstoneProximitySensor getInstance() {
        return RedstoneProximitySensor.getInstance();
    }
}
