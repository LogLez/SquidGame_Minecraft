package io.hysekai.game.squidgame.game.bille;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameType;
import io.hysekai.game.squidgame.game.TeamManager;
import io.hysekai.game.squidgame.game.bille.task.BilleTask;
import io.hysekai.game.squidgame.game.bille.teams.BilleTeam;
import io.hysekai.game.squidgame.game.rope.team.Invitation;
import io.hysekai.game.squidgame.manager.SquidLocations;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.scoreboard.games.BilleAddon;
import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.utils.SquidGameUtil;
import net.md_5.bungee.api.chat.BaseComponent;

public class Bille extends AbstractGame implements TeamManager {

    private final List<ParticipantPlayer> qualifiedParticipantPlayer = new ArrayList<>();


    private final BilleListener billeListener;
    private final BilleTask billeTask;
    private BilleAddon billeAddon;

    /*
    Variables for Team system
     */
    private final List<AbstractTeam> teams = new ArrayList<>();
    private List<AbstractTeam> lastTeams = new ArrayList<>();
    private final Map<ParticipantPlayer, List<Invitation>> invitations = new HashMap<>();
    private int maxTeams, maxPlayersPerTeam;
    private boolean timeTeam = true;

    public Bille(SquidGameManager squidGameManager) {
        super(squidGameManager, GameType.BILLE,
                "§7§o[§6§oSquidGame§o§7] §7Bienvenue sur le jeu du :§e " + GameType.BILLE.getName(),
                "§7§o[§6§oSquidGame§o§7] §7Le but du jeu est de récupérer les billes de votre adversaire",
                "§7§o[§6§oSquidGame§o§7] Si vous n'avez plus de billes, vous risquez de... mourir",
                "§7§o[§6§oSquidGame§o§7] §7Bonne chance !"
        );
        this.billeListener = new BilleListener(this);
        this.billeTask = new BilleTask(this);
        this.billeAddon = new BilleAddon(this);
        this.setSpawnLocation(SquidLocations.WHITE_ROOM.getLocation());

        //this.setSpawnLocation(WorldsManager.SquidGameWorld.BILLE.getWorld(), -1014.5, 11, -144.5,(float) -1.9,(float)0.2));

    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.billeTask.setMessageUtils(getSquidGameManager().getMessageUtils());
        loadInfos();
        getSquidGameManager().getSquidGamePlugin().getSidebar().registerAddon(billeAddon);
        Bukkit.getPluginManager().registerEvents(billeListener,getSquidGameManager().getSquidGamePlugin());
    }

    @Override
    public void onEliminate(ParticipantPlayer participantPlayer) {
        super.onEliminate(participantPlayer);
        Optional<AbstractTeam> billeTeam = getTeam(participantPlayer);

        if (billeTeam.isPresent()) {
            ((BilleTeam) billeTeam.get()).setQualified(true);
            if (checkWin())
        	onStop();
        }
    }

    public boolean checkWin() {
        for(AbstractTeam abstractTeam : getTeams())
            if(!((BilleTeam) abstractTeam).isQualified())
        	return false;
        return true;
    }

    @Override
    public void onPreTime(int time) {
        if(time == 5){
            if(getTeams().size() < getMaxTeams()){
                int count  = getMaxTeams() - getTeams().size();
                for(int i = 0 ; i < count ;i++){
                    new BilleTeam(this);
                }
            }

            timeTeam = (false);
            List<ParticipantPlayer> participantPlayerList = getSquidGameManager().getParticipantManager().getPlayingPlayers().stream().filter(participantPlayer -> !hasTeam(participantPlayer)).collect(Collectors.toList());
            participantPlayerList.forEach(this::setRandomTeamToPlayer);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        billeTask.start();
        
        for(AbstractTeam team : getTeams()){
            Bukkit.broadcastMessage(getTeams().size() + " §eComposition de l'equipe de bille :\n" );
            StringBuilder st = new StringBuilder();
            team.getPlayers().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                st.append(" §7- ").append(player.getName()).append("\n");
                HAPI.getUtils().teleportPlayer(player, SquidLocations.START_BILLE.getLocation());
            }));
            st.append("\n");
            Bukkit.broadcastMessage(st.toString());

