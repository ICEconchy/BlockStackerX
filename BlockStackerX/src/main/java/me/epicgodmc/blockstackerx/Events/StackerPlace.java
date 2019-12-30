package me.epicgodmc.blockstackerx.Events;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import de.tr7zw.nbtapi.NBTItem;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Objects.SLocation;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import me.epicgodmc.blockstackerx.Utilities.DisplayManager;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class StackerPlace implements Listener {
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private Util util = plugin.util;
    private MessageManager mm = plugin.messageManager;


    @EventHandler(priority = EventPriority.LOWEST)
    public void place(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            ItemStack item = event.getItemInHand();
            NBTItem nbtItem = new NBTItem(item);
            if (nbtItem.hasNBTData()) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();
                SLocation loc = new SLocation(event.getBlock().getLocation());

                if (nbtItem.hasKey("BlockStackerX")) {
                    if (player.hasPermission("bs.placeStacker")) {
                        Material mat = event.getBlock().getType();
                        String type = nbtItem.getString("BlockStackerX");
                        Location displayLoc = event.getBlock().getLocation().add(util.getOffset(type));


                        Hologram hologram = DisplayManager.createDisplay(plugin, type, 0, displayLoc);
                        int newID = plugin.placedStacks.getNewID();
                        StackerPlaced sp = new StackerPlaced(newID, type, uuid, null, loc, hologram, 0);
                    }else{
                        event.setCancelled(true);
                        player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
                        return;
                    }
                } else if (nbtItem.hasKey("BlockStackerX_nonSolid")) {
                    if (player.hasPermission("bs.placePickupStacker")) {
                        String type = nbtItem.getString("StackerType");
                        Material chosenMat = Material.valueOf(nbtItem.getString("StackerMaterial").toUpperCase());
                        int value = nbtItem.getInteger("StackerValue");

                        Location displayLoc = event.getBlock().getLocation().add(util.getOffset(type));


                        Hologram hologram = DisplayManager.createDisplay(plugin, type, value, displayLoc);
                        int newID = plugin.placedStacks.getNewID();
                        StackerPlaced sp = new StackerPlaced(newID, type, uuid, chosenMat, loc, hologram, value);
                    }else{
                        event.setCancelled(true);
                        player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
                        return;
                    }
                }
            }
        }


    }


}
