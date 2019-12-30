package me.epicgodmc.blockstackerx.Commands.SubCommands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Data.MySqlManager;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SyncCommand extends SubCommand
{

    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MySqlManager mySqlManager = plugin.mySqlManager;
    private MessageManager mm = plugin.messageManager;


    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0)
        {
            if (player.hasPermission("bs.sync")) {
                player.sendMessage(mm.applyCC(mm.getMessage("syncAttempt", true)));
                try {

                    if (plugin.useSql && plugin.getConnection() != null && !plugin.getConnection().isClosed()) {
                        if (mySqlManager.syncYaml())
                        {
                            player.sendMessage(mm.applyCC(mm.getMessage("syncSuccess", true)));
                        }else{
                            player.sendMessage(mm.applyCC(mm.getMessage("syncFail", true)));
                        }

                    } else {
                        player.sendMessage(mm.applyCC(mm.getMessage("sqlConnectionError", true)));
                        return;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage(mm.applyCC(mm.getMessage("sqlConnectionError", true)));
                }
            }else{
                player.sendMessage(mm.applyCC(mm.getMessage("noPermission", true)));
                return;
            }
        }else{
            player.sendMessage(mm.applyCC(mm.getMessage("cmdNotRecognized", true)));
        }
    }

    @Override
    public String name() {
        return plugin.commandManager.sync;
    }

    @Override
    public String info() {
        return "Sync yaml config with mySql Database";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
