package io.hysekai.game.squidgame.manager;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldsManager {

    private final SquidGameManager squidGameManager;

    public WorldsManager(SquidGameManager squidGameManager){
        this.squidGameManager = squidGameManager;}

    public void teleport(Location location, ParticipantPlayer participantPlayer){
        participantPlayer.sendActions(player -> HAPI.getUtils().teleportPlayer(player, location));
    }

    public void teleport(Location location, Player player){
        HAPI.getUtils().teleportPlayer(player, location);
    }
    public void teleportAll(Location world){
        getSquidGameManager().getParticipantManager().getParticipants().values().forEach(participantPlayer -> teleport(world, participantPlayer));
    }

    public SquidGameManager getSquidGameManager() { return squidGameManager; }

}
