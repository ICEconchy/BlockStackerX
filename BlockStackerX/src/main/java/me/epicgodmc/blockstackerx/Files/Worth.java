package me.epicgodmc.blockstackerx.Files;

import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Worth
{


    private BlockStackerX plugin = BlockStackerX.getInstance();


    private File worthConfigFile;
    private FileConfiguration worthConfig;


    public FileConfiguration getWorthConfig() {
        return this.worthConfig;
    }

    public double getWorth(Material mat)
    {
        ConfigurationSection confSection = worthConfig.getConfigurationSection("Worth");
        String matStr = mat.toString();
        if (!confSection.contains(matStr))
        {
            return 1.0;
        }else{
            return worthConfig.getDouble("Worth."+matStr);
        }
    }

    public void setWorth(Material mat, double value)
    {
        worthConfig.set("Worth."+mat.toString(), value);
        saveWorthConfig();

    }

    public void saveWorthConfig()
    {
        try{
            worthConfig.save(worthConfigFile);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void createWorthConfig() {
        worthConfigFile = new File(plugin.getDataFolder(), "Worth.yml");
        if (!worthConfigFile.exists()) {
            worthConfigFile.getParentFile().mkdirs();
            plugin.saveResource("Worth.yml", false);
        }

        worthConfig= new YamlConfiguration();
        try {
            worthConfig.load(worthConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
