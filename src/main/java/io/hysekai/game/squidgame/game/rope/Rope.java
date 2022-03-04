package io.hysekai.game.squidgame.game.rope;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.player.HPlayer;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.rope.tasks.TeleportationStartTask;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameType;
import io.hysekai.game.squidgame.game.TeamManager;
import io.hysekai.game.squidgame.game.bille.Bille;
import io.hysekai.game.squidgame.game.rope.listeners.RopeListener;
import io.hysekai.game.squidgame.game.rope.team.Invitation;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.manager.rope.DuelManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.scoreboard.games.RopeGameAddon;
import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.utils.SquidGameUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import java.util.*;
import java.util.stream.Collectors;

public class Rope extends AbstractGame implements TeamManager {

    private final RopeListener ropeListener;
    private final List<ParticipantPlayer> qualifiedParticipantPlayer = new ArrayList<>();

    /*
    Variables for Duel system
    */
    private final DuelManager duelManager;

    /*
    Variables for Team system
     */
    private final List<AbstractTeam> teams = new ArrayList<>();
    private final Map<ParticipantPlayer, List<Invitation>> invitations = new HashMap<>();
    private int maxTeams, maxPlayersPerTeam;
    private boolean timeTeam = true;

    private final RopeGameAddon ropeGameAddon;


    public Rope(SquidGameManager squidGameManager) {

        super(squidGameManager, GameType.ROPE,
                "§7§o[§6§oSquidGame§o§7] §7Bienvenue sur le jeu du :§e " + GameType.ROPE.getName(),
                "§7§o[§6§oSquidGame§o§7] §7Ce jeu oppose deux équipes au jeu du tir à la corde traditionnel",
                "§7§o[§6§oSquidGame§o§7] §7A chaque tour, il vous faudra cliquer au bon moment pour faire gagner des points à votre équipe",
                "§7§o[§6§oSquidGame§o§7] §7Si vous cliquez au moment moment, vous aure 1 point en moins.",
                "§7§o[§6§oSquidGame§o§7] §7A la fin du duel, l'equipe possédant le plus de points sera gagnante",
                "§7§o[§6§oSquidGame§o§7] §7Sur ce, bonne chance !");
        duelManager = new DuelManager(this);
        ropeListener = new RopeListener(this);

        ropeGameAddon = new RopeGameAddon(this);

       // setSpawnLocation(SquidLocation.WHITE_ROOM.getLocation());

        //getTimeLocation()[0] = new Location(WorldsManager.SquidGameWorld.ROPE.getWorld(), -1367, 75, -278,180,0);
        ///getTimeLocation()[1] = new Location(WorldsManager.SquidGameWorld.ROPE.getWorld(), -1371, 75, -278, 180,0);
       //// getTimeLocation()[3] = new Location(WorldsManager.SquidGameWorld.ROPE.getWorld(), -1377, 75, -278, 180,0);
        //getTimeLocation()[4] = new Location(WorldsManager.SquidGameWorld.ROPE.getWorld(), -1381, 75, -278, 180,0);
    }


    @Override
    public void onLoad() {
        super.onLoad();

        sidebar.registerAddon(ropeGameAddon);

        loadInfos();
        Bukkit.getPluginManager().registerEvents(ropeListener, squidGameManager.getSquidGamePlugin());

        getSquidGameManager().getParticipantManager().getPlayingPlayers().forEach(participantPlayer -> {

            participantPlayer.sendActions(player -> {
                player.sendMessage( "§8-----------------------------");
                player.sendMessage( "§7Pour créer une équipe : §a§l/team create");
                player.sendMessage( "§7Pour quitter ou dissoudre votre équipe : §c§l/team leave");
                player.sendMessage( "§7Pour voir les membres de votre équipe : §e§l/team list");
                player.sendMessage( "§8-----------------------------");
            });


        });

    }

    @Override
    public void onEliminate(ParticipantPlayer participantPlayer) {
        super.onEliminate(participantPlayer);
        Optional<AbstractTeam> team = getTeam(participantPlayer);
        HPlayer hPlayer = HAPI.getPlayerManager().getHPlayer(participantPlayer.getUuid());
        if(team.isEmpty()) return;
        hPlayer.setTabPrefix("");
        hPlayer.setTagSuffix("");
    }

