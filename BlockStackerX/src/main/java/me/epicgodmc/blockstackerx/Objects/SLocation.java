package me.epicgodmc.blockstackerx.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SLocation
{

    String world;
    double x;
    double y;
    double z;

    public SLocation(String world, double x, double y, double z)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SLocation(Location location)
    {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public boolean equals(SLocation location)
    {
        if (location.world.equalsIgnoreCase(this.world) && location.x == this.x && location.y == this.y && location.z == this.z)
        {
            return true;
        }
        return false;
    }

    public Location toLocation()
    {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z);
    }
    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
