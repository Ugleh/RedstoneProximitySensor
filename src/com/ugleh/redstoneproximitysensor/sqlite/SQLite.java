package com.ugleh.redstoneproximitysensor.sqlite;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {
    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS rps_sensors (" + // make sure to put your table name in here too.
            "`uuid` varchar(36) NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
            "`world` varchar(32) NOT NULL," +
            "`x` int(11) NOT NULL," +
            "`y` int(11) NOT NULL," +
            "`z` int(11) NOT NULL," +
            "`inverted` BOOL NOT NULL," +
            "`range` int(11) NOT NULL," +
            "`acceptedEntities` TEXT," +
            "`owner` varchar(36) NOT NULL," +
            "`ownerOnlyEdit` BOOL NOT NULL," +
            "PRIMARY KEY (`uuid`)" +
            ");";
    String dbname;

    public SQLite(JavaPlugin instance) {
        super(instance);
        dbname = plugin.getConfig().getString("sensors", "rps_sensors"); // Set the table name here e.g player_kills
    }

    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");
        dataFolder.getParentFile().mkdirs();
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}
 