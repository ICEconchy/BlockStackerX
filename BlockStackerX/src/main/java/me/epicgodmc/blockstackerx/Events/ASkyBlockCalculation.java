package me.epicgodmc.blockstackerx.Events;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Messages;
import com.wasteofplastic.askyblock.events.IslandPreLevelEvent;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.PlacedStacks;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class ASkyBlockCalculation implements Listener
{

    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private PlacedStacks placedStacks = plugin.placedStacks;
    private MessageManager mm = plugin.messageManager;

    @EventHandler
    public void calc(IslandPreLevelEvent event)
    {
        double TotalLevelsFromStacks = 0;
        double TotalLevel = event.getLevel();
        UUID player = event.getPlayer();
        List<UUID> teamMembers = ASkyBlockAPI.getInstance().getTeamMembers(player);

        for (StackerPlaced SP : placedStacks.getPlacedStacksMap().values()) {
            if (SP.getChosenMaterial() != null) {
                if (SP.getUuid().equals(player) || teamMembers.contains(SP.getUuid())) {

                    //is from this island
                    double levelsToAdd = SP.computeAndGetLevels();
                    TotalLevel += levelsToAdd;
                    TotalLevelsFromStacks += levelsToAdd;
                }
            }
        }
        event.setLevel((int) TotalLevel);
        Bukkit.getPlayer(player).sendMessage(mm.applyCC(mm.getMessage("calculationEvent", true).replace("%LEVELS%", ""+TotalLevelsFromStacks)));


    }
}
