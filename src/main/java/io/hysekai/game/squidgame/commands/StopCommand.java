package io.hysekai.game.squidgame.commands;

import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.state.SquidGameState;
import org.bukkit.entity.Player;

public class StopCommand extends CommandArgument{

    private final SquidGameManager squidGameManager;

    public StopCommand(SquidGameManager squidGameManager) {
        super("stop");
        this.squidGameManager = squidGameManager;
    }

    @Override
    public void run(Player player, String[] args) {
        if(args.length != 0){
            player.sendMessage("§e/sg stop | Stop la partie");
            return;
        }

        if(squidGameManager.getSquidGameState() == SquidGameState.WAITING) return;
        squidGameManager.stopGame();
    }
}