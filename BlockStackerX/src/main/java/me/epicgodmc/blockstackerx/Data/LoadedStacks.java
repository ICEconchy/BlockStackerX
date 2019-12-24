package me.epicgodmc.blockstackerx.Data;

import java.util.HashMap;
import java.util.Map;

public class LoadedStacks
{

    private static Map<String, Boolean> loaded = new HashMap<>();

    public static void add(String type, boolean enabled)
    {
        loaded.put(type, enabled);
    }
    public static boolean getEnabled(String type)
    {
        return loaded.get(type);
    }




}
