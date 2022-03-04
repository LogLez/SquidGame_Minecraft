package io.hysekai.game.squidgame.game.bille;

import io.hysekai.game.squidgame.game.bille.teams.BilleTeam;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BilleListener implements Listener {


    protected final Bille bille;
    public BilleListener(Bille bille) { this.bille = bille; }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event){
        event.setCancelled(true);

        if(!bille.isTimeTeam() || !(event.getRightClicked() instanceof Player)) return;
        ParticipantPlayer participantPlayer = bille.getSquidGameManager().getParticipantManager().getParticipant(event.getPlayer());
        ParticipantPlayer targetParticipantPlayer = bille.getSquidGameManager().getParticipantManager().getParticipant((Player) event.getRightClicked());

        if(participantPlayer == null || targetParticipantPlayer == null ||
                !participantPlayer.isPlaying() || !targetParticipantPlayer.isPlaying()) return;

        if(!bille.hasTeam(participantPlayer)) return;
        if(bille.hasTeam(targetParticipantPlayer)){
            participantPlayer.getPlayer().sendMessage("§cCe joueur a déjà une équipe !");
            return;
        }


        bille.getTeam(participantPlayer).ifPresent(team -> {

            if(!bille.canInvitePlayer(targetParticipantPlayer, bille.getLastTeam(participantPlayer))){
                participantPlayer.getPlayer().sendMessage("§cVous ne pouvez pas inviter ce joueur car il ne faisait pas partie de votre équipe !");
                return;
            }

            if(bille.isTeamFull(team)) {
                participantPlayer.sendActions(player -> player.sendMessage("§cVotre équipe est pleine !"));
                return;
            }
            bille.sendJoinRequest(participantPlayer, targetParticipantPlayer, team);

        });
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
	ParticipantPlayer participantPlayer = bille.getSquidGameManager().getParticipantManager().getParticipant(e.getPlayer());
	if (participantPlayer != null)
	    ((BilleTeam) bille.getTeam(participantPlayer).get()).setQualified(true);
    }
    
}
