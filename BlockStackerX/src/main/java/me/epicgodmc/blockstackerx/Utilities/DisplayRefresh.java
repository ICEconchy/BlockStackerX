package me.epicgodmc.blockstackerx.Utilities;

import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.scheduler.BukkitRunnable;

public class DisplayRefresh
{


    public DisplayRefresh(BlockStackerX plugin)
    {
        Util util = plugin.util;

        new BukkitRunnable() {
            @Override
            public void run() {
                util.reloadDisplays();
            }
        }.runTaskTimer(plugin, 60*20, 60*20);
    }
}
