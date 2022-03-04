package io.hysekai.game.squidgame.common.tasks;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarPriority;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.scoreboard.TimeAddon;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class BeginTask extends HysekaiRunTaskTimer {

    private final SquidGameManager squidGameManager;
    private int time = 10;


    public BeginTask(SquidGameManager squidGameManager, double ticks, long delay) {
        super(squidGameManager, ticks, delay);
        this.squidGameManager =  squidGameManager;

    }

    /*public BeginTask(SquidGameManager squidGameManager) {
        this.squidGameManager = squidGameManager;
    }*/


    @Override
    public void run() {
        time--;
        getSquidGameManager().getSquidGamePlugin().getSidebar().update();
        //getSquidGameManager().getScoreboardManager().updateScoreBoards(3,"§8▏ §7D\u00e9part dans §6" + this.time +" §7!");
        Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(time));

        float division =  Float.parseFloat(String.valueOf(time)) / 10.0F;
        Bukkit.getOnlinePlayers().forEach(player -> player.setExp(division));



        if(squidGameManager.getParticipantManager().getParticipants().size() < 1){
            cancel();
            return;
        }

        if(time == 60 || time == 30 || time == 15)
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.NOTE_PLING,50,0);
                HAPI.getUtils().sendTitle(player,0,20,0,"§b"+time ,"§7secondes restantes");
            });

        if(time <= 10)
            getSquidGameManager().getMessageUtils().sendTimerToPlayingPlayers(time);

        if(time == 0){
            cancelTask();
            squidGameManager.startGame();
        }

    }

    public SquidGameManager getSquidGameManager() { return squidGameManager; }
    public int getTime() {return time;}
}
