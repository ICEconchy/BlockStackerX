package me.epicgodmc.blockstackerx.Commands.SubCommands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.LoadedStacks;
import me.epicgodmc.blockstackerx.Objects.SubCommandConsole;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GiveCommandConsole extends SubCommandConsole
{
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private Util util = plugin.util;
    private MessageManager mm = plugin.messageManager;

    @Override
    public void onCommand(ConsoleCommandSender console, String[] args) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (util.stackerIsLoaded(args[1])) {
                        if (LoadedStacks.getEnabled(args[1])) {
                            target.getInventory().addItem(plugin.blockFactory.getStacker(args[1], 1));
                            target.sendMessage(mm.applyCC(mm.getMessage("receiveStacker", true)));
                        } else {
                            target.sendMessage(mm.applyCC(mm.getMessage("disabledStacker", true)));
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (args.length == 3) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (util.stackerIsLoaded(args[1])) {
                        int amt = 0;
                        try {
                            amt = Integer.parseInt(args[2]);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                        target.sendMessage(mm.applyCC(mm.getMessage("receiveStacker", true)));
                        target.getInventory().addItem(plugin.blockFactory.getStacker(args[1], amt));
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
    }

    @Override
    public String name() {
        return plugin.consoleCommandManager.give;
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
