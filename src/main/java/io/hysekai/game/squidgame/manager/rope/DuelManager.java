package io.hysekai.game.squidgame.manager.rope;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.hologram.Hologram;
import io.hysekai.bukkit.api.hologram.HologramBuilder;
import io.hysekai.bukkit.api.hologram.line.TextLine;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.rope.tasks.DuelClickTask;
import io.hysekai.game.squidgame.game.rope.team.HologramTeam;
import io.hysekai.game.squidgame.game.rope.data.PlayerRopeGame;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.duel.DuelTeamLocation;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.game.rope.duel.Duel;
import io.hysekai.game.squidgame.game.rope.team.Team;
import org.bukkit.*;


import java.util.*;
import java.util.stream.Collectors;

public class DuelManager {

    private final Rope rope;
    private final HologramTeam hologramTeam;
    private final DuelClickTask duelClickTask;
    private final List<Duel> duels = new ArrayList<>();

    private final DuelTeamLocation team1Location = new DuelTeamLocation(new Location(Bukkit.getWorld("world"),-64.5,72.5,89.5,90,0),
            new Location(Bukkit.getWorld("world"),-63.5,72.5,89.5,90,0),
            new Location(Bukkit.getWorld("world"),-64.5,72.5,90.5,90,0));

    private final DuelTeamLocation team2Location = new DuelTeamLocation(new Location(Bukkit.getWorld("world"),-86.5,72.5,91.5,-90,2),
            new Location(Bukkit.getWorld("world"),-87.5,72.5,90.5,-90,2),
            new Location(Bukkit.getWorld("world"),-86.5,72.5,90.5,-90,2));


    private Hologram hologramScore;
    private Map<ParticipantPlayer, PlayerRopeGame> tasks = new HashMap<>();


    public DuelManager(Rope rope){
        this.rope = rope;
        this.duelClickTask = new DuelClickTask(this, 20, 0);
        this.hologramTeam = new HologramTeam(this);
    }

    public boolean isInDuel(Team team){
        Optional<Duel> optionalDuel = getDuels().stream().filter(duel -> duel.getTeam1().equals(team) || duel.getTeam2().equals(team)).findAny();
        if(optionalDuel.isEmpty()) return false;
        if(optionalDuel.get().getDuelStatus() != Duel.DuelStatus.PLAYING) return false;
        return true;
    }

    public boolean isInDuel(ParticipantPlayer participantPlayer){
        if( getDuelClickTask().getCurrentDuel() == null) return false;
        return getDuelClickTask().getCurrentDuel().isIn(participantPlayer);
    }

    public void changeDuel(){
        List<Duel> duelsAvailable = getDuels().stream().filter(duel -> duel.getDuelStatus() == Duel.DuelStatus.WAITING).collect(Collectors.toList());
        if(duelsAvailable.size() == 0){
            rope.onStop();
            return;
        }


        Duel duel = duelsAvailable.get(new Random().nextInt(duelsAvailable.size()));
        getDuelClickTask().setCurrentDuel(duel);


        new HysekaiRunTaskTimer(getRope().getSquidGameManager(),20,0) {
            int timer = 11;
            @Override
            public void run() {

                if(timer == 10)
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        HAPI.getUtils().sendTitle(player,0,20,0,"§9"+timer,"");});

                if(timer == 5)
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,5,0);
                        HAPI.getUtils().sendTitle(player,0,20,0,"§c"+timer,"");});

                if(timer == 4)
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,5,1);
                        HAPI.getUtils().sendTitle(player,0,20,0,"§6"+timer,"");});

                if(timer == 3)
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 2);
                        HAPI.getUtils().sendTitle(player, 0, 20, 0, "§e" + timer, "");
                    });

                if(timer == 2)
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,5,3);
                        HAPI.getUtils().sendTitle(player,0,20,0,"§2"+timer,"");});

                if(timer == 1)
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING,5,4);
                        HAPI.getUtils().sendTitle(player,0,20,0,"§a"+timer,"");});


                if(timer == 0){
                    duel.preparation();
                    cancelTask();
                }
                timer--;
            }
        }.start();

    }

    public void createDuels(){

        List<AbstractTeam> teams = new ArrayList<>(getRope().getTeams());
        Collections.shuffle(teams);

        for(int i = 0; i < teams.size() - 1;i+=2)
            this.duels.add(new Duel(getRope(),(Team) teams.get(i), (Team) teams.get(i+1)));

        changeDuel();

    }


    public void setHologram(Location location){
        HologramBuilder hologramBuilder = new HologramBuilder(location);
        hologramBuilder.line(new TextLine("§e§oTirage au sort"));
        hologramBuilder.line(new TextLine("§b§k!!§7 -  §b§k!!"));
        this.hologramScore = hologramBuilder.build();

    }

    public DuelClickTask getDuelClickTask() {return duelClickTask;}

    public Map<ParticipantPlayer, PlayerRopeGame> getTasks() {return tasks;}

    public Rope getRope() {return rope;}
    public List<Duel> getDuels() {return duels;}

    public DuelTeamLocation getTeam1Location() {return team1Location;}
    public DuelTeamLocation getTeam2Location() {return team2Location;}


    public HologramTeam getHologramTeam() {return hologramTeam;}
    public Hologram getHologramScore() {return hologramScore;}

    public void updateHologramDuel(AbstractTeam team1, AbstractTeam team2) {

        ((TextLine) this.hologramScore.getLine(0)).setText("§7§o§lDuel");
        ((TextLine) this.hologramScore.getLine(1)).setText(""+ (team1 != null ? team1.getId() : "§b§k!!") + " - " + (team2 != null ? team2.getId() : "§b§k!!"));
    }
    public void resetHologram( ) {
        ((TextLine) this.hologramScore.getLine(0)).setText("§e§oTirage au sort");
        ((TextLine) this.hologramScore.getLine(1)).setText("§b§k!!§7 - §b§k!!");
    }


}
