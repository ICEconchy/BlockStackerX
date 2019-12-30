package me.epicgodmc.blockstackerx.Utilities;

import me.epicgodmc.blockstackerx.BlockStackerX;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager
{
    private BlockStackerX plugin = BlockStackerX.getInstance();

    public String applyCC(String input)
    {
        return ChatColor.translateAlternateColorCodes('&', input);
    }


    public String getMessage(String message, boolean prefix)
    {
        if (prefix)return plugin.lang.getLangConfig().getString("prefix")+plugin.lang.getLangConfig().getString(message);
        else return plugin.lang.getLangConfig().getString(message);
    }

    public void sendUsage(Player player)
    {
        for (String s : plugin.lang.getLangConfig().getStringList("usage"))
        {
            player.sendMessage(applyCC(s));
        }
    }

}
