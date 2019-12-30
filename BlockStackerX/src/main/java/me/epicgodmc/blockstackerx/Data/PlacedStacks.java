package me.epicgodmc.blockstackerx.Data;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public class PlacedStacks {

    private HashMap<Location, StackerPlaced> placedStacks = new HashMap<>();


    public void addStack(Location loc, StackerPlaced SP) {
        placedStacks.put(loc, SP);
    }
    public void deleteStack(Location loc)
    {
        placedStacks.remove(loc);
    }
    public boolean contains(Location location)
    {
        return placedStacks.containsKey(location);
    }
    public StackerPlaced getStacker(Location location)
    {
        return placedStacks.get(location);
    }
    public HashMap<Location, StackerPlaced> getPlacedStacksMap()
    {
        return placedStacks;
    }

    public StackerPlaced getFirst()
    {
        return (StackerPlaced) placedStacks.values().toArray()[0];
    }

    public int getNewID()
    {
        return placedStacks.size()+1;
    }


}
