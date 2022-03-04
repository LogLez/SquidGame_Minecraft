package io.hysekai.game.squidgame.common.tasks;

import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.scoreboard.TimeAddon;
import org.bukkit.Bukkit;

public class LoadGameTask extends HysekaiRunTaskTimer {

    private final AbstractGame abstractGame;
    private int timeLoad, i = 0;

    public LoadGameTask(AbstractGame abstractGame){
        super(abstractGame.getSquidGameManager(),20,0);
        this.abstractGame =abstractGame;
        this.timeLoad = getAbstractGame().getGameType().getPreTime();
    }


    @Override
    public void run() {
        timeLoad--;

        if(timeLoad % 4 == 0 && i < getAbstractGame().getAnnounce().size()) {
            Bukkit.broadcastMessage(getAbstractGame().getAnnounce().get(i));
            i++;
        }


        Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(timeLoad));
        getAbstractGame().onPreTime(timeLoad);

        if(timeLoad <= 10)
            getAbstractGame().getSquidGameManager().getMessageUtils().sendTimerToPlayingPlayers(timeLoad);


        if(timeLoad == 0){
            cancelTask();
            getAbstractGame().setGameStatus(GameStatus.GAME);
            getAbstractGame().onStart();
            return;
        }

        getAbstractGame().getSquidGameManager().getSquidGamePlugin().getSidebar().update();

    }


    public AbstractGame getAbstractGame() {return abstractGame;}
    public int getTimeLoad() {return timeLoad;}


}
