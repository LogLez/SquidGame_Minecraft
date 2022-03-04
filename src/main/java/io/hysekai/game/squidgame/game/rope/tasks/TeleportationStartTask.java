package io.hysekai.game.squidgame.game.rope.tasks;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportationStartTask extends HysekaiRunTaskTimer {

    private final Rope rope;
    private int current;

    public TeleportationStartTask(Rope rope){
        super(rope.getSquidGameManager(),50,0);
        this.rope = rope;

    }

    @Override
    public void run() {
        getRope().getTeams().forEach(abstractTeam -> {


            if(abstractTeam.getPlayers().size() > current){
                abstractTeam.getPlayers().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                    HAPI.getUtils().sendAboveHotbarMessage(player, "§7Téléportation §b(" + (current + 1) + "/" + getRope().getMaxPlayersPerTeam() +")");
                }));

                ParticipantPlayer participantPlayer = abstractTeam.getPlayers().get(current);
                participantPlayer.sendActions(player -> {
                    HAPI.getUtils().teleportAsync(player, ((Team)abstractTeam).getFirst().add(Team.behind.clone().multiply(2*current)), PlayerTeleportEvent.TeleportCause.PLUGIN);
                });
            }

        });
        current++;

        if(current > getRope().getMaxPlayersPerTeam()) {
            cancel();
            getRope().getDuelManager().createDuels();
        }
    }

    public Rope getRope() {return rope;}
    public int getCurrent() {return current;}
}
