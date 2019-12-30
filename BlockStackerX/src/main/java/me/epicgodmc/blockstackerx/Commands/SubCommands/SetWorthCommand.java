package me.epicgodmc.blockstackerx.Commands.SubCommands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SetWorthCommand extends SubCommand
{

    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("bs.setworth")) {
            if (args.length == 0) {
                player.sendMessage(mm.applyCC(mm.getMessage("addValue", true)));
                return;
            }
            if (args.length == 1) {
                if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
                    double value = 0.0;
                    try {

                        value = Double.parseDouble(args[0]);

                    } catch (Exception e) {
                        player.sendMessage(mm.applyCC(mm.getMessage("notAnInt", true)));
                    }
                    Material mat = player.getItemInHand().getType();
                    plugin.worth.setWorth(mat, value);
                    player.sendMessage(mm.applyCC(mm.getMessage("worthSave", true).replace("%ITEM%", mat.toString()).replace("%VALUE%", "" + value)));
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
        return plugin.commandManager.setWorth;
    }

    @Override
    public String info() {
        return "null";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
