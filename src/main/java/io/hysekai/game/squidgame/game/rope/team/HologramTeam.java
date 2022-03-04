package io.hysekai.game.squidgame.game.rope.team;

import io.hysekai.bukkit.api.hologram.Hologram;
import io.hysekai.bukkit.api.hologram.HologramBuilder;
import io.hysekai.bukkit.api.hologram.line.TextLine;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.manager.rope.DuelManager;
import io.hysekai.game.squidgame.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HologramTeam {

    private final DuelManager duelManager;

    private final Hologram hologramTeam1Spec;
    private final Hologram hologramTeam1Play;
    private final Hologram hologramTeam2Spec;
    private final Hologram hologramTeam2Play;

    private Team team1, team2;

    public HologramTeam(DuelManager duelManager){
        this.duelManager = duelManager;

        this.hologramTeam1Play = new HologramBuilder(new Location(Bukkit.getWorld("world"),-75,75,84))
                .line(new TextLine("§e§lEquipe ?")).line(new TextLine("§7§o§l0 points")).build();
        this.hologramTeam1Spec = new HologramBuilder(new Location(Bukkit.getWorld("world"),-68,37,43))
                .line(new TextLine("§e§lEquipe ?")).line(new TextLine("§7§o§l0 points")).build();

        this.hologramTeam2Play = new HologramBuilder(new Location(Bukkit.getWorld("world"),-75,75,95))
                .line(new TextLine("§e§lEquipe ?")).line(new TextLine("§7§o§l0 points")).build();
        this.hologramTeam2Spec = new HologramBuilder(new Location(Bukkit.getWorld("world"),-81,37,42))
                .line(new TextLine("§e§lEquipe ?")).line(new TextLine("§7§o§l0 points")).build();
    }


    public void setHologramTeam(Team team1, Team team2){
        setTeams(team1, team2);

        PlayerUtils.launch(getHologramTeam1Spec().getLocation().add(0,1,0), ((Team) team1).getTeamColor().getColor());
        PlayerUtils.launch(getHologramTeam2Spec().getLocation().add(0,1,0),  ((Team) team2).getTeamColor().getColor());

        ((TextLine) this.hologramTeam1Play.getLine(0)).setText("§e§lEquipe " + team1.getTeamColor().getChatColor() + team1.getId());
        ((TextLine) this.hologramTeam1Spec.getLine(0)).setText("§e§lEquipe " + team1.getTeamColor().getChatColor() + team1.getId());

        ((TextLine) this.hologramTeam2Play.getLine(0)).setText("§e§lEquipe " + team2.getTeamColor().getChatColor() + team2.getId());
        ((TextLine) this.hologramTeam2Spec.getLine(0)).setText("§e§lEquipe " + team2.getTeamColor().getChatColor() + team2.getId());
    }


    public void updateScore(){
        if(team1 == null || team2 == null) return;

        String team1Points = "points";
        String team2Points = "points";

        if(team1.getPoints() <=1 ) team1Points = " point";
        if(team2.getPoints() <=1 ) team2Points = " point";


        ((TextLine) this.hologramTeam1Play.getLine(1)).setText("§7"+ team1.getPoints() + " " + team1Points);
        ((TextLine) this.hologramTeam1Spec.getLine(1)).setText("§7"+ team1.getPoints() + " " + team1Points);

        ((TextLine) this.hologramTeam2Play.getLine(1)).setText("§7"+ team2.getPoints() + " " + team2Points);
        ((TextLine) this.hologramTeam2Spec.getLine(1)).setText("§7"+ team2.getPoints() + " " + team2Points);
    }

    public void setTeams(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public DuelManager getDuelManager() {return duelManager;}
    public Hologram getHologramTeam1Spec() {return hologramTeam1Spec;}
    public Hologram getHologramTeam1Play() {return hologramTeam1Play;}
    public Hologram getHologramTeam2Spec() {return hologramTeam2Spec;}
    public Hologram getHologramTeam2Play() {return hologramTeam2Play;}

}