    @Override
    public void onStart() {
        super.onStart();

        Optional<AbstractGame> optionalAbstractGame = getSquidGameManager().getMiniGamesManager().getGame(GameType.BILLE);
        optionalAbstractGame.ifPresent(abstractGame -> ((Bille) abstractGame).setLastTeams(getTeams()));
        //getDuelManager().getDuelClickTask().runTaskTimer(getSquidGameManager().getMain(), 0,1);
        ////getQualifiedParticipantPlayer().forEach(participantPlayer -> participantPlayer.sendActions(player ->
                //getSquidGameManager().getWorldsManager().teleport(SquidLocation.DORTOIR,player)));

        getTeams().forEach(abstractTeam -> Collections.shuffle(abstractTeam.getPlayers()));


        for(ParticipantPlayer participantPlayer : getSquidGameManager().getParticipantManager().getPlayingPlayers()) {
            participantPlayer.sendActions(player -> {
                Optional<AbstractTeam> team = getTeam(participantPlayer);
                HPlayer hPlayer = HAPI.getPlayerManager().getHPlayer(player.getUniqueId());
                if (team.isEmpty()) return;
                hPlayer.setTabPrefix("" +((Team) team.get()).getTeamColor().getChatColor());
                hPlayer.setTagPrefix("" + ((Team) team.get()).getTeamColor().getChatColor());
            });
        }

        new TeleportationStartTask(this).start();

    }

    @Override
    public void onStop() {

        sidebar.unregisterAddon(ropeGameAddon);
        HandlerList.unregisterAll(ropeListener);

        for(ParticipantPlayer participantPlayer : getSquidGameManager().getParticipantManager().getPlayingPlayers()) {
            participantPlayer.sendActions(player -> {
                Optional<AbstractTeam> team = getTeam(participantPlayer);
                HPlayer hPlayer = HAPI.getPlayerManager().getHPlayer(player.getUniqueId());
                if (team.isEmpty()) return;
                hPlayer.setTabPrefix("");
                hPlayer.setTagPrefix("");
            });
        }


        super.onStop();

    }

    @Override
    public void onPreTime(int time) {
        if(time == 5){
            setTimeTeam(false);
            List<ParticipantPlayer> participantPlayerList = getSquidGameManager().getParticipantManager().getPlayingPlayers().stream().filter(participantPlayer -> !hasTeam(participantPlayer)).collect(Collectors.toList());
            Collections.shuffle(participantPlayerList);
            participantPlayerList.forEach(this::setRandomTeamToPlayer);
        }
    }

    //public RopeTeamManager getTeamManager() { return (RopeTeamManager) getSquidGameManager().getTeamManager();}
    public List<ParticipantPlayer> getQualifiedParticipantPlayer() {return qualifiedParticipantPlayer;}
    public RopeListener getRopeListener() {return ropeListener;}
    public void setTimeTeam(boolean timeTeam) {this.timeTeam = timeTeam;}
    public DuelManager getDuelManager() {return duelManager;}

    /**
     *
     *  Method TeamInterface
     *
     */

    @Override
    public boolean addParticipantToTeam(ParticipantPlayer participantPlayer) {
        Team team = new Team(this,participantPlayer);
        return addParticipantToTeam(participantPlayer, team);
    }

    @Override
    public boolean addParticipantToTeam(ParticipantPlayer participantPlayer, AbstractTeam team) {
        if(team.getPlayers().size() == getMaxPlayersPerTeam()) return false;

        removeParticipantToTeam(participantPlayer);
        team.getPlayers().add(participantPlayer);
        HPlayer hPlayer = HAPI.getPlayerManager().getHPlayer(participantPlayer.getUuid());
        if(hPlayer != null)
            return false;


        hPlayer.setTabPrefix("" + ((Team)team).getTeamColor().getColor());
        hPlayer.setTagSuffix("" + ((Team)team).getTeamColor().getColor());
        if(((Team) team).getChief() == null) ((Team) team).setChief(participantPlayer);

        team.getPlayers().stream().filter(participantPlayer1 -> !participantPlayer1.equals(participantPlayer)).forEach(participantPlayer1 -> {
           /* if(getSquidGameManager().getScoreboardManager().getScoreboards().containsKey(participantPlayer1))
                getSquidGameManager().getScoreboardManager().getScoreboards().get(participantPlayer1).updateLine(3,"§8▏ §7Equipe : §9" + team.getId() + "§7(" + team.getPlayers().size()+")");
*/
            participantPlayer1.sendActions(player -> player.sendMessage("§b"+participantPlayer.getPlayer().getName() + "§a a rejoint l'équipe !"));
        });

       /* if(getSquidGameManager().getScoreboardManager().getScoreboards().containsKey(participantPlayer))
            getSquidGameManager().getScoreboardManager().getScoreboards().get(participantPlayer).updateLine(3,"§8▏ §7Equipe : §9" + team.getId() + "§7(" + team.getPlayers().size()+")");
*/
        return true;
    }

    @Override
    public void removeParticipantToTeam(ParticipantPlayer participantPlayer) {
        Optional<AbstractTeam> optionalTeam = getTeams().stream().filter(team -> team.getPlayers().contains(participantPlayer)).findAny();
        optionalTeam.ifPresent(team -> {
            team.getPlayers().remove(participantPlayer);
            team.getPlayers().stream().filter(participantPlayer1 -> !participantPlayer1.equals(participantPlayer)).forEach(participantPlayer1 -> participantPlayer1.sendActions(player -> player.sendMessage("§b"+participantPlayer.getPlayer().getName() + "§c a quitté l'équipe !")));
            if(team.getPlayers().size() == 0)
                getTeams().remove(team);
            else
                if(((Team)team).getChief().equals(participantPlayer)) ((Team)team).setChief(team.getPlayers().get(0));
        });

        getTeams().forEach(abstractTeam -> abstractTeam.getPlayers().forEach(participantPlayer1 -> {
            if(participantPlayer.equals(participantPlayer1)) abstractTeam.getPlayers().remove(participantPlayer1);
        }));
    }

