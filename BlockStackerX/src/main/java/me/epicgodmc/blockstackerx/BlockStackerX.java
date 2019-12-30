package me.epicgodmc.blockstackerx;

import me.epicgodmc.blockstackerx.Commands.CommandManager;
import me.epicgodmc.blockstackerx.Commands.ConsoleCommandManager;
import me.epicgodmc.blockstackerx.Data.MySqlManager;
import me.epicgodmc.blockstackerx.Data.PlacedStacks;
import me.epicgodmc.blockstackerx.Data.YamlData;
import me.epicgodmc.blockstackerx.Events.*;
import me.epicgodmc.blockstackerx.Files.Lang;
import me.epicgodmc.blockstackerx.Files.StackData;
import me.epicgodmc.blockstackerx.Files.Worth;
import me.epicgodmc.blockstackerx.Utilities.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BlockStackerX extends JavaPlugin {
    public FileConfiguration config = this.getConfig();
    private PluginManager pluginManager = this.getServer().getPluginManager();

    public static boolean aSkyBlock;
    public static boolean skyBlockX;

    public final boolean useSql = config.getBoolean("mySql.enabled");
    private Connection connection;
    private String host, database, username, password, table;
    private int port;


    //instances
    public Lang lang;
    public Util util;
    public CommandManager commandManager;
    public ConsoleCommandManager consoleCommandManager;
    public BlockFactory blockFactory;
    public PlacedStacks placedStacks;
    public StackData stackData;
    public MySqlManager mySqlManager;
    public YamlData yamlData;
    public MessageManager messageManager;
    public Worth worth;
    ///


    @Override
    public void onEnable() {
        if (!checkDependencies()) return;

        Metrics metrics = new Metrics(this);
        
        registerInstances();
        setupFiles();
        registerEvents();
        commandManager.setup();
        consoleCommandManager.setup();
        try {
            if (!useSql) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + messageManager.getMessage("yamlUse", false));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"[BlockStackerX] Loaded "+yamlData.load()+" Stackers");
            } else {
                connectSql();
                if (connection != null && !connection.isClosed())
                {
                    mySqlManager.checkForTable(table);

                    new DataBasePing(this);
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"[BlockStackerX] Loaded "+mySqlManager.load()+" Stackers");
                }else{
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED+messageManager.getMessage("yamlLoadSqlError", false));
                    Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +"[BlockStackerX] Loaded "+yamlData.load()+" Stackers");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        util.checkConfiguration();

        new AutoSave(this);

    }

    @Override
    public void onDisable() {
            try {
                if (!useSql) {
                    yamlData.save();
                } else {
                    if (connection != null && !connection.isClosed()) {
                        mySqlManager.updateAndSave();
                    } else {
                        yamlData.save();
                    }
                    disconnectSql();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DisplayManager.deleteAllDisplays();
        }



    public boolean checkDependencies()
    {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
        {
            getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            getLogger().severe("*** This plugin will be disabled. ***");
            this.setEnabled(false);
            return false;
        }

        if (this.getServer().getPluginManager().getPlugin("ASkyBlock") == null)
        {
            aSkyBlock = false;

        }else
        {
            aSkyBlock = true;
            System.out.print("[BlockStackerX] Found ASkyBlock");
            pluginManager.registerEvents(new ASkyBlockCalculation(), this);
            return true;
        }
        if (!this.getServer().getPluginManager().isPluginEnabled("SkyblockX"))
        {
            skyBlockX = false;
        }else{
            skyBlockX = true;
            System.out.print("[BlockStackerX] Found SkyBlockX");
            return true;
        }


        if (!aSkyBlock && !skyBlockX)
        {
            this.getLogger().severe("Could not find skyblock dependency");
            this.getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        return false;
    }



    public void registerEvents() {
        pluginManager.registerEvents(new StackerPlace(), this);
        pluginManager.registerEvents(new StackerBreak(), this);
        pluginManager.registerEvents(new StackerAddBlocks(), this);
        pluginManager.registerEvents(new StackerRemoveBlocks(), this);

    }


    public void setupFiles() {
        saveDefaultConfig();
        lang.createLangConfig();
        stackData.createStackDataConfig();
        worth.createWorthConfig();
    }

    public void registerInstances() {
        messageManager = new MessageManager();
        commandManager = new CommandManager();
        consoleCommandManager = new ConsoleCommandManager();
        util = new Util();
        lang = new Lang();
        blockFactory = new BlockFactory();
        placedStacks = new PlacedStacks();
        stackData = new StackData();
        mySqlManager = new MySqlManager();
        yamlData = new YamlData();
        worth = new Worth();
    }

    private void disconnectSql() {
        try {
            if (getConnection() != null || !getConnection().isClosed()) {
                getConnection().close();
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[BlockStackerX] Successfully closed mySql connection");
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[BlockStackerX] Failed to close mySql connection!");
            e.printStackTrace();
        }
    }

    private void connectSql() {
        host = config.getString("mySql.host");
        database = config.getString("mySql.database");
        username = config.getString("mySql.username");
        password = config.getString("mySql.password");
        port = config.getInt("mySql.port");
        table = config.getString("mySql.table");

        try {

            if (getConnection() != null && !getConnection().isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[BlockStackerX] Successfully connected to mySql");

        } catch (SQLException | ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not connect to mySql!, using Yaml Storage");
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
