package com.ugleh.redstoneproximitysensor.sqlite;

import com.ugleh.redstoneproximitysensor.util.RPS;
import com.ugleh.redstoneproximitysensor.util.RPSData;
import com.ugleh.redstoneproximitysensor.util.RPSLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;


public abstract class Database {
    // The name of the table we created back in SQLite class.
    public String table = "rps_sensors";
    JavaPlugin plugin;
    Connection connection;

    public Database(JavaPlugin instance) {
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + "");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    public boolean doesSensorExist(String uuid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + uuid + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return false;
    }


    public Integer getRPSSensors() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + ";");
            rs = ps.executeQuery();
            while (rs.next()) {
                Bukkit.broadcastMessage(rs.getString("uuid"));

            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return 0;
    }


    //Now we need methods to save things to the database
    public void setSensor(String sensorID, Location location, boolean inverted, int range, List<String> acceptedEntities, UUID owner, boolean ownerOnlyEdit) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (uuid, world, x, y, z, inverted, range, acceptedEntities, owner, ownerOnlyEdit) VALUES(?,?,?,?,?,?,?,?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, sensorID);

            ps.setString(2, location.getWorld().getName());
            ps.setInt(3, location.getBlockX());
            ps.setInt(4, location.getBlockY());
            ps.setInt(5, location.getBlockZ());

            ps.setBoolean(6, inverted);
            ps.setInt(7, range);
            ps.setString(8, listToCsv(acceptedEntities, ','));
            ps.setString(9, owner.toString());
            ps.setBoolean(10, ownerOnlyEdit);
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }


    String listToCsv(List<String> listOfStrings, char separator) {
        StringBuilder sb = new StringBuilder();

        // all but last
        for (int i = 0; i < listOfStrings.size() - 1; i++) {
            sb.append(listOfStrings.get(i));
            sb.append(separator);
        }

        // last string, no separator
        if (listOfStrings.size() > 0) {
            sb.append(listOfStrings.get(listOfStrings.size() - 1));
        }

        return sb.toString();
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }

    //Now we need methods to save things to the database
    public void removeSensor(String sensorID) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("DELETE FROM " + table + " WHERE uuid = ?");
            ps.setString(1, sensorID);
            ps.executeUpdate();
            return;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return;
    }

    public void setDataOfSensor(RPS tempRPS) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + tempRPS.getUniqueID() + "';");

            rs = ps.executeQuery();
            while (rs.next()) {
                ArrayList<String> items = new ArrayList<String>(Arrays.asList(rs.getString("acceptedEntities").split(",")));
                tempRPS.setData(rs.getBoolean("inverted"), rs.getInt("range"), items, rs.getBoolean("ownerOnlyEdit"));
                return;
            }
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

/*public HashMap<UUID, HashMap<UUID, RPSLocation>> addSensors() {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        conn = getSQLConnection();
        ps = conn.prepareStatement("SELECT * FROM " + table);
        rs = ps.executeQuery();
        HashMap<UUID, HashMap<UUID, RPSLocation>> listOfSensors = new HashMap<UUID, HashMap<UUID, RPSLocation>>();

        while(rs.next()){
			String worldName = rs.getString("world");
			Double x = Double.parseDouble(rs.getString("x"));
			Double y = Double.parseDouble(rs.getString("y"));
            Double z = Double.parseDouble(rs.getString("z"));
            RPSLocation location = new RPSLocation(worldName, x, y, z);
            HashMap<UUID, RPSLocation> tempData = new HashMap<UUID, RPSLocation>();
            tempData.put(UUID.fromString(rs.getString("owner")), location);
            listOfSensors.put(UUID.fromString(rs.getString("uuid")), tempData);
     	   
        }
        return listOfSensors;
    } catch (SQLException ex) {
        plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
    } finally {
        try {
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
        }
    }
    return null;
}*/


    public ArrayList<RPSData> addSensors() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table);
            rs = ps.executeQuery();
            ArrayList<RPSData> listOfSensors = new ArrayList<RPSData>();

            while (rs.next()) {
                String worldName = rs.getString("world");
                Double x = Double.parseDouble(rs.getString("x"));
                Double y = Double.parseDouble(rs.getString("y"));
                Double z = Double.parseDouble(rs.getString("z"));
                RPSLocation location = new RPSLocation(worldName, x, y, z);
                listOfSensors.add(new RPSData(UUID.fromString(rs.getString("owner")), UUID.fromString(rs.getString("uuid")), location));

            }
            return listOfSensors;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public void setSensor(RPS rps) {
        this.setSensor(rps.getUniqueID().toString(), rps.getLocation(), rps.isInverted(), rps.getRange(), rps.getAcceptedTriggerFlags(), rps.getOwner(), rps.isOwnerOnlyEdit());
    }
}
