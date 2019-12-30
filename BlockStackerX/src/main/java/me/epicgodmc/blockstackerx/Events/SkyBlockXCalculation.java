package me.epicgodmc.blockstackerx.Events;

import io.illyria.skyblockx.core.IPlayer;
import io.illyria.skyblockx.event.IslandPostLevelCalcEvent;
import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.PlacedStacks;
import me.epicgodmc.blockstackerx.Objects.StackerPlaced;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;
import java.util.UUID;

public class SkyBlockXCalculation implements Listener
{

    private BlockStackerX plugin = BlockStackerX.getInstance();
    private PlacedStacks placedStacks = plugin.placedStacks;
    private MessageManager mm = plugin.messageManager;

    @EventHandler
    public void preLevelEvent(IslandPostLevelCalcEvent event)
    {

        double TotalLevelsFromStacks = 0;
        double TotalLevel = event.getLevelAfterCalc();
        IPlayer iPlayer = event.getIsland().getOwnerIPlayer();
        UUID uuid = UUID.fromString(iPlayer.getUuid());
        Set<String> teamMembers = iPlayer.getIsland().getAllMemberUUIDs();

        for (StackerPlaced sp : placedStacks.getPlacedStacksMap().values()) {
            if (sp.getChosenMaterial() != null) {
                if (sp.getUuid().equals(uuid) || teamMembers.contains(uuid.toString())) {
                    //is from this island
                    double levelsToAdd = sp.computeAndGetLevels();
                    TotalLevel += levelsToAdd;
                    TotalLevelsFromStacks += levelsToAdd;

                }
            }
        }




        Bukkit.getPlayer(uuid).sendMessage(mm.applyCC(mm.getMessage("calculationEvent", true).replace("%LEVELS%", ""+TotalLevelsFromStacks)));

    }


}
