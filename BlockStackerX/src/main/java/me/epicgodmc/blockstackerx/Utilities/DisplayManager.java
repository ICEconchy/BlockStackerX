package me.epicgodmc.blockstackerx.Utilities;


import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class DisplayManager
{

    private static List<Hologram> displays = new ArrayList<>();

    public static Hologram createDisplay(BlockStackerX plugin, String type, int value, Location location)
    {
        String name = plugin.messageManager.applyCC(plugin.getConfig().getString("Stackers." + type + ".valueFormat").replace("%VALUE%", "" + value));

        Hologram hologram = HologramsAPI.createHologram(plugin, location);
        hologram.appendTextLine(name);

        displays.add(hologram);

        return hologram;
    }

    public static void deleteAllDisplays()
    {
        displays.forEach(Hologram::delete);
    }
    public static void deleteDisplay(Hologram hologram)
    {
        hologram.delete();
    }

    public static void updateHologram(BlockStackerX plugin, Hologram hologram, String type, int value)
    {
        String newName = plugin.messageManager.applyCC(plugin.getConfig().getString("Stackers." + type + ".valueFormat").replace("%VALUE%", "" + value));
        hologram.insertTextLine(0, newName);
        hologram.getLine(1).removeLine();

    }

    public static void reloadAllDisplays(BlockStackerX plugin)
    {
        for (StackerPlaced sp : plugin.placedStacks.getPlacedStacksMap().values())
        {
            Hologram replacement = createDisplay(plugin, sp.getType(), sp.getValue(), sp.getDisplay().getLocation());
            sp.getDisplay().delete();
            sp.setDisplay(replacement);
        }

    }
}
