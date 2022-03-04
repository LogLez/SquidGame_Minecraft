package io.hysekai.game.squidgame.game.rope.duel;

import io.hysekai.game.squidgame.game.rope.team.Team;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class DuelTeamLocation {

    private Location startLocation;
    private Team team;
    private Vector directionBehind, directionLeft;
    private int i = -1;

    public DuelTeamLocation(Location startLocation, Location behind, Location left){
        this.startLocation = startLocation.clone();
        this.directionBehind = behind.toVector().subtract(startLocation.toVector()).normalize().clone();
        this.directionLeft = left.toVector().subtract(startLocation.toVector()).normalize().clone();
    }

    public Location getNext(){
        i++;
        int j = 0;
        if( i % 2 != 0)
            j = 2;

        return getStartLocation().clone().clone().add(getDirectionBehind().clone().multiply(i*4)).add(getDirectionLeft().clone().multiply(j));
    }

    public Location getStartLocation() {return startLocation;}
    public void setStartLocation(Location startLocation) {this.startLocation = startLocation;}

    public Vector getDirectionBehind() {return directionBehind;}
    public void setDirectionBehind(Vector directionBehind) {this.directionBehind = directionBehind;}

    public Vector getDirectionLeft() {return directionLeft;}
    public void setDirectionLeft(Vector directionLeft) {this.directionLeft = directionLeft;}

    public Team getTeam() {return team;}
    public void setTeam(Team team) {this.team = team;}

}
