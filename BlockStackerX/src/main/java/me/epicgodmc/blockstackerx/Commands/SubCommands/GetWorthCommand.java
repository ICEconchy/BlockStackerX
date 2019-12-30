package me.epicgodmc.blockstackerx.Commands.SubCommands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GetWorthCommand extends SubCommand
{
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("bs.getworth")) {
            if (args.length == 0) {
                if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
                    player.sendMessage(mm.applyCC(mm.getMessage("worth", true).replace("%WORTH%", "" + plugin.worth.getWorth(player.getItemInHand().getType()))));
                } else {
                    player.sendMessage(mm.applyCC(mm.getMessage("noBlockInHand", true)));
                    return;
                }
            }
        }else{
            player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
            return;
        }
    }

    @Override
    public String name() {
        return plugin.commandManager.getWorth;
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
