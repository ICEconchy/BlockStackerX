package me.epicgodmc.blockstackerx.Commands.SubCommands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.LoadedStacks;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import org.bukkit.entity.Player;

public class ListCommand extends SubCommand
{
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("bs.list")) {
            if (args.length == 0) {
                player.sendMessage(mm.applyCC(mm.getMessage("prefix", false) + "list types: \n&f&l1. &9loaded"));
                return;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("loaded")) {
                    player.sendMessage(mm.applyCC(mm.getMessage("prefix", false) + "Loaded Stackers:"));
                    int index = 1;
                    for (String s : plugin.config.getConfigurationSection("Stackers").getKeys(false)) {
                        if (LoadedStacks.getEnabled(s)) {
                            player.sendMessage(mm.applyCC("&f&l" + index + ". &9" + s));
                        } else {
                            player.sendMessage(mm.applyCC("&f&l" + index + ". &9" + s + " &4&l[DISABLED] - TYPE MUST BE A BLOCK"));
                        }

                    }
                }
            } else {
                player.sendMessage(mm.applyCC(mm.getMessage("cmdNotRecognized", true)));
            }
        }else{
            player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
            return;
        }
    }

    @Override
    public String name() {
        return plugin.commandManager.list;
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
