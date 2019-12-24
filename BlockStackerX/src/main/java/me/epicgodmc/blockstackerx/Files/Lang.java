package me.epicgodmc.blockstackerx.Files;

import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Lang
{
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);


    private File langConfigFile;
    private FileConfiguration langConfig;


    public FileConfiguration getLangConfig() {
        return this.langConfig;
    }

    public void reload()
    {
        try{
            langConfig.load(langConfigFile);
        }catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void createLangConfig() {
        langConfigFile = new File(plugin.getDataFolder(), "Lang.yml");
        if (!langConfigFile.exists()) {
            langConfigFile.getParentFile().mkdirs();
            plugin.saveResource("Lang.yml", false);
        }

        langConfig= new YamlConfiguration();
        try {
            langConfig.load(langConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
