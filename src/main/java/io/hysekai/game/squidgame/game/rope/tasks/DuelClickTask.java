package io.hysekai.game.squidgame.game.rope.tasks;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.rope.data.PlayerRopeGame;
import io.hysekai.game.squidgame.game.rope.duel.Duel;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.manager.rope.DuelManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public class DuelClickTask extends HysekaiRunTaskTimer {

    private final DuelManager duelManager;
    private Duel currentDuel;

    public DuelClickTask(DuelManager duelManager, double ticks, long delay) {
        super(duelManager.getRope().getSquidGameManager(), ticks, delay);
        this.duelManager = duelManager;
    }


    @Override
    public void run() {


        if(getDuelManager().getRope().getGameStatus() == GameStatus.FINISH){
            Bukkit.getOnlinePlayers().forEach(player -> HAPI.getUtils().sendAboveHotbarMessage(player, ""));
            cancelTask();
            return;
        }

        if(getDuelManager().getDuelClickTask().getCurrentDuel() == null || getDuelManager().getDuelClickTask().getCurrentDuel().getDuelStatus() != Duel.DuelStatus.PLAYING) return;


        List<PlayerRopeGame> playerRopeGames = new ArrayList<>(getDuelManager().getTasks().values());

        for (PlayerRopeGame playerRopeGame : playerRopeGames) {
            if(!playerRopeGame.getParticipantPlayer().isPlaying()) continue;

            playerRopeGame.getParticipantPlayer().sendActions(player -> {
                if(playerRopeGame.getSafeZone()[0] <= playerRopeGame.getCurrent() &&  playerRopeGame.getCurrent() <= playerRopeGame.getSafeZone()[1])
                    playerRopeGame.setSafe(true);
                else
                    playerRopeGame.setSafe(false);

                StringBuilder st = new StringBuilder("");
                for(int i = 0; i < playerRopeGame.getCurrent(); i++){

                    if(playerRopeGame.getSafeZone()[0] <= i && playerRopeGame.getSafeZone()[1] >= i){
                        st.append("§a▌");
                        continue;
                    }
                    st.append("§c▌");
                }

                st.append("§6▌");
                for(int i = playerRopeGame.getCurrent() + 1; i < 31; i++){
                    if(playerRopeGame.getSafeZone()[0] <= i && playerRopeGame.getSafeZone()[1] >= i){
                        st.append("§a▌");
                        continue;
                    }
                    st.append("§c▌");
                }
                HAPI.getUtils().sendAboveHotbarMessage(player, st.toString());
                playerRopeGame.incremente();

                if(checkWinner()) return;

            });
        }
    }

    private boolean checkWinner(){
        if(getCurrentDuel() == null) return false;
        int maxPoints = 5;
        int maxPlayers = getDuelManager().getRope().getMaxPlayersPerTeam();

        if(maxPlayers == 4) maxPoints = 60;
        if(maxPlayers == 6) maxPoints = 80;

        if(getCurrentDuel().getTeam1().getPoints() >= maxPoints) {
            getCurrentDuel().stop(getCurrentDuel().getTeam1(), getCurrentDuel().getTeam2());
            return true;
        }

        if(getCurrentDuel().getTeam2().getPoints() >= maxPoints) {
            getCurrentDuel().stop(getCurrentDuel().getTeam2(), getCurrentDuel().getTeam1());
            return true;
        }

        return false;

    }


    public DuelManager getDuelManager() {return duelManager;}
    public Duel getCurrentDuel() {return currentDuel;}
    public boolean hasCurrentDuel() {return  currentDuel != null;}
    public void setCurrentDuel(Duel currentDuel) {
        this.currentDuel = currentDuel;
        if(getDuelManager().getHologramScore() == null)
            getDuelManager().setHologram(new Location(Bukkit.getWorld("world"),-75,38,36));

        getDuelManager().resetHologram();
    }

}
