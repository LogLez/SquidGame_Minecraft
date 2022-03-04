package io.hysekai.game.squidgame.game.rope.listeners;

import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.rope.data.PlayerRopeGame;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.game.rope.team.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class RopeListener implements Listener {

    protected final Rope rope;
    public RopeListener(Rope rope) { this.rope = rope; }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event){
        event.setCancelled(true);

        if(!rope.isTimeTeam() || !(event.getRightClicked() instanceof Player)) return;
        ParticipantPlayer participantPlayer = rope.getSquidGameManager().getParticipantManager().getParticipant(event.getPlayer());
        ParticipantPlayer targetParticipantPlayer = rope.getSquidGameManager().getParticipantManager().getParticipant((Player) event.getRightClicked());

        if(participantPlayer == null || targetParticipantPlayer == null ||
                !participantPlayer.isPlaying() || !targetParticipantPlayer.isPlaying()) return;

        if(!rope.hasTeam(participantPlayer)) return;
        if(rope.hasTeam(targetParticipantPlayer)){
            participantPlayer.getPlayer().sendMessage("§cCe joueur a déjà une équipe !");
            return;
        }


        rope.getTeam(participantPlayer).ifPresent(team -> {
            if(rope.isTeamFull(team)) {
                participantPlayer.sendActions(player -> player.sendMessage("§cVotre équipe est pleine !"));
                return;
            }
            rope.sendJoinRequest(participantPlayer, targetParticipantPlayer, team);

        });
    }
    @EventHandler
    public void onMove(EntityDamageEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        ParticipantPlayer participantPlayer = rope.getSquidGameManager().getParticipantManager().getParticipant(event.getPlayer());

        if (participantPlayer == null || !participantPlayer.isPlaying() || rope.getGameStatus() != GameStatus.GAME ) return;

        if(getRope().getQualifiedParticipantPlayer().contains(participantPlayer)) return;
        if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())
            event.setCancelled(true);

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ParticipantPlayer participantPlayer = rope.getSquidGameManager().getParticipantManager().getParticipant(event.getPlayer());

        if (participantPlayer == null || !participantPlayer.isPlaying() || rope.getDuelManager().getDuelClickTask().getCurrentDuel() == null ) return;
        if(!rope.getDuelManager().getDuelClickTask().getCurrentDuel().getParticipantPlayersPlaying().contains(participantPlayer)) return;

        PlayerRopeGame playerRopeGame = rope.getDuelManager().getTasks().get(participantPlayer);
        if(playerRopeGame == null) return;

        Optional<AbstractTeam> optionalTeam = rope.getTeam(participantPlayer);
        optionalTeam.ifPresent(team -> {

            Team ropeTeam = (Team) team;
            if(playerRopeGame.isSafe())
                playerRopeGame.correct(ropeTeam);
            else
                playerRopeGame.wrong(ropeTeam);

        });

    }

    public Rope getRope() {return rope;}
}
