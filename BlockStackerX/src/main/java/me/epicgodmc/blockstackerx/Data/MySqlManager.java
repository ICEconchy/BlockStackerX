package me.epicgodmc.blockstackerx.Data;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MySqlManager {

    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;

    private PlacedStacks placedStacks = plugin.placedStacks;
    private String table = plugin.config.getString("mySql.table");

    public void checkForTable(String table)
    {
        try{
            sendQuery("CREATE TABLE IF NOT EXISTS `"+table+"` ( `ID` INT NOT NULL , `VALUE` LONGTEXT NOT NULL , INDEX `INDEX` (`ID`)) ENGINE = InnoDB;");
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }


    public void updateAndSave() {
        if (plugin.getConnection() != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + mm.getMessage("sqlUpdateAndSave", false));
            for (StackerPlaced sp : placedStacks.getPlacedStacksMap().values()) {
                int id = sp.getId();
                if (containsID(id)) {
                    update(id, sp.toString());
                } else {
                    try {
                        sendQuery("INSERT INTO " + table + " (ID, VALUE) VALUES ('" + sp.getId() + "','" + sp.toString() + "')");
                    } catch (SQLException e) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlError", false));
                        e.printStackTrace();
                    }

                }

            }
        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            return;
        }
    }

    public boolean syncYaml() {
        if (plugin.getConnection() != null) {
            for (String data : plugin.stackData.getStackDataConfig().getStringList("Data")) {

                int id = Integer.parseInt(data.split(";")[0]);
                if (containsID(id)) {
                    update(id, data);
                } else {
                    try {
                        sendQuery("INSERT INTO " + table + " (ID, VALUE) VALUES ('" + id + "','" + data + "')");
                        return true;
                    } catch (SQLException e) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlError", false));
                        e.printStackTrace();
                        return false;
                    }

                }

            }
        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            return false;
        }
        return true;
    }

    public void save() {
        if (plugin.getConnection() != null) {
            for (StackerPlaced sp : placedStacks.getPlacedStacksMap().values()) {
                try {
                    sendQuery("INSERT INTO " + table + " (ID, VALUE) VALUES ('" + sp.getId() + "','" + sp.toString() + "')");
                } catch (SQLException e) {
                    e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlError", false));
                }

            }
        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            return;
        }
    }

    public boolean containsID(int id) {
        if (plugin.getConnection() != null) {
            try {

                PreparedStatement statement = plugin.getConnection().prepareStatement("SELECT * FROM `" + table + "` WHERE ID=?");
                statement.setString(1, "" + id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    return true;
                }
                return false;


            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlError", false));
            }
        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
        }
        return true;
    }

    public void update(int id, String input) {
        if (plugin.getConnection() != null) {
            try {
                sendQuery("UPDATE " + table + " SET VALUE='" + input + "' WHERE ID='" + id + "'");
            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlError", false));
            }

        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
        }
    }


    public int load() {
        if (plugin.getConnection() != null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + mm.getMessage("sqlLoad", false));
            try {
                PreparedStatement sql = plugin.getConnection().prepareStatement("SELECT VALUE FROM `" + table + "`");
                ResultSet set = sql.executeQuery();
                ResultSetMetaData setMetaData = set.getMetaData();
                int amt = 0;

                while (set.next()) {
                    for (int i = 0; i < setMetaData.getColumnCount(); i++) {
                        String data = set.getString(i + 1);

                        new StackerPlaced(data);
                        amt++;
                    }
                }
                return amt;

            } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlError", false));
            }

        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
        }
        return 0;
    }


    public void sendQuery(String sql) throws SQLException {
        if (plugin.getConnection() != null) {
            plugin.getConnection().createStatement().execute(sql);

        }else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("sqlInvalidConnection", false));
        }
    }

}
