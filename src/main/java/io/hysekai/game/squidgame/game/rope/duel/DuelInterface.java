package io.hysekai.game.squidgame.game.rope.duel;

import io.hysekai.game.squidgame.game.rope.team.Team;

public interface DuelInterface {

    void preparation();
    void duel();
    void stop(Team winner, Team loser);
}