            BilleTeam billeTeam = (BilleTeam) team;
            billeTeam.getPlayerOne().setData(this, 10);
            billeTeam.getPlayerTwo().setData(this, 10);
        }
    }

    @Override
    public void onPlayerQualified(ParticipantPlayer participantPlayer) {
        super.onPlayerQualified(participantPlayer);
    }

    @Override
    public void onStop() {
        HandlerList.unregisterAll(billeListener);

        squidGameManager.getSquidGamePlugin().getSidebar().unregisterAddon(billeAddon);
        super.onStop();
    }

    public List<ParticipantPlayer> getQualifiedParticipantPlayer() {return qualifiedParticipantPlayer;}


    /**
     *
     *  Method TeamInterface
     *
    */

    public void setLastTeams(List<AbstractTeam> lastTeams) {
        this.lastTeams = lastTeams;
     }

    public boolean canInvitePlayer(ParticipantPlayer participantPlayer, Optional<AbstractTeam> abstractTeam) {
        return abstractTeam.isPresent() && abstractTeam.get().getPlayers().contains(participantPlayer);
    }

    public Optional<AbstractTeam> getLastTeam(ParticipantPlayer participantPlayer) {
        return lastTeams.stream().filter(abstractTeam -> abstractTeam.getPlayers().contains(participantPlayer)).findFirst();
    }
    @Override
    public boolean addParticipantToTeam(ParticipantPlayer participantPlayer) {
        if(getTeam(participantPlayer).isPresent() || getTeams().size() >= getMaxTeams()) return false;

        BilleTeam billeTeam = new BilleTeam(this,participantPlayer);
        billeTeam.setPlayer(participantPlayer);
        billeTeam.getPlayers().add(participantPlayer);
        return true;
    }

    @Override
    public boolean addParticipantToTeam(ParticipantPlayer participantPlayer, AbstractTeam team) {
        BilleTeam billeTeam = (BilleTeam) team;

        billeTeam.getPlayers().add(participantPlayer);
        billeTeam.setPlayer(participantPlayer);

        return true;
    }

    @Override
    public void removeParticipantToTeam(ParticipantPlayer participantPlayer) {
        Optional<AbstractTeam> optionalTeam = getTeams().stream().filter(team -> team.getPlayers().contains(participantPlayer)).findAny();
        optionalTeam.ifPresent(team -> {
            team.getPlayers().remove(participantPlayer);
            team.getPlayers().stream().filter(participantPlayer1 -> !participantPlayer1.equals(participantPlayer)).forEach(participantPlayer1 -> participantPlayer1.sendActions(player -> player.sendMessage("§b"+participantPlayer.getPlayer().getName() + "§c a quitté l'équipe !")));
            if(team.getPlayers().size() == 0) getTeams().remove(team);
        });

        getTeams().forEach(abstractTeam -> abstractTeam.getPlayers().forEach(participantPlayer1 -> {
            if(participantPlayer.equals(participantPlayer1)) abstractTeam.getPlayers().remove(participantPlayer1);
        }));
    }

    @Override
    public Optional<AbstractTeam> getTeamById(int id) {
        return getTeams().stream().filter(team -> team.getId() == id).findFirst();
    }

    @Override
    public void loadInfos() {
        int count = getSquidGameManager().getParticipantManager().getPlayingPlayers().size();
        maxTeams = ((int) count/2);
        maxPlayersPerTeam = 2;
    }


    @Override
    public boolean hasTeam(ParticipantPlayer participantPlayer) {
        Optional<AbstractTeam> optionalTeam = getTeams().stream().filter(team -> team.getPlayers().contains(participantPlayer)).findAny();
        return optionalTeam.isPresent();
    }

    @Override
    public Optional<AbstractTeam> getTeam(ParticipantPlayer participantPlayer) {
        return getTeams().stream().filter(team -> team.getPlayers().contains(participantPlayer)).findAny();
    }

    @Override
    public boolean isTeamFull(AbstractTeam team) {
        return team.getPlayers().size() == getMaxPlayersPerTeam();
    }

    @Override
    public boolean isTimeTeam() {
        return timeTeam;
    }

    @Override
    public List<AbstractTeam> getTeams() {
        return teams;
    }

    @Override
    public int getMaxPlayersPerTeam() {return maxPlayersPerTeam;}

    @Override
    public int getMaxTeams() {return maxTeams;}

    @Override
    public void setRandomTeamToPlayer(ParticipantPlayer participantPlayer) {
        if(hasTeam(participantPlayer)) return;

        for(AbstractTeam team : getTeams()){
            if(isTeamFull(team)) continue;
            if(addParticipantToTeam(participantPlayer, team)) return;
        }

        getQualifiedParticipantPlayer().add(participantPlayer);
      //  getSquidGameManager().getWorldsManager().teleport(SquidLocation.DORTOIR, participantPlayer);
        participantPlayer.sendActions(player -> player.sendMessage("§aVous êtes qualifié car vous n'avez pas d'équipe"));

    }

    @Override
    public Map<ParticipantPlayer, List<Invitation>> getInvitations() {
        return invitations;
    }

    @Override
    public boolean hasInvitationOfTeam(ParticipantPlayer participantPlayer, int id) {
        Optional<AbstractTeam> optionalTeam = getTeamById(id);
        return optionalTeam.isPresent() && hasInvitationOfTeam(participantPlayer, optionalTeam.get());
    }

    @Override
    public boolean hasInvitationOfTeam(ParticipantPlayer participantPlayer, AbstractTeam team) {
        if(!getInvitations().containsKey(participantPlayer)) return false;
        return getInvitations().get(participantPlayer).stream().anyMatch(invitation -> invitation.getTeam().equals(team));
    }

    @Override
    public Optional<Invitation> getInvitationOfTeam(ParticipantPlayer participantPlayer, int id) {
        return getInvitations().get(participantPlayer).stream().filter(invitation -> invitation.getTeam().getId() == id).findFirst();
    }

    @Override
    public boolean sendJoinRequest(ParticipantPlayer participantPlayer, ParticipantPlayer targetParticipantPlayer, AbstractTeam team) {
        if (hasInvitationOfTeam(targetParticipantPlayer, team)) {
            participantPlayer.getPlayer().sendMessage("§cCe joueur a déjà une invitation de cette équipe !");
            return false;
        }

        Optional<AbstractTeam> lastTeam = getLastTeam(participantPlayer);
        if(lastTeam.isEmpty() || !lastTeam.get().getPlayers().contains(targetParticipantPlayer)){
            participantPlayer.sendActions(player -> player.sendMessage("§cCe joueur ne faisait pas partie de votre équipe !"));
            return false;
        }

        if(!getInvitations().containsKey(targetParticipantPlayer))
            getInvitations().put(targetParticipantPlayer, Collections.singletonList(new Invitation(team, participantPlayer)));
        else
            getInvitations().get(targetParticipantPlayer).add(new Invitation(team, participantPlayer));


        participantPlayer.getPlayer().sendMessage("§7Vous avez envoyé une demande d'invitation au joueur : §b" + targetParticipantPlayer.getPlayer().getName() + "\n");
        targetParticipantPlayer.sendActions(player ->
        {

            player.sendMessage("§e§o"+participantPlayer.getPlayer().getName() + " §7vous a invité à rejoindre son équipe ! Souhaitez vous la rejoindre ? \n");
            player.sendMessage("§7\n");
            BaseComponent baseComponent = SquidGameUtil.getClickableTextComponent("§7      §7[§aAcceptez§7]", "/team accept " + team.getId(), "§7Cliquez pour acceptez l'invitation.");
            baseComponent.addExtra("§7                §7");
            baseComponent.addExtra(SquidGameUtil.getClickableTextComponent("§7[§cDecliner§7]", "/team decline " + team.getId(), "§7Cliquez pour refuser l'invitation."));
            baseComponent.addExtra("\n");

            player.spigot().sendMessage(baseComponent);
        });

        return true;
    }

}
