package me.epicgodmc.blockstackerx.Commands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Commands.SubCommands.*;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CommandManager implements CommandExecutor
{
    private ArrayList<SubCommand> commands = new ArrayList<>();
    private BlockStackerX plugin = BlockStackerX.getPlugin(BlockStackerX.class);
    private MessageManager mm = plugin.messageManager;


    public CommandManager() {
    }


    public String main = "blockstacker";
    //Sub Commands
    public String debug = "debug";
    public String give = "give";
    public String list = "list";
    public String sync = "sync";
    public String getWorth = "getWorth";
    public String setWorth = "setWorth";

    //////

    public void setup() {
        plugin.getCommand(main).setExecutor(this);

        this.commands.add(new DebugCommand());
        this.commands.add(new GiveCommand());
        this.commands.add(new ListCommand());
        this.commands.add(new SyncCommand());
        this.commands.add(new GetWorthCommand());
        this.commands.add(new SetWorthCommand());


    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(mm.applyCC(mm.getMessage("onlyPlayers", true)));
            return true;
        }
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(main)) {
            if (args.length == 0) {
                mm.sendUsage(player);
                return true;
            }
            SubCommand target = this.get(args[0]);

            if (target == null){
                player.sendMessage(mm.applyCC(mm.getMessage("cmdNotRecognized", true)));
                return true;
            }


            ArrayList<String> argList = new ArrayList<String>();
            argList.addAll(Arrays.asList(args));
            argList.remove(0);

            String[] arguments = argList.toArray(new String[argList.size()]);

            try{
                target.onCommand(player, arguments);
            }catch (Exception e)
            {
                player.sendMessage(mm.applyCC(mm.getMessage("error", true)));
                e.printStackTrace();
            }

        }
        return true;
    }

    private SubCommand get(String name) {
        Iterator<SubCommand> subcommands = this.commands.iterator();

        while (subcommands.hasNext()) {
            SubCommand sCmd = (SubCommand) subcommands.next();

            if (sCmd.name().equalsIgnoreCase(name)) {
                return sCmd;
            }

            String[] aliases;
            int length = (aliases = sCmd.aliases()).length;

            for (int var5 = 0; var5 < length; ++var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sCmd;
                }
            }
        }
        return null;
    }
}
