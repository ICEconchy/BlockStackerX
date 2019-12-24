package me.epicgodmc.blockstackerx.Events;

import de.tr7zw.nbtapi.NBTItem;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Objects.SLocation;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
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

                    Material mat = event.getBlock().getType();
                    String type = nbtItem.getString("BlockStackerX");
                    Location displayLoc = event.getBlock().getLocation().add(util.getOffset(type));


                    ArmorStand display = util.createDisplay(type, 0, displayLoc);
                    int newID = plugin.placedStacks.getNewID();
                    StackerPlaced sp = new StackerPlaced(newID, type, uuid, null, loc, display, 0);
                } else if (nbtItem.hasKey("BlockStackerX_nonSolid")) {
                    String type = nbtItem.getString("StackerType");
                    Material chosenMat = Material.valueOf(nbtItem.getString("StackerMaterial").toUpperCase());
                    int value = nbtItem.getInteger("StackerValue");

                    Location displayLoc = event.getBlock().getLocation().add(util.getOffset(type));


                    ArmorStand display = util.createDisplay(type, value, displayLoc);
                    int newID = plugin.placedStacks.getNewID();
                    StackerPlaced sp = new StackerPlaced(newID, type, uuid, chosenMat, loc, display, value);

                }
            }
        }


    }


}
