package io.hysekai.game.squidgame.game.rope.duel;


import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.player.HPlayer;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.rope.data.PlayerRopeGame;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.game.rope.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Duel implements DuelInterface {



    private final Rope rope;
    private final Team team1, team2;
    private DuelStatus duelStatus = DuelStatus.WAITING;


    public Duel(Rope rope, Team team1, Team team2){
        this.rope = rope;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Rope getRope() {return rope;}

    public Team getTeam1() {return team1;}
    public Team getTeam2() {return team2;}
    public List<Team> getTeams(){return Arrays.asList(getTeam1(), getTeam2());}

    public boolean isIn(ParticipantPlayer participantPlayer){
        return getTeam1().getPlayers().contains(participantPlayer) || getTeam2().getPlayers().contains(participantPlayer);
    }
    public boolean isDuelStatus(DuelStatus duelStatus){ return this.duelStatus == duelStatus; }
    public DuelStatus getDuelStatus() {return duelStatus;}
    public void setDuelStatus(DuelStatus duelStatus) {this.duelStatus = duelStatus;}

    public int getDifference(){
        return Math.abs(getTeam1().getPoints() - getTeam2().getPoints());
    }
    public List<ParticipantPlayer> getParticipantPlayersPlaying(){
        List<ParticipantPlayer> participantPlayers = new ArrayList<>(getTeam1().getPlayers());
        participantPlayers.addAll(getTeam2().getPlayers());
        return participantPlayers;
    }

    @Override
    public void preparation() {
        getRope().getDuelManager().updateHologramDuel(getTeam1(), getTeam2());
        getRope().getDuelManager().getHologramTeam().setHologramTeam(getTeam1(), getTeam2());

        getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> player.sendMessage("§7C'est au tour de votre équipe ! Téléportation dans quelques instants...")));

        new HysekaiRunTaskTimer(getRope().getSquidGameManager(),20,0) {
            int timer = 10;
            int count = 0;
            @Override
            public void run() {
                if(timer != 0)timer--;

                if(timer == 10)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7secondes");
                    }));
                if(timer == 5)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7secondes");
                    }));
                if(timer == 4)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7secondes");
                    }));
                if(timer == 3)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7secondes");
                    }));
                if(timer == 2)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7secondes");
                    }));
                if(timer == 1)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7secondes");
                    }));


                if(timer == 1)
                    getParticipantPlayersPlaying().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,1,1);
                        HAPI.getUtils().sendTitle(player,1,20,1,"§e" + timer,"§7seconde");
                    }));


                if(timer == 0){
                    //cancelTask();

                    try{
                        Bukkit.getScheduler().isCurrentlyRunning(rope.getDuelManager().getDuelClickTask().getTaskId());
                    }catch (IllegalStateException e){
                        rope.getDuelManager().getDuelClickTask().runTaskTimer(rope.getSquidGameManager().getSquidGamePlugin(), 0,1);
                    }
                    if(getTeam1().getPlayers().size() > count){
                        getTeam1().getPlayers().get(count).sendActions(player -> {
                            HAPI.getUtils().sendAboveHotbarMessage(player, "§7Téléportation §b(" + (count + 1) + "/" + getRope().getMaxPlayersPerTeam() +")");
                            HAPI.getUtils().teleportAsync(player, getRope().getDuelManager().getTeam1Location().getNext(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        });
                    }
                    if(getTeam2().getPlayers().size() > count){
                        getTeam2().getPlayers().get(count).sendActions(player -> {
                            HAPI.getUtils().sendAboveHotbarMessage(player, "§7Téléportation §b(" + (count + 1) + "/" + getRope().getMaxPlayersPerTeam() +")");
                            HAPI.getUtils().teleportAsync(player, getRope().getDuelManager().getTeam2Location().getNext(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                        });
                    }
                    count++;

                    if(count > getTeam1().getPlayers().size() && count > getTeam2().getPlayers().size()){
                        duel();
                        cancelTask();
                    }


                }

            }
        }.start();

    }

    @Override
    public void duel() {
        setDuelStatus(DuelStatus.PLAYING);
        for(ParticipantPlayer participantPlayer : getRope().getDuelManager().getDuelClickTask().getCurrentDuel().getParticipantPlayersPlaying())
            getRope().getDuelManager().getTasks().put(participantPlayer, new PlayerRopeGame(getRope(), participantPlayer));

    }

    @Override
    public void stop(Team winner, Team loser) {
        getRope().getDuelManager().getTasks().clear();

        setDuelStatus(DuelStatus.PLAYED);
        getParticipantPlayersPlaying().forEach(participantPlayer ->  {
            getRope().getSquidGameManager().getMessageUtils().sendMessageToPlayer(participantPlayer, "§7§oLe duel est terminé !");
            HPlayer hPlayer = HAPI.getPlayerManager().getHPlayer(participantPlayer.getUuid());
            hPlayer.setTabPrefix("");
            hPlayer.setTagSuffix("");
        });

        loser.setAlive(false);
        loser.getPlayers().forEach(participantPlayer -> {
            getRope().onEliminate(participantPlayer);
            participantPlayer.sendActions(player -> player.sendMessage("§cVotre équipe a été eliminé !"));
        });
        winner.getPlayers().forEach(participantPlayer -> {
            getRope().getQualifiedParticipantPlayer().add(participantPlayer);
            participantPlayer.sendActions(player -> {
                player.sendMessage("§aVotre équipe a gagné le duel !");
                //getRope().getSquidGameManager().getWorldsManager().teleport(SquidLocation.DORTOIR, player);
            });
        });
        getRope().getDuelManager().changeDuel();
    }


    public enum DuelStatus{

        WAITING, PLAYING, PLAYED;

    }
}
