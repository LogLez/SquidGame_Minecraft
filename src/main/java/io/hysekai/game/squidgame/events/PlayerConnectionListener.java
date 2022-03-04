package io.hysekai.game.squidgame.events;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.event.player.PlayerLoadEvent;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.manager.SquidLocations;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.players.ParticipantState;
import io.hysekai.game.squidgame.state.SquidGameState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerConnectionListener implements Listener {

    private final SquidGameManager squidGameManager;
    public PlayerConnectionListener(SquidGameManager squidGameManager){ this.squidGameManager = squidGameManager;}

    @EventHandler
    public void onJoin(PlayerLoadEvent event){
        Player player = event.getPlayer();
        ParticipantPlayer participantPlayer = squidGameManager.getParticipantManager().addParticipantPlayer(player);;

        if(squidGameManager.getSquidGameState() == SquidGameState.WAITING){
            //player.teleport(new Location(Bukkit.getWorld("world"),0,10,1));
            squidGameManager.getWorldsManager().teleport(SquidLocations.SPAWN.getLocation(), player);
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            players.forEach(target -> HAPI.getUtils().sendAboveHotbarMessage(target, ChatColor.GOLD + player.getPlayer().getName() + " §fa rejoint la partie §f(§9" + players.size() + "§f/§6100§f)"));
            participantPlayer.cleanUpPlayer(ParticipantState.ALIVE);

            squidGameManager.getSquidGamePlugin().getSidebar().update();
            return;
        }

        participantPlayer.cleanUpPlayer(ParticipantState.DEATH);
        //squidGameManager.getMain().getServer().getPluginManager().callEvent(new ParticipantPlayerJoinEvent(squidGameManager, player));

    }



    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        ParticipantPlayer participantPlayer = squidGameManager.getParticipantManager().getParticipant(player);

        if(participantPlayer == null) return;
        participantPlayer.setState(ParticipantState.DEATH);

        event.setQuitMessage("§7{§c-§7} " + player.getPlayer().getName());

        if(!squidGameManager.isSquidGameState(SquidGameState.WAITING)){
            event.setQuitMessage("§7{§c-§7} " + player.getPlayer().getName());
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }else{
            squidGameManager.getParticipantManager().removeParticipant(player);
        }

        String players = "";
        if(squidGameManager.getParticipantManager().getPlayingPlayers().size() == 1)
            players  = "§8▏ §7Joueur : §9"  + squidGameManager.getParticipantManager().getPlayingPlayers().size() + "§7/§6100";
        else
            players  = "§8▏ §7Joueurs : §9"  + squidGameManager.getParticipantManager().getPlayingPlayers().size() + "§7/§6100";

        //squidGameManager.getScoreboardManager().updateScoreBoards(2,players);
        //squidGameManager.getMain().getServer().getPluginManager().callEvent(new ParticipantPlayerLeaveEvent(squidGameManager, player));
    }



}
