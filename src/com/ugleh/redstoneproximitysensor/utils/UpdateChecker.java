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
	public UpdateChecker(String version)
	{
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
        if(!getInstance().getVersion().equals(this.latestVersion))
        {
        	needsUpdate = true;
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] " + getInstance().getLang().get("lang_update_notice"));
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] " + getInstance().getLang().get("lang_update_latest") + ": " + ChatColor.GREEN + this.latestVersion);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] " + getInstance().getLang().get("lang_update_desc") + ": " + ChatColor.GREEN + this.latestDesc);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[RPS] " + getInstance().getLang().get("lang_update_link") + ": " + ChatColor.GREEN + "https://www.spigotmc.org/resources/redstone-proximity-sensor.17965/");
        	
        	
        }
	}
	
	public static RedstoneProximitySensor getInstance()
	{
		return RedstoneProximitySensor.getInstance();
	}
}
