package io.hysekai.game.squidgame.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;

public class MessageUtils {

    private final SquidGameManager squidGameManager;
    private final String prefix = "§7§o[§6§oSquidGame§o§7] ";

    public MessageUtils(SquidGameManager squidGameManager) {
        this.squidGameManager = squidGameManager;
    }


    public void sendTimerToPlayingPlayers(int time){

        String message = "";
        switch (time){
            case 10:
                message =  "§9";
                break;
            case 5:
                message =  "§c";
                break;
            case 4:
                message =  "§6";
                break;
            case 3:
                message =  "§e";
                break;
            case 2:
                message =  "§2";
                break;
            case 1:
                message =  "§a";
                break;
            default:
                return;

        }

        String timer = message + time;
        getSquidGameManager().getParticipantManager().
                getPlayingPlayers().
                forEach(participantPlayer -> participantPlayer.sendActions
                        (player -> {
                            player.playSound(player.getLocation(), Sound.NOTE_PLING,5,4);
                            HAPI.getUtils().sendTitle(player,1,30,1, timer,"");
                        }));

    }
    public void sendBroadcast(String message){
        Bukkit.broadcastMessage(prefix + message);
    }
    public void sendMessageToPlayer(ParticipantPlayer participantPlayer, String message){
        participantPlayer.sendActions(player -> player.sendMessage(prefix + message));
    }
    public void sendMessageToPlayer(Player player, String message){
        player.sendMessage(prefix + message);
    }
    public void sendBroadcast(String message, List<Player> players){
        players.forEach(player -> sendMessageToPlayer(player, message));
    }

    public String getPrefix() {return prefix;}
    public SquidGameManager getSquidGameManager() { return squidGameManager; }
}

