package io.hysekai.game.squidgame.game.bille.teams;

import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;

public class BilleTeam extends AbstractTeam {

    private ParticipantPlayer playerOne;
    private ParticipantPlayer playerTwo;
    private int billePari;
    boolean qualified = false;

    public BilleTeam(AbstractGame abstractGame) {
        super(abstractGame);

    }

    public BilleTeam(AbstractGame abstractGame,ParticipantPlayer playerOne,ParticipantPlayer playerTwo) {
        super(abstractGame);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public BilleTeam(AbstractGame abstractGame,ParticipantPlayer playerOne) {
        this(abstractGame,playerOne,null);

    }

    public ParticipantPlayer getParticipantPlayer(){
        return getPlayerOne() == null ? getPlayerTwo() : getPlayerOne();
    }

    public void setQualified(boolean qualified) {this.qualified = qualified;}
    public boolean isQualified() {return qualified;}

    public ParticipantPlayer getPlayer(boolean firstPlayer) {
        return (firstPlayer ? playerOne : playerTwo);
    }
    
    public ParticipantPlayer getPlayerOne() {
        return playerOne;
    }
    public void setPlayerOne(ParticipantPlayer playerOne) {
        this.playerOne = playerOne;
    }
    public ParticipantPlayer getPlayerTwo() {
        return playerTwo;
    }
    public void setPlayerTwo(ParticipantPlayer playerTwo) {
        this.playerTwo = playerTwo;
    }
    public void setPlayer(ParticipantPlayer participantPlayer) {
        if(getPlayerOne() == null) setPlayerOne(participantPlayer);
        else if(getPlayerTwo() == null) setPlayerTwo(participantPlayer);
    }

    public int getBillePari() {
        return billePari;
    }

    public void setBillePari(int billePari) {
        this.billePari = billePari;
    }
}
