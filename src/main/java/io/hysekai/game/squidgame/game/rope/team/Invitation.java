package io.hysekai.game.squidgame.game.rope.team;

import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;

public class Invitation {

    private final Team team;
    private final ParticipantPlayer participantPlayer;

    public Invitation(AbstractTeam team, ParticipantPlayer participantPlayer){
        this.team = (Team) team;
        this.participantPlayer = participantPlayer;
    }

    public AbstractTeam getTeam() {return team;}
    public ParticipantPlayer getParticipantPlayer() {return participantPlayer;}
}
