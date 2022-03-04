package io.hysekai.game.squidgame.game;

import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.game.rope.team.Invitation;
import io.hysekai.game.squidgame.players.ParticipantPlayer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TeamManager{

    /*
        Team system
    */
    boolean addParticipantToTeam(ParticipantPlayer participantPlayer);
    boolean addParticipantToTeam(ParticipantPlayer participantPlayer, AbstractTeam team);
    void removeParticipantToTeam(ParticipantPlayer participantPlayer);
    Optional<AbstractTeam> getTeamById(int id);
    void loadInfos();
    boolean hasTeam(ParticipantPlayer participantPlayer);
    Optional<AbstractTeam> getTeam(ParticipantPlayer participantPlayer);
    boolean isTeamFull(AbstractTeam team);
    boolean isTimeTeam();
    List<AbstractTeam> getTeams();
    int getMaxPlayersPerTeam();
    int getMaxTeams();
    void setRandomTeamToPlayer(ParticipantPlayer participantPlayer);
    /*
    Invitation system
     */
    Map<ParticipantPlayer, List<Invitation>> getInvitations();
    boolean hasInvitationOfTeam(ParticipantPlayer participantPlayer, int id);
    boolean hasInvitationOfTeam(ParticipantPlayer participantPlayer, AbstractTeam team);Optional<Invitation> getInvitationOfTeam(ParticipantPlayer participantPlayer, int id );
    boolean sendJoinRequest(ParticipantPlayer participantPlayer, ParticipantPlayer targetParticipantPlayer, AbstractTeam team);
}
