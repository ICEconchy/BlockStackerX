package me.epicgodmc.blockstackerx.Commands;

import me.epicgodmc.blockstackerx.BlockStackerX;
import me.epicgodmc.blockstackerx.Commands.SubCommands.GiveCommandConsole;
import me.epicgodmc.blockstackerx.Objects.SubCommand;
import me.epicgodmc.blockstackerx.Objects.SubCommandConsole;
import me.epicgodmc.blockstackerx.Utilities.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ConsoleCommandManager implements CommandExecutor
{

    private ArrayList<SubCommandConsole> commands = new ArrayList<>();
    private BlockStackerX plugin = BlockStackerX.getInstance();
    private MessageManager mm = plugin.messageManager;


    public String main = "blockstackerconsole";


    //SubCommands
    public String give = "give";
    //

    public void setup()
    {
        plugin.getCommand(main).setExecutor(this);

        this.commands.add(new GiveCommandConsole());

    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            return true;
        }
        ConsoleCommandSender console = (ConsoleCommandSender) sender;
        if (cmd.getName().equalsIgnoreCase(main)) {
            if (args.length == 0) {
                return true;
            }
            SubCommandConsole target = this.get(args[0]);

            if (target == null){
                return true;
            }


            ArrayList<String> argList = new ArrayList<String>();
            argList.addAll(Arrays.asList(args));
            argList.remove(0);

            String[] arguments = argList.toArray(new String[argList.size()]);

            try{
                target.onCommand(console, arguments);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        return true;
    }

    private SubCommandConsole get(String name) {
        Iterator<SubCommandConsole> subcommands = this.commands.iterator();

        while (subcommands.hasNext()) {
            SubCommandConsole sCmd = (SubCommandConsole) subcommands.next();

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
