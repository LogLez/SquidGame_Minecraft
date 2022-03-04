package io.hysekai.game.squidgame.commands;

import org.bukkit.entity.Player;

public class InvCommand extends CommandArgument{

    public InvCommand() {
        super("inv");
    }

    @Override
    public void run(Player player, String[] args) {
        //HAPI.getMenuManager().displayMenuToPlayer(new PariGui(player,15),player);
    }
}