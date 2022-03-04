package io.hysekai.game.squidgame.commands;

import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import org.bukkit.entity.Player;

public class StartCommand extends CommandArgument{

    private final SquidGameManager squidGameManager;

    public StartCommand(SquidGameManager squidGameManager) {
        super("start");

        this.squidGameManager = squidGameManager;
    }

    @Override
    public void run(Player player, String[] args) {
        if(args.length != 1){
            player.sendMessage("§e/start §7<id> | Start la game avec le mini jeu voulu");
            player.sendMessage("§eVoici la liste des mini jeux");

            for(AbstractGame abstractGame : squidGameManager.getMiniGamesManager().getGames()){
                player.sendMessage("§7- " + abstractGame.getGameType().getName() + " --> " + abstractGame.getGameType().getId());
            }
            return;
        }
        int i = Integer.parseInt(args[0]);

        for(AbstractGame abstractGame : squidGameManager.getMiniGamesManager().getGames()){
            if(abstractGame.getGameType().getId() != i) continue;

            squidGameManager.getMiniGamesManager().setCurrentGame(abstractGame);
            squidGameManager.startPreGame();

        }
    }
}
