package me.epicgodmc.blockstackerx.Objects;

import org.bukkit.command.ConsoleCommandSender;

public abstract class SubCommandConsole
{


    public SubCommandConsole()
    {}

    public abstract void onCommand(ConsoleCommandSender console, String[] args);

    public abstract String name();
    public abstract String info();
    public abstract String[] aliases();
}