    @Override
    public boolean isTimeTeam() {
        return timeTeam;
    }
    @Override
    public Optional<AbstractTeam> getTeamById(int id) {
        return getTeams().stream().filter(team -> team.getId() == id).findFirst();
    }

    @Override
    public void loadInfos() {
        int count = getSquidGameManager().getParticipantManager().getPlayingPlayers().size();

        /*if((int) (count / 8) % 2 == 0){

            if((int) (count / 6) % 2 == 0){

                if(count % 6 < count % 8){
                    maxPlayersPerTeam = (6);
                    maxTeams = (count / 6);
                }else{
                    maxPlayersPerTeam = (8);
                    maxTeams = (count / 8);
                }
                return;

            }else if((int) (count / 4) % 2 == 0){

                if(count % 8 <= count % 4){
                    maxPlayersPerTeam = (8);
                    maxTeams = (count / 8);
                }else{
                    maxPlayersPerTeam = (4);
                    maxTeams = (count / 4);
                }
                return;

            } else{
                maxPlayersPerTeam = (8);
                maxTeams = (count / 8);
                return;
            }

        }
        if((int) (count / 6) % 2 == 0){

            if((int) (count / 8) % 2 == 0){

                if(count % 6 < count % 8){
                    maxPlayersPerTeam = (6);
                    maxTeams = (count / 6);
                }else{
                    maxPlayersPerTeam = (8);
                    maxTeams = (count / 8);
                }
                return;

            }else if((int) (count / 4) % 2 == 0){

                if(count % 6 < count % 4){
                    maxPlayersPerTeam = (6);
                    maxTeams = (count / 6);
                }else{
                    maxPlayersPerTeam = (4);
                    maxTeams = (count / 4);
                }
                return;
            }else{
                maxPlayersPerTeam = (6);
                maxTeams = (count / 6);
                return;
            }

        }

        if((int) (count / 4) % 2 == 0){

            if((int) (count / 8) % 2 == 0){

                if(count % 4 < count % 8){
                    maxPlayersPerTeam = (4);
                    maxTeams = ((int) (count / 4));
                }else{
                    maxPlayersPerTeam = (8);
                    maxTeams = (count / 8);
                }

            }else if((int) (count / 6) % 2 == 0){

                if(count % 4 < count % 6){
                    maxPlayersPerTeam = (4);
                    maxTeams = (count / 4);
                }else{
                    maxPlayersPerTeam = (6);
                    maxTeams = (count / 6);
                }
            }else{
                maxPlayersPerTeam = (4);
                maxTeams = (count / 4);
            }
        }*/

        maxPlayersPerTeam = 2;
        maxTeams = (count / 2);
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

        if(getTeams().size() < getMaxTeams()){
            int size = getMaxTeams() - getTeams().size();
            if(size % 2 != 0) size--;

            for(int i = 0 ; i < size;i++)
                new Team(this);

        }


        Optional<AbstractTeam> optionalTeam = getTeams().stream().filter(team -> team.getPlayers().size() < getMaxPlayersPerTeam()).findAny();
        if(optionalTeam.isPresent()){
            if(addParticipantToTeam(participantPlayer, optionalTeam.get())) participantPlayer.sendActions(player -> {
                player.sendMessage("§aVous avez aléatoirement rejoint l'équipe " + optionalTeam.get().getId());
            });
        }else{
            getQualifiedParticipantPlayer().add(participantPlayer);
            participantPlayer.sendActions(player -> player.sendMessage("§aVous êtes qualifié car vous n'avez pas d'équipe"));
        }
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
            getSquidGameManager().getMessageUtils().sendMessageToPlayer(participantPlayer, "§cCe joueur a déjà une invitation de cette équipe !");
            return false;
        }

        if(!getInvitations().containsKey(targetParticipantPlayer))
            getInvitations().put(targetParticipantPlayer, Collections.singletonList(new Invitation(team, participantPlayer)));
        else
            getInvitations().get(targetParticipantPlayer).add(new Invitation(team, participantPlayer));


        getSquidGameManager().getMessageUtils().sendMessageToPlayer(participantPlayer, "§7Vous avez envoyé une demande d'invitation au joueur : §b" + targetParticipantPlayer.getPlayer().getName() + "\n");
        targetParticipantPlayer.sendActions(player ->
        {

            getSquidGameManager().getMessageUtils().sendMessageToPlayer(player, "§e§o"+participantPlayer.getPlayer().getName() + " §7vous a invité à rejoindre son équipe ! Souhaitez vous la rejoindre ? \n");
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
