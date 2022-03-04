package io.hysekai.game.squidgame.common.tasks;

import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.Cycle;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.state.PvPState;
import org.bukkit.Bukkit;

public class NightTask extends HysekaiRunTaskTimer.Asynchronously {

    private final Cycle cycle;

    public NightTask(Cycle cycle){
        super(cycle.getSquidGameManager(),20,0);
        this.cycle = cycle;
        this.cycle.setTime(10);

    }


    @Override
    public void run() {

        getPlugin().getSquidGamePlugin().getSidebar().update();

        if(cycle.getTime() == 30){
            getPlugin().getMessageUtils().sendBroadcast("§7§oLe PvP est maintenant activé.");
            cycle.setPvPState(PvPState.ALLOWED);
            //getNight().getSquidGameManager().getScoreboardManager().updateScoreBoards(3,"§8▏ §7Cycle : §9Nuit");
           // WorldsManager.SquidGameWorld.DORMITORY.setNight();
        }


        if(cycle.getNightText() != null)
            if(cycle.getTime() % 4 == 0)
                for(int text = 0; text<cycle.getNightText().size();text++)
                    Bukkit.broadcastMessage(cycle.getNightText().get(text));

        cycle.setTime(cycle.getTime() - 1);
        Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(cycle.getTime()));
        getPlugin().getMessageUtils().sendTimerToPlayingPlayers(cycle.getTime());


        if(cycle.getTime() == 0){
            cancelTask();
            cycle.startDay();
        }
    }

}
