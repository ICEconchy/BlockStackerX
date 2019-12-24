package me.epicgodmc.blockstackerx.Utilities;

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
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Util
{
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private MessageManager mm = plugin.messageManager;

    public void reloadDisplays()
    {
        for (StackerPlaced sp : plugin.placedStacks.getPlacedStacksMap().values())
        {
            sp.getDisplay().setCustomName("");
            sp.getDisplay().setCustomName(plugin.config.getString("Stackers."+sp.getType()+".valueFormat").replace("%VALUE%", ""+sp.getValue()));


            Location displayLoc = sp.getDisplay().getLocation();
            KillDisplay(sp.getDisplay());
            sp.setDisplay(createDisplay(sp.getType(), sp.getValue(), displayLoc));
        }
    }


    public void checkConfiguration()
    {
        for (String s : plugin.config.getConfigurationSection("Stackers").getKeys(false))
        {
            String materialValue = plugin.config.getString("Stackers."+s+".itemType");
            Material material = Material.valueOf(materialValue.toUpperCase());
            if (material.isBlock())
            {
                LoadedStacks.add(s, true);
            }else
            {
                LoadedStacks.add(s, false);
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED+mm.getMessage("invalidStacker", false).replace("%TYPE%", s));
            }

        }

    }


    public boolean stackerIsLoaded(String type)
    {
        return plugin.config.isSet("Stackers."+type);
    }


    public boolean isPickaxe(Material mat)
    {
        return mat.equals(Material.WOOD_PICKAXE) || mat.equals(Material.STONE_PICKAXE) || mat.equals(Material.GOLD_PICKAXE) || mat.equals(Material.IRON_PICKAXE) || mat.equals(Material.DIAMOND_PICKAXE);
    }


    public void killAllDisplays()
    {
        for (StackerPlaced sp : plugin.placedStacks.getPlacedStacksMap().values())
        {
            KillDisplay(sp.getDisplay());
        }
    }
    public void KillDisplay(Entity display)
    {
        display.remove();
    }

    public void UpdateDisplay(String type, ArmorStand display, int value)
    {
        if (value == 1)
        {
            display.setCustomName("");
        }
        String newName = plugin.getConfig().getString("Stackers."+type+".valueFormat").replace("%VALUE%", ""+value);
        display.setCustomName(mm.applyCC(newName));

    }
    public ArmorStand createDisplay(String type, int value, Location location) {
        String name = plugin.getConfig().getString("Stackers."+type+".valueFormat").replace("%VALUE%", ""+value);

        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setGravity(false);
        stand.setCustomName(mm.applyCC(name));
        stand.setCustomNameVisible(true);
        stand.setVisible(false);

        return stand;
    }

    public List<Material> getBlockList(String type)
    {
        List<String> confList = plugin.getConfig().getStringList("Stackers."+type+".allowedBlocks");
        List<Material> matList = new ArrayList<>();
        for (String s : confList)
        {
            matList.add(Material.valueOf(s.toUpperCase()));
        }
        return matList;
    }

    public Vector getOffset(String type)
    {
        String[] data = plugin.config.getString("Stackers."+type+".displayOffset").split(",");

        float x;
        float y;
        float z;

        try{

            x = Float.parseFloat(data[0]);
            y = Float.parseFloat(data[1]);
            z = Float.parseFloat(data[2]);
        }catch (Exception e)
        {
            plugin.getLogger().severe("displayOffset of Stacker "+type+" is invalid!!!");
            plugin.getLogger().severe("displayOffset of Stacker "+type+" is invalid!!!");
            return new Vector(0,0,0);
        }
        return new Vector(x, y, z);
    }










}
