package com.ugleh.redstoneproximitysensor.sqlite;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class Error {
   public static void execute(JavaPlugin plugin, Exception ex){
       plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
   }
   public static void close(JavaPlugin plugin, Exception ex){
       plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
   }
}