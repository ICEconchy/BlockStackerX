package me.epicgodmc.blockstackerx.Events;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.PlacedStacks;
import me.epicgodmc.blockstackerx.Utilities.DisplayManager;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.StackerNonSolid;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import me.epicgodmc.blockstackerx.Utilities.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class StackerBreak implements Listener {

    private BlockStackerX plugin = BlockStackerX.getInstance();
    private Util util = plugin.util;
    private MessageManager mm = plugin.messageManager;

    private PlacedStacks placedStacks = plugin.placedStacks;

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        if (placedStacks.contains(location)) {
            Player player = event.getPlayer();
            StackerPlaced sp = placedStacks.getStacker(location);
            if (sp.getUuid().equals(player.getUniqueId()) || plugin.config.getBoolean("Stackers." + sp.getType() + ".teamStacking") && util.playerIsOnTeamOf(player.getUniqueId(), sp.getUuid())) {
                if (player.hasPermission("bs.breakstacker")) {
                    Material mat = player.getItemInHand().getType();
                    if (util.isPickaxe(mat)) {


                        event.getPlayer().sendMessage(mm.applyCC(mm.getMessage("brokeStacker", true)));

                        DisplayManager.deleteDisplay(sp.getDisplay());
                        new StackerNonSolid(sp.getType(), sp.getChosenMaterial(), sp.getValue(), event.getPlayer().getUniqueId());
                        placedStacks.deleteStack(location);

                    } else {
                        event.setCancelled(true);
                        player.sendMessage(mm.applyCC(mm.getMessage("holdPickaxe", true)));
                        return;
                    }
                }else{
                    event.setCancelled(true);
                    player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
                    return;
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(mm.applyCC(mm.getMessage("notYourStacker", true)));
                return;
            }
        }


    }

}
