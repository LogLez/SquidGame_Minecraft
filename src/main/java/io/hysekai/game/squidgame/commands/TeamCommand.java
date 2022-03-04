package io.hysekai.game.squidgame.commands;

import io.hysekai.game.squidgame.game.TeamManager;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.rope.team.Invitation;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamCommand extends CommandArgument{

    private final SquidGameManager squidGameManager;

    public TeamCommand(SquidGameManager squidGameManager) {
        super("team");

        this.squidGameManager = squidGameManager;
    }

    @Override
    public void run(Player player, String[] args) {

        AbstractGame abstractGame = squidGameManager.getMiniGamesManager().getCurrentGame();
        ParticipantPlayer participantPlayer = squidGameManager.getParticipantManager().getParticipant(player);
        if(participantPlayer == null )
            return;


        //if( !(abstractGame instanceof Rope) &&  !(abstractGame instanceof Bille)) return;
        if(!(abstractGame instanceof TeamManager)) return;
        final TeamManager teamManager = (TeamManager) abstractGame;
        if(!teamManager.isTimeTeam()) return;


        if(args.length != 1 && args.length != 2) {
            player.sendMessage("§7Veuillez effectuer §e/team create");
            return;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("create")){
                if(teamManager.hasTeam(participantPlayer)){
                    player.sendMessage("§cVeuillez quitter votre équipe avant de pouvoir en crée une ! §e/squid team leave");
                    return;
                }
                if(teamManager.getMaxTeams() == teamManager.getTeams().size()){
                    player.sendMessage("Le nombre d'équipe maximum a été atteint ! ");

                    return;
                }

                teamManager.addParticipantToTeam(participantPlayer);
                player.sendMessage("§aVous avez crée votre équipe !");

                return;
            }

            if(args[0].equalsIgnoreCase("leave")){
                if(!teamManager.hasTeam(participantPlayer)){
                    player.sendMessage("§cVous n'avez pas d'équipe pour le moment !");
                    return;
                }

                teamManager.removeParticipantToTeam(participantPlayer);
                player.sendMessage("Vous avez quitté votre équipe");
                return;
            }
            return;
        }


        if(!teamManager.getInvitations().containsKey(participantPlayer)) return ;
        List<Invitation> invitations = new ArrayList<>(teamManager.getInvitations().get(participantPlayer));

        int id = Integer.parseInt(args[1]);
        teamManager.getTeamById(id).ifPresent(team -> {

            Optional<Invitation> optionalInvitation = invitations.stream().filter(invitation1 -> invitation1.getTeam().equals(team)).findAny();

            optionalInvitation.ifPresent(invitation -> {
                if(args[0].equalsIgnoreCase("accept")){
                    teamManager.addParticipantToTeam(participantPlayer, invitation.getTeam());
                    invitations.remove(invitation);
                    teamManager.getInvitations().replace(participantPlayer, invitations);
                    player.sendMessage("§aVous avez accepté l'invitation !");
                    return;
                }

                if(args[0].equalsIgnoreCase("decline")){
                    invitations.remove(invitation);
                    teamManager.getInvitations().replace(participantPlayer, invitations);
                    player.sendMessage("§cVous avez décliné l'invitation !");
                }
                return;

            });
            if(!optionalInvitation.isPresent()) player.sendMessage("§cCette invitation n'est plus valable !");
        });
    }

    public SquidGameManager getSquidGameManager() {return squidGameManager;}
}
