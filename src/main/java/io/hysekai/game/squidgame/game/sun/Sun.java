package io.hysekai.game.squidgame.game.sun;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameType;
import io.hysekai.game.squidgame.game.sun.guardian.Guardian;
import io.hysekai.game.squidgame.game.sun.listeners.SunListener;
import io.hysekai.game.squidgame.manager.SquidLocations;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.state.PvPState;
import io.hysekai.game.squidgame.utils.Cuboid;
import io.hysekai.game.squidgame.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sun extends AbstractGame {

    private final SunListener sunListener;

    private final Map<ParticipantPlayer, Long> countdownPunch = new HashMap<>();
    private Guardian guardian;
    private boolean canMove = true;

    private final Cuboid battlefield = new Cuboid(
            new Location(Bukkit.getWorld("world"),931,150,2),
            new Location(Bukkit.getWorld("world"),1067,0,187));

    public Sun(SquidGameManager squidGameManager) {

        super(squidGameManager, GameType.SUN,
                "§7§o[§b§oSquidGame§o§7] §7Bienvenue sur le jeu du :§e " + GameType.SUN.getName(),
                "§7§o[§b§oSquidGame§o§7] §7Le but du jeu est d'atteindre l'autre partie de la zone où se trouve la §epetite fille§7.",
                "§7§o[§b§oSquidGame§o§7] §cAttention§7, quand la §epetite fille§7 se tourne vers vous, il ne faut §cpas bouger §7sous peine d'être... §ctué§7.",
                "§7§o[§b§oSquidGame§o§7] §aBonne chance !");


        this.sunListener = new SunListener(this);
        this.setSpawnLocation(SquidLocations.START_SUN.getLocation());

       // this.getTimeLocation()[0] = new Location(Bukkit.getWorld("world"), -1166, 45, -397,0,0);
      ////  this.getTimeLocation()[1] = new Location(Bukkit.getWorld("world"), -1162, 45, -397, 0,0);
       // this.getTimeLocation()[3] = new Location(Bukkit.getWorld("world"), -1156, 45, -397, 0,0);
       // this.getTimeLocation()[4] = new Location(Bukkit.getWorld("world"), -1152, 45, -397, 0,0);

    }

    @Override
    public void onLoad() {
        super.onLoad();
        Bukkit.getPluginManager().registerEvents(this.sunListener, getSquidGameManager().getSquidGamePlugin());


        this.guardian = new Guardian(this, new Location(Bukkit.getWorld("world"),-1155,4,-212));
        getGuardian().spawnGuardian();

        //sidebar.registerAddon(new TimeAddon(squidGameManager), SidebarPriority.BOTTOM);*
    }

    @Override
    public void onStart() {
        super.onStart();
        getGuardian().startGuardian();
    }

    @Override
    public void onStop() {
        HandlerList.unregisterAll(getSunListener());
        getGuardian().stopGuardian();


        super.onStop();


    }

    @Override
    public void onEliminate(ParticipantPlayer participantPlayer) {

        super.onEliminate(participantPlayer);
        getQualifiedParticipantPlayer().remove(participantPlayer);
        participantPlayer.sendActions(player -> HAPI.getUtils().sendTitle(player,1,30,1,"§cEliminé !" ,""));
        checkWin();

    }

    @Override
    public void onPlayerQualified(ParticipantPlayer participantPlayer) {
        super.onPlayerQualified(participantPlayer);
        getQualifiedParticipantPlayer().add(participantPlayer);
        PlayerUtils.launch(participantPlayer.getPlayer().getLocation().subtract(0,0.2,0), Color.YELLOW);
        checkWin();
    }

    @Override
    public void onPreTime(int time) {

    }


    private void checkWin(){
        if(getQualifiedParticipantPlayer().size() == squidGameManager.getParticipantManager().getPlayingPlayers().size()){
           squidGameManager.setPvp(PvPState.DENY);
            onStop();
        }
    }

    public SunListener getSunListener() {return sunListener;}
    public boolean isCanMove() {return canMove;}
    public void setCanMove(boolean canMove) {this.canMove = canMove;}
    public Guardian getGuardian() {return guardian;}
    public Cuboid getBattlefield() {return battlefield;}
    public Map<ParticipantPlayer, Long> getCountdownPunch() {return countdownPunch;}


}


