package io.hysekai.game.squidgame.game.squidgame;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SquidGameData {

    public enum GameLocation {
        OUTSIDE,
        INSIDE_SQUARE,
        INSIDE_TRIANGLE,
        INSIDE_NECK_LEFT,
        INSIDE_NECK_MIDDLE,
        INSIDE_NECK_RIGHT,
        INSIDE_WIN_ZONE,
        INSIDE_ENTER_ZONE;

        boolean is(GameLocation... locations) {
            for (GameLocation location : locations)
                if (location == this)
                    return true;
            return false;
        }

        boolean isNot(GameLocation... locations) {
            return !is(locations);
        }
    }

    public enum CrossingBy {
        NONE,
        LEFT,
        RIGHT,
    }

    private boolean attacker;
    private transient ParticipantPlayer participantPlayer;
    private GameLocation gameLocation;
    private CrossingBy crossingBy;

    public SquidGameData(ParticipantPlayer participantPlayer, boolean attacker) {
        this.crossingBy = CrossingBy.NONE;
        this.participantPlayer = participantPlayer;
        this.attacker = attacker;
        this.gameLocation = (attacker) ? GameLocation.OUTSIDE : GameLocation.INSIDE_SQUARE;
        participantPlayer.sendActions(player -> player.sendMessage("Vous êtes " + (attacker ? "attaquant" : "défenseur") + " !"));
    }

    public void teleportToSpawn() {
        Location loc;
        if (attacker)
            loc = new Location(Bukkit.getWorld("world"), -70, 5, 395,2,-1);
        else
            loc = new Location(Bukkit.getWorld("world"), -48, 5, 416,180,1);

        participantPlayer.sendActions((bukkitPlayer) -> HAPI.getUtils().teleportPlayer(bukkitPlayer, loc));
    }

    public void setCrossingBy(CrossingBy crossingBy) {
        this.crossingBy = crossingBy;
    }

    public boolean isCrossingBy(CrossingBy value) {
        return crossingBy == value;
    }

    public CrossingBy getCrossingBy() {
        return crossingBy;
    }

    public boolean isAttacker() {
        return attacker;
    }

    public GameLocation getGameLocation() {
        return gameLocation;
    }

    public void setGameLocation(GameLocation gameLocation) {
        this.gameLocation = gameLocation;
    }
}
