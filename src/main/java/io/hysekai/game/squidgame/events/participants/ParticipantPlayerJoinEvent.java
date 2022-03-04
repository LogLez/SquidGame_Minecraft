package io.hysekai.game.squidgame.events.participants;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.manager.SquidLocations;
import io.hysekai.game.squidgame.players.ParticipantState;
import io.hysekai.game.squidgame.state.SquidGameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ParticipantPlayerJoinEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();


    private final SquidGameManager squidGameManager;
    private final Player player;

    private boolean cancelled = false;

    public ParticipantPlayerJoinEvent(SquidGameManager squidGameManager, Player player) {
        this.squidGameManager =squidGameManager;
        this.player = player;


        HAPI.getUtils().setTabHeaderAndFooter(player, """
                §e● §eBienvenue sur §6PLAY.HYSEKAI.FR §e●
                                
                """, """

                §e● §eTwitter : §b@Hysekai
                §e● §eBoutique : §6store.hysekai.fr
                §e● §eDiscord : §9discord.gg/hysekai
                """);

        if(squidGameManager.isSquidGameState(SquidGameState.WAITING)){

            getSquidGameManager().getWorldsManager().teleport(SquidLocations.SPAWN.getLocation(), player);
            Bukkit.broadcastMessage("Test 001");
            if(!squidGameManager.getParticipantManager().isParticipant(player))
                squidGameManager.getParticipantManager().addParticipantPlayer(player);
            else
                squidGameManager.getParticipantManager().getParticipant(player).cleanUpPlayer();
        }else{
            player.setGameMode(GameMode.SPECTATOR);
            squidGameManager.getParticipantManager().getParticipant(player).setState(ParticipantState.DEATH);

            HAPI.getUtils().teleportPlayer(player, squidGameManager.getMiniGamesManager().getCurrentGame().getSpawnLocation());
        }

        String players = "";
        if(getSquidGameManager().getParticipantManager().getPlayingPlayers().size() == 1)
            players  = "§8▏ §7Joueur : §9"  + getSquidGameManager().getParticipantManager().getPlayingPlayers().size() + "§7/§6100" ;
        else
            players  = "§8▏ §7Joueurs : §9"  + getSquidGameManager().getParticipantManager().getPlayingPlayers().size() + "§7/§6100";

        //getSquidGameManager().getScoreboardManager().updateScoreBoards(2,players);


    }


   public SquidGameManager getSquidGameManager() {
        return squidGameManager;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
