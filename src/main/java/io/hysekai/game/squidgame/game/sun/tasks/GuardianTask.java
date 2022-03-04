package io.hysekai.game.squidgame.game.sun.tasks;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.sun.guardian.Guardian;
import org.bukkit.Sound;

public class GuardianTask extends HysekaiRunTaskTimer {

    private final Guardian guardian;
    private int period;
    private int timer, count = -2;

    public GuardianTask(Guardian guardian, int period){
        super(guardian.getSun().getSquidGameManager(),1,0);
        this.guardian = guardian;
        this.period = period;
    }

    @Override
    public void run() {

        if(getGuardian().getSun().getGameStatus() == GameStatus.FINISH){
            cancel();
            return;
        }

        if(timer == 0){

            //TODO Rotate petite fille
            getGuardian().getSun().setCanMove(true);
            getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                    .stream()
                    .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer))
                    .forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        HAPI.getUtils().sendAboveHotbarMessage(player, "§2§o§lCourez !");
                        HAPI.getUtils().sendTitle(player, 1, 1, 1, "", "");
                        player.playSound(player.getLocation(), Sound.BLAZE_DEATH,5,5);
                    }));
            /*Bukkit.getOnlinePlayers()
                    .forEach(player -> {
                        Title.sendActionBar(player, "§2§o§lCourez !");
                        Title.sendTitle(player, 1, 1, 1, "", "");
                        player.playSound(player.getLocation(), Sound.BLAZE_DEATH,5,5);
                    });*/

        }

        if(this.timer > 0 && this.timer % period == 0){
            count++;
            if(count == -1)  getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                    .stream()
                    .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer))
                    .forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), getGuardian().getSpeed().getSound(),5,5);
                    }));

            if (count == 1)
                getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                        .stream()
                        .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer))
                        .forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                            HAPI.getUtils().sendTitle(player, 1, period, 1, "§e1", "");
                        }));
            if (count == 2)
                getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                        .stream()
                        .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer)).forEach(participantPlayer -> participantPlayer.sendActions(player -> HAPI.getUtils().sendTitle(player, 1, period, 1, "§e2", "")));
            if (count == 3)
                getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                        .stream()
                        .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer)).forEach(participantPlayer -> participantPlayer.sendActions(player -> HAPI.getUtils().sendTitle(player, 1, period, 1, "§e3", "")));

            if (count == 4){
                getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                        .stream()
                        .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer)).forEach(participantPlayer -> participantPlayer.sendActions(player -> HAPI.getUtils().sendTitle(player, 1, period*5, 1, "§eSoleil" + "", "§cNe bougez Plus")));


                getGuardian().getSun().setCanMove(false);
   /*             Bukkit.getOnlinePlayers()
                        .forEach(player -> {
                            Title.sendActionBar(player, "§4§o§lAttention !");
                            player.playSound(player.getLocation(), Sound.BLAZE_DEATH,5,5);
                        });*/

                getGuardian().getSun().getSquidGameManager().getParticipantManager().getPlayingPlayers()
                        .stream()
                        .filter(participantPlayer -> !getGuardian().getSun().getQualifiedParticipantPlayer().contains(participantPlayer))
                        .forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                            HAPI.getUtils().sendAboveHotbarMessage(player, "§4§o§lAttention !");
                            player.playSound(player.getLocation(), Sound.BLAZE_DEATH,5,5);
                        }));

                //TODO Rotate petite fille
                if(getGuardian().getSpeed() != Guardian.Speed.FAST) {
                    getGuardian().setSpeed( getGuardian().getSpeed().upgrade());
                    getGuardian().getGuardianTask().setPeriod(getGuardian().getSpeed().getPeriod());
                }

                timer = -period*5;
                count = -2;
            }

        }

        timer++;
    }



    public Guardian getGuardian() {return guardian;}

    public int getCount() {return count;}
    public int getPeriod() {return period;}
    public void setPeriod(int period) {this.period = period;}
}
