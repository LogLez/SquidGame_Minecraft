package io.hysekai.game.squidgame.events.participants;

import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.players.ParticipantState;
import io.hysekai.game.squidgame.state.SquidGameState;
import io.hysekai.game.squidgame.SquidGameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ParticipantPlayerLeaveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final SquidGameManager squidGameManager;
    private ParticipantPlayer participantPlayer;
    private final Player player;

    private boolean cancelled = false;

    public ParticipantPlayerLeaveEvent(SquidGameManager squidGameManager, Player player) {
        this.squidGameManager = squidGameManager;
        this.player = player;



        if(!squidGameManager.getParticipantManager().isParticipant(player)) return;
        if(squidGameManager.isSquidGameState(SquidGameState.WAITING)) {
            squidGameManager.getParticipantManager().removeParticipant(player);
        }else{
            this.participantPlayer = squidGameManager.getParticipantManager().getParticipant(player);
            participantPlayer.setState(ParticipantState.DEATH);
        }



    }


    public SquidGameManager getSquidGameManager() {
        return squidGameManager;
    }
    public ParticipantPlayer getParticipantPlayer() {return participantPlayer;}
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
