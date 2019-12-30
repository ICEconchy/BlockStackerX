package me.epicgodmc.blockstackerx.Commands.SubCommands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.LoadedStacks;
import me.epicgodmc.blockstackerx.Objects.StackerNonSolid;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import me.epicgodmc.blockstackerx.Utilities.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand {
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private Util util = plugin.util;
    private MessageManager mm = plugin.messageManager;


    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("bs.give")) {
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
                        player.sendMessage(mm.applyCC(mm.getMessage("stackerNotLoaded", true)));
                    }
                } else {
                    player.sendMessage(mm.applyCC(mm.getMessage("playerNotFound", true)));
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
                            player.sendMessage(mm.applyCC(mm.getMessage("notAnInt", true)));
                            e.printStackTrace();
                            return;
                        }
                        target.sendMessage(mm.applyCC(mm.getMessage("receiveStacker", true)));
                        target.getInventory().addItem(plugin.blockFactory.getStacker(args[1], amt));
                    } else {
                        player.sendMessage(mm.applyCC(mm.getMessage("stackerNotLoaded", true)));
                    }
                } else {
                    player.sendMessage(mm.applyCC(mm.getMessage("playerNotFound", true)));
                    return;
                }
            }
            // /bs give {player} {type} {material} {value}
            if (args.length == 4)
            {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null)
                {
                    if (util.stackerIsLoaded(args[1])) {
                        Material mat = Material.valueOf(args[2]);
                        if (mat != null) {
                            if (util.getBlockList(args[1]).contains(mat)) {
                                int value;
                                try {

                                    value = Integer.parseInt(args[3]);

                                } catch (Exception e) {
                                    player.sendMessage(mm.applyCC(mm.getMessage("notAnInt", true)));
                                    e.printStackTrace();
                                    return;
                                }
                                target.sendMessage(mm.applyCC(mm.getMessage("receiveStacker", true)));
                                new StackerNonSolid(args[1], mat, value, target.getUniqueId());
                            }else{
                                target.sendMessage(mm.applyCC(mm.getMessage("prefix",false)+"Material not allowed in this stacker!"));
                            }
                        }else{
                            target.sendMessage(mm.applyCC(mm.getMessage("prefix", false)+"Invalid Material!"));
                        }
                    } else {
                        player.sendMessage(mm.applyCC(mm.getMessage("stackerNotLoaded", true)));
                    }

                }else {
                    player.sendMessage(mm.applyCC(mm.getMessage("playerNotFound", true)));
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
        return plugin.commandManager.give;
    }

    @Override
    public String info() {
        return "Command to give BlockStackerX related items";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
