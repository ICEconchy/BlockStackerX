package me.epicgodmc.blockstackerx.Commands.SubCommands;

import de.tr7zw.nbtapi.NBTItem;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.MySqlManager;
import me.epicgodmc.blockstackerx.Utilities.DisplayManager;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DebugCommand extends SubCommand
{
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private MessageManager mm = plugin.messageManager;
    private MySqlManager mySqlManager = new MySqlManager();

    // /bs debug killdisplays <radius>

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("bs.debug")) {
            if (args.length == 0) {
                player.sendMessage(mm.applyCC(mm.getMessage("prefix", false) + "Debug Params:"));
                player.sendMessage(mm.applyCC("&f&l1. &9PlacedStacks\n&f&l2. &9printnbt\n&f&l3. &9mysql\n&f&l4. &9RemoveHolograms <radius>"));
                return;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reloadDisplays"))
                {
                    player.sendMessage(mm.applyCC(mm.getMessage("displayReload", true)));
                    DisplayManager.reloadAllDisplays(plugin);
                }
                if (args[0].equalsIgnoreCase("mysql"))
                {
                    player.sendMessage(mm.applyCC(mm.getMessage("prefix", false) + "mySql Params"));
                    player.sendMessage(mm.applyCC("&f&l1. &9save\n&f&l2. &9load\n&f&l3. &9updateAndSave\n&f&l4. &9" +
                            "checkfortable <table>\n&f&l5. &9checkforID <ID>"));
                    return;
                }
                if (args[0].equalsIgnoreCase("printnbt")) {
                    if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
                        NBTItem nbtItem = new NBTItem(player.getItemInHand());
                        player.sendMessage(nbtItem.asNBTString());
                    } else {
                        player.sendMessage("Please hold an item");
                    }
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("removeHolograms"))
                {
                    int radius;
                    try{

                        radius = Integer.parseInt(args[1]);
                    }catch (Exception e)
                    {
                        player.sendMessage(mm.applyCC(mm.getMessage("notAnInt", true)));
                        return;
                    }
                    player.sendMessage(mm.applyCC(mm.getMessage("prefix", false)+"Removing armorstands in radius: "+radius));

                    AtomicInteger amt = new AtomicInteger();
                    List<Entity> nearbyEntities = player.getNearbyEntities(radius,radius,radius);
                    nearbyEntities.forEach((e) -> {
                        if (e instanceof ArmorStand)
                        {
                            amt.incrementAndGet();
                            e.remove();
                        }
                    });
                    player.sendMessage(mm.applyCC(mm.getMessage("prefix", false)+"Removed "+amt+" Armorstands"));


                }
                if (args[0].equalsIgnoreCase("mysql")) {
                    if (args[1].equalsIgnoreCase("updateAndSave")) {
                        mySqlManager.updateAndSave();
                    }
                    if (args[1].equalsIgnoreCase("save")) {
                        mySqlManager.save();
                    }
                    if (args[1].equalsIgnoreCase("load")) {
                        mySqlManager.load();
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("mysql")) {
                    if (args[1].equalsIgnoreCase("checkfortable")) {
                        mySqlManager.checkForTable(args[2]);
                    }
                    if (args[1].equalsIgnoreCase("checkforID")) {
                        if (mySqlManager.containsID(Integer.parseInt(args[2]))) {
                            player.sendMessage("true");
                        } else player.sendMessage("false");
                    }
                }
            }
        }else
        {
            player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
            return;
        }
    }

    @Override
    public String name() {
        return plugin.commandManager.debug;
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
