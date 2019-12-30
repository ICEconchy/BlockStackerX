package me.epicgodmc.blockstackerx.Data;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class YamlData
{
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;

    private PlacedStacks placedStacks = plugin.placedStacks;

    public int load()
    {
        if (plugin.stackData.getStackDataConfig().isSet("Data")) {
            int amt = 0;
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + mm.getMessage("yamlLoad", false));
            for (String s : plugin.stackData.getStackDataConfig().getStringList("Data")) {
                new StackerPlaced(s);
                amt++;
            }
            return amt;
        }else{
            return 0;
        }

    }

    public void save()
    {
        if (!placedStacks.getPlacedStacksMap().isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + mm.getMessage("yamlSave", false));
            List<String> confList = plugin.stackData.getStackDataConfig().getStringList("Data");
            confList.clear();

            int amt = 0;
            for (StackerPlaced sp : placedStacks.getPlacedStacksMap().values()) {
                confList.add(sp.toString());
                amt++;

            }
            plugin.stackData.getStackDataConfig().set("Data", confList);
            plugin.stackData.saveStackDataConfig();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN+"Saved "+amt+" Stackers");
        }else return;
    }
}
