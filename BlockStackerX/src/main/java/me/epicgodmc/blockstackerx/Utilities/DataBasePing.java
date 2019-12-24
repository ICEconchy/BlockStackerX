package me.epicgodmc.blockstackerx.Utilities;

import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;


public class DataBasePing
{


    public DataBasePing(BlockStackerX plugin)
    {

        new BukkitRunnable() {
            @Override
            public void run() {

                try{
                    System.out.print("[BlockStackerX] Pinging database");
                    plugin.mySqlManager.sendQuery("SELECT * FROM `"+plugin.getTable()+"`");
                }catch (SQLException e)
                {
                    e.printStackTrace();
                }


            }
        }.runTaskTimer(plugin, 60*20,60*20);

    }


}
