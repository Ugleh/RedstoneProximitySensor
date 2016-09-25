package com.ugleh.redstoneproximitysensor.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class FileReplace {
    List<String> lines = new ArrayList<String>();
    String line = null;

    public FileReplace(JavaPlugin plugin) {
        try {
            File f1 = new File(plugin.getDataFolder() + "/sensors.yml");
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                if (!line.contains("org.bukkit.Location"))
                {
                	lines.add(line);
                	lines.add(System.getProperty("line.separator"));
                }else
                {
                	System.out.print("WHAT?");
                }
            }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for(String s : lines)
                 out.write(s);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}