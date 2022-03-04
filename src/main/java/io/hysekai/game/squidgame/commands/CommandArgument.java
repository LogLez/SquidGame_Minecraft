package io.hysekai.game.squidgame.commands;

import org.bukkit.entity.Player;

import java.util.Arrays;

public abstract class CommandArgument {

    private String argument;
    private String[] aliases;
    private String permission;

    public CommandArgument(String argument){
        this(argument,new String[]{},"squidgame." + argument);
    }

    public CommandArgument(String argument,String permission){
        this(argument,new String[]{},permission);
    }

    public CommandArgument(String argument, String[] aliases, String permission) {
        this.argument = argument;
        this.aliases = aliases;
        this.permission = permission;
    }

    public abstract void run(Player player,String[] args);

    public boolean hasArgument(String argument){
        if(this.argument.equalsIgnoreCase(argument)) return true;
        boolean hasArgument = false;
        for(String aliase : aliases){
            if(aliase.equalsIgnoreCase(argument)) hasArgument = true;
        }

        return hasArgument;
    }

    public boolean hasPermission(Player player){
        return player.isOp() || player.hasPermission(permission);
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
