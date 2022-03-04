package io.hysekai.game.squidgame.commands;

import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.TeamManager;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;
import io.hysekai.game.squidgame.game.rope.team.Invitation;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.hysekai.bukkit.api.HAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestCommand implements CommandExecutor {

    private final SquidGameManager squidGameManager;
    public TestCommand(SquidGameManager squidGameManager){
        this.squidGameManager = squidGameManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

        if(!(sender instanceof  Player)) return false;
        Player player = (Player) sender;

        AbstractGame abstractGame = squidGameManager.getMiniGamesManager().getCurrentGame();
        ParticipantPlayer participantPlayer = squidGameManager.getParticipantManager().getParticipant(player);
        if(participantPlayer == null )
            return false;

//        if( !(abstractGame instanceof Rope) &&  !(abstractGame instanceof Bille)) return false;
        if(!(abstractGame instanceof TeamManager)) return false;
        TeamManager teamManager = (TeamManager) abstractGame;
        if(!teamManager.isTimeTeam()) return false;


        if(args.length != 1 && args.length != 2) {
            player.sendMessage("§7Veuillez effectuer §e/team create");
            return false;
        }

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("list")){
                Optional<AbstractTeam> optionalTeam = teamManager.getTeam(participantPlayer);
                if(optionalTeam.isPresent()){
                    player.sendMessage("Voici les membre de votre équipe (Equipe " + optionalTeam.get().getId() + ")\n");
                    for(ParticipantPlayer participantPlayer1 : optionalTeam.get().getPlayers()){
                        participantPlayer1.sendActions(player1 -> player.sendMessage("- " + player1.getName()));
                    }

                }else{

                }
            }
            if(args[0].equalsIgnoreCase("create")){
                if(teamManager.hasTeam(participantPlayer)){
                    player.sendMessage("§cVeuillez quitter votre équipe avant de pouvoir en crée une ! §e/squid team leave");
                    return false;
                }
                if(teamManager.getMaxTeams() == teamManager.getTeams().size()){
                    player.sendMessage("Le nombre d'équipe maximum a été atteint ! ");

                    return false;
                }

                teamManager.addParticipantToTeam(participantPlayer);
                player.sendMessage("§aVous avez crée votre équipe !");

                return false;
            }

            if(args[0].equalsIgnoreCase("leave")){
                if(!teamManager.hasTeam(participantPlayer)){
                    player.sendMessage("§cVous n'avez pas d'équipe pour le moment !");
                    return false;
                }

                teamManager.removeParticipantToTeam(participantPlayer);
                player.sendMessage("Vous avez quitté votre équipe");
                return false;
            }
            return false;
        }


        if(!teamManager.getInvitations().containsKey(participantPlayer)) return false;
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

        return false;
    }
}
