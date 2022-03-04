package io.hysekai.game.squidgame.manager;

import io.hysekai.game.squidgame.commands.CommandArgument;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandManager {

    private List<CommandArgument> commandArguments = new ArrayList<>();

    public void registerCommandArgs(CommandArgument commandArgument){
        commandArguments.add(commandArgument);
    }

    public void executeCommand(Player player,String[] args){
        if(args.length == 0) return;

        String subArgument = args[0];
        Optional<CommandArgument> commandArgumentOptional = commandArguments.stream().filter(commandArgument1 -> commandArgument1.hasArgument(subArgument)).findFirst();

        if(commandArgumentOptional.isEmpty()) return;
        CommandArgument commandArgument = commandArgumentOptional.get();

        if(!commandArgument.hasPermission(player)){
            player.sendMessage("§cVous n'avez pas la permission");
            return;
        }

        commandArgument.run(player, Arrays.copyOfRange(args,1,args.length));
    }

    public List<CommandArgument> getCommandArguments() {
        return commandArguments;
    }

    public void setCommandArguments(List<CommandArgument> commandArguments) {
        this.commandArguments = commandArguments;
    }
}
