package com.ugleh.redstoneproximitysensor.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.ugleh.redstoneproximitysensor.RedstoneProximitySensor;

public class UpdateChecker {
	final String versionUrl = "https://raw.githubusercontent.com/Ugleh/RedstoneProximitySensor/master/version";
	public String latestVersion;
	public String latestDesc;
	public boolean needsUpdate = false;
	private static RedstoneProximitySensor redstoneProximitySensor = RedstoneProximitySensor.getInstance();
	public UpdateChecker()
	{
		this.redstoneProximitySensor = redstoneProximitySensor;
        try {
            URL url = new URL(versionUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ( (line = br.readLine()) != null)
            	if(line.startsWith("#"))
            	{
            		latestVersion = line.substring(1, line.length());
            	}else if(line.startsWith("^"))
            	{
            		latestDesc = line.substring(1, line.length());
            	}
            br.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!redstoneProximitySensor.getVersion().equals(this.latestVersion))
        {
        	needsUpdate = true;
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] Redstone Proximity Sensor is outdated.");
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] Latest Version: " + ChatColor.GREEN + this.latestVersion);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] Description: " + ChatColor.GREEN + this.latestDesc);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] Link: " + ChatColor.GREEN + "https://www.spigotmc.org/resources/redstone-proximity-sensor.17965/");
        }
	}
}
