package io.hysekai.game.squidgame.common.tasks;

import fr.hysekai.schematic.schematic.PasteOptions;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.duel.Duel;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.utils.Numbers;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MiniGamesCountDownTask extends HysekaiRunTaskTimer {

    private final AbstractGame abstractGame;
    private int time = 0;

    //TODO PASTE EVERY SECOND schematic numbers in class Numbers (enumeration package )
    //fr.hysekai.squidgame.game.bridge.chasubles;

    public MiniGamesCountDownTask(SquidGameManager squidGameManager, AbstractGame abstractGame) {
        super(squidGameManager,20,0);
        this.abstractGame = abstractGame;
        this.time = abstractGame.getGameType().getTime();
    }

    @Override
    public void run() {
        if(abstractGame.getGameStatus() == GameStatus.FINISH || !getPlugin().getMiniGamesManager().getCurrentGame().equals(abstractGame)){
            cancel();
            getPlugin().getMiniGamesManager().setMiniGamesCountDownTask(null);
            return;
        }


        String timeInText = getAbstractGame().getSquidGameManager().getTimeUtils().formatedTime(time);
        for(int i =0 ; i < 5;i++){
            if(i == 2) continue;
            Optional<Numbers> optionalNumber = Numbers.getNumber(timeInText.charAt(i));
            int finalI = i;
            optionalNumber.ifPresent(numbers -> {
                Location location = getAbstractGame().getTimeLocation()[finalI];
                if(location != null){
                   numbers.getSchematic().paste(location, PasteOptions.ROTATE_AND_COPY_AIR);
                }

            });
        }


        if(this.abstractGame instanceof Rope){
            if(this.abstractGame.getGameStatus() != GameStatus.GAME) return;
            if(!((Rope)getAbstractGame()).getDuelManager().getDuelClickTask().hasCurrentDuel()) return;

            Duel currentDuel = ((Rope) getAbstractGame()).getDuelManager().getDuelClickTask().getCurrentDuel();
            if (currentDuel.getDuelStatus() != Duel.DuelStatus.PLAYING) return;
            time--;
            getAbstractGame().getSquidGameManager().getSquidGamePlugin().getSidebar().update();


            if(time == 0){
                time = 120;
                if(currentDuel.getTeam1().getPoints() > currentDuel.getTeam2().getPoints())
                    currentDuel.stop(currentDuel.getTeam1(), currentDuel.getTeam2());
                else
                    currentDuel.stop(currentDuel.getTeam2(), currentDuel.getTeam1());
            }
            return;
        }

        time--;
        getAbstractGame().getSquidGameManager().getSquidGamePlugin().getSidebar().update();

        if(time == 0){
            cancelTask();
            List<ParticipantPlayer> participantPlayerList = new ArrayList<>(getAbstractGame().getSquidGameManager().getParticipantManager().getPlayingPlayers());
            for(ParticipantPlayer participantPlayer :participantPlayerList){
                if(!getAbstractGame().getQualifiedParticipantPlayer().contains(participantPlayer))continue;
                getAbstractGame().onEliminate(participantPlayer);
            }
            abstractGame.onStop();
        }


    }

    public int getTime() {return time;}
    public void setTime(int time) {this.time = time;}
    public AbstractGame getAbstractGame() {return abstractGame;}
}
