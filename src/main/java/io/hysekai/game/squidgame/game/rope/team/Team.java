package io.hysekai.game.squidgame.game.rope.team;

import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.utils.Colors;
import kotlin.jvm.internal.PackageReference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class Team extends AbstractTeam {

    private static final Location start = new Location(Bukkit.getWorld("world"),-68.5,35.2,28.5,0,2);
    public static Vector behind = new Vector(0,0,-1);


    private ParticipantPlayer chief;
    private int points;
    private final Location first;
    private final Colors teamColor;


    public Team(Rope rope){
        this(rope, null);
    }
    public Team(Rope rope, ParticipantPlayer chief){
        super(rope);

        this.chief = chief;
        this.teamColor = getRandomColor();
        this.first = start.clone().add(new Vector(-1,0,0).multiply(2*(rope.getTeams().size() - 1)));
    }




    private Colors getRandomColor(){
        final List<Colors> alreadyTaken = new ArrayList<>();
        List<AbstractTeam> teams = ((Rope) getAbstractGame()).getTeams().stream().filter(abstractTeam -> !abstractTeam.equals(this)).collect(Collectors.toList());
        teams.forEach(abstractTeam -> {
            alreadyTaken.add( ((Team)abstractTeam).getTeamColor());
            Bukkit.broadcastMessage("This team "+ ( (Team)abstractTeam).getId()+ " " + ( (Team)abstractTeam).getTeamColor());
        });

        if(alreadyTaken.size() == ((Rope) getAbstractGame()).getTeams().size())
            return null;


        Colors color = Colors.values()[new Random().nextInt(Colors.values().length - 1)];
        while(alreadyTaken.contains(color)){
            color =  Colors.values()[new Random().nextInt(Colors.values().length - 1)];
        }

        return  color;
    }

    public int getPoints() {return points;}
    public void addPoint(int points) {
        this.points += points;
        ( (Rope) getAbstractGame()).getDuelManager().getHologramTeam().updateScore();

    }
    public void removePoint(int points) {
        this.points -= points;
        if(this.points <= 0) this.points = 0;
        ( (Rope) getAbstractGame()).getDuelManager().getHologramTeam().updateScore();

    }

    public ParticipantPlayer getChief() {return chief;}
    public void setChief(ParticipantPlayer chief) {this.chief = chief;}
    public Location getFirst() {return first;}
    public Colors getTeamColor() {return teamColor;}
}
