package io.hysekai.game.squidgame.game.rope.data;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Sound;

import java.util.Random;

public class PlayerRopeGame {

    private final Rope rope;
    private final ParticipantPlayer participantPlayer;

    private int time;
    private int  current = 1, sizeSafeZone = 9, secondTurn = 0;
    private final int[] safeZone = new int[2];

    private boolean safe = false, clicked = false, increase = true;

    public PlayerRopeGame( Rope rope, ParticipantPlayer participantPlayer){
        this.rope = rope;
        this.participantPlayer = participantPlayer;
        randomSafeZone();
    }

    private void randomSafeZone(){

        Random r = new Random();

        getSafeZone()[0] = r.nextInt(31 - getSizeSafeZone());
        getSafeZone()[1] = getSafeZone()[0] + getSizeSafeZone();

        if(getSizeSafeZone() <= 6){
            secondTurn++;
            if(secondTurn == 2){
                setSafeZone(getSizeSafeZone() - 1);
                secondTurn = 0;
            }
        }else
            setSafeZone(getSizeSafeZone() - 1);

        if(getSizeSafeZone() <= 1) setSafeZone(1);

    }

    public int getCurrent() {return current;}
    public void incremente(){
        if(this.increase){
            current++;
            if(current == 30) this.increase = false;
        } else{
            current--;
            if(current <= 0){
                current = 0;
                this.increase = true;
            }
        }

    }

    public void correct(Team team){
        setSafe(false);

        randomSafeZone();
        current = 0;
        time = 0;
        getParticipantPlayer().sendActions(player -> {
            player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
            HAPI.getUtils().sendTitle(player,1,20,1,"§a+1", "");
            team.addPoint(1);

        });
    }

    public void wrong(Team team){
        setSafe(false);

        current = 0;
        time = 0;
        getParticipantPlayer().sendActions(player -> {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO,1,1);
            HAPI.getUtils().sendTitle(player,1,20,1,"§c-1", "");
            team.removePoint(1);
        });
    }


    public int getSizeSafeZone() {return sizeSafeZone;}
    public void setSafeZone(int zone) {this.sizeSafeZone = zone;}
    public int[] getSafeZone() {return safeZone;}

    public void changeDirection() {this.increase = !this.increase;}
    public boolean isIncrease() {return increase;}

    public boolean isSafe() {return safe;}
    public void setSafe(boolean safe) {this.safe = safe;}

    public boolean hasClicked() {return clicked;}
    public void setClicked(boolean clicked) {this.clicked = clicked;}

    public Rope getRope() {return rope;}
    public ParticipantPlayer getParticipantPlayer() {return participantPlayer;}
}
