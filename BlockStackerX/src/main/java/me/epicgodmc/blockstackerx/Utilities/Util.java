package me.epicgodmc.blockstackerx.Utilities;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import io.illyria.skyblockx.core.IPlayer;
import io.illyria.skyblockx.core.IPlayerKt;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.LoadedStacks;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util {
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;

    public boolean playerIsOnTeamOf(UUID player, UUID target) {
        if (BlockStackerX.aSkyBlock)
        {
            if (ASkyBlockAPI.getInstance().getTeamMembers(target).contains(player))
            {
                return true;
            }else return false;

        }

        else if (BlockStackerX.skyBlockX) {
            IPlayer Itarget = IPlayerKt.getIPlayerByUUID(target.toString());
            if (Itarget.hasIsland()) {
                if (Itarget.getIsland().getMembers().contains(player.toString()))
                {
                    return true;
                }else return false;
            }else return false;
        }
        return false;
    }

    public void checkConfiguration() {
        for (String s : plugin.config.getConfigurationSection("Stackers").getKeys(false)) {
            String materialValue = plugin.config.getString("Stackers." + s + ".itemType");
            Material material = Material.valueOf(materialValue.toUpperCase());
            if (material.isBlock()) {
                LoadedStacks.add(s, true);
            } else {
                LoadedStacks.add(s, false);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + mm.getMessage("invalidStacker", false).replace("%TYPE%", s));
            }

        }

    }


    public boolean stackerIsLoaded(String type) {
        return plugin.config.isSet("Stackers." + type);
    }


    public boolean isPickaxe(Material mat) {
        return mat.equals(Material.WOOD_PICKAXE) || mat.equals(Material.STONE_PICKAXE) || mat.equals(Material.GOLD_PICKAXE) || mat.equals(Material.IRON_PICKAXE) || mat.equals(Material.DIAMOND_PICKAXE);
    }

    public List<Material> getBlockList(String type) {
        List<String> confList = plugin.getConfig().getStringList("Stackers." + type + ".allowedBlocks");
        List<Material> matList = new ArrayList<>();
        for (String s : confList) {
            matList.add(Material.valueOf(s.toUpperCase()));
        }
        return matList;
    }

    public Vector getOffset(String type) {
        String[] data = plugin.config.getString("Stackers." + type + ".displayOffset").split(",");

        float x;
        float y;
        float z;

        try {

            x = Float.parseFloat(data[0]);
            y = Float.parseFloat(data[1]);
            z = Float.parseFloat(data[2]);
        } catch (Exception e) {
            plugin.getLogger().severe("displayOffset of Stacker " + type + " is invalid!!!");
            plugin.getLogger().severe("displayOffset of Stacker " + type + " is invalid!!!");
            return new Vector(0, 0, 0);
        }
        return new Vector(x, y, z);
    }


}
