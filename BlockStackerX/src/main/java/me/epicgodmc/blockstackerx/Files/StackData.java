package me.epicgodmc.blockstackerx.Files;

import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class StackData
{

    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);


    private File stackdataConfigFile;
    private FileConfiguration stackdataConfig;


    public FileConfiguration getStackDataConfig() {
        return this.stackdataConfig;
    }


    public void saveStackDataConfig()
    {
        try{

            stackdataConfig.save(stackdataConfigFile);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void reload()
    {
        try{
            stackdataConfig.load(stackdataConfigFile);
        }catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void createStackDataConfig() {
        stackdataConfigFile = new File(plugin.getDataFolder(), "StackData.yml");
        if (!stackdataConfigFile.exists()) {
            stackdataConfigFile.getParentFile().mkdirs();
            plugin.saveResource("StackData.yml", false);
        }

        stackdataConfig= new YamlConfiguration();
        try {
            stackdataConfig.load(stackdataConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
