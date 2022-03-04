package io.hysekai.game.squidgame.team;

import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.TeamManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;

import java.util.ArrayList;
import java.util.List;

public class AbstractTeam  {

    private int id;
    private AbstractGame abstractGame;
    private boolean isAlive = true;
    private final List<ParticipantPlayer> participantPlayers = new ArrayList<>();


    public AbstractTeam(AbstractGame abstractGame) {
        this.abstractGame  = abstractGame;
        this.id = ((TeamManager) abstractGame).getTeams().size() + 1;
        ((TeamManager) abstractGame).getTeams().add(this);
    }

    public List<ParticipantPlayer> getPlayers() { return participantPlayers; }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public AbstractGame getAbstractGame() {return abstractGame;}
    public void setAbstractGame(AbstractGame abstractGame) {this.abstractGame = abstractGame;}

    public boolean isAlive() {return isAlive;}
    public void setAlive(boolean alive) {isAlive = alive;}
}
