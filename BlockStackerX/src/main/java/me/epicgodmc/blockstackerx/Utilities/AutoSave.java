package me.epicgodmc.blockstackerx.Utilities;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.MySqlManager;
import me.epicgodmc.blockstackerx.Data.YamlData;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class AutoSave
{

    private int saveIntervalMinutes;

    public AutoSave(BlockStackerX plugin)
    {
        if (plugin.config.getBoolean("Autosave")) {
            System.out.print("[BlockStackerX] Stating AutoSave Thread");
            YamlData yamlData = plugin.yamlData;
            MySqlManager mySqlManager = plugin.mySqlManager;
            saveIntervalMinutes = plugin.config.getInt("AutosaveInterval");
            int minute = (60 * 20);

            new BukkitRunnable() {
                @Override
                public void run() {

                    System.out.print("[BlockStackerX] AutoSaving");
                    try {
                        if (!plugin.useSql) {
                            yamlData.save();
                        } else {
                            if (plugin.getConnection() != null && !plugin.getConnection().isClosed()) {
                                mySqlManager.updateAndSave();
                            } else {
                                yamlData.save();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }.runTaskTimer(plugin, saveIntervalMinutes * minute, saveIntervalMinutes * minute);
        }
    }


}
