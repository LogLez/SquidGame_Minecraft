package io.hysekai.game.squidgame.game;

import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarPriority;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.common.tasks.NightTask;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.scoreboard.CycleAddon;
import io.hysekai.game.squidgame.state.PvPState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Cycle {


    private final SquidGameManager squidGameManager;
    private CycleStatus cycleStatus = CycleStatus.DAY;
    private boolean cycling = false;
    private int time = 15;

    private List<String> dayText, nightText;

    private CycleAddon cycleAddon;

    public Cycle(SquidGameManager squidGameManager){
        this.squidGameManager = squidGameManager;
        this.cycleAddon = new CycleAddon(getSquidGameManager());

    }



    public void addCycle(){
        setCycling(true);

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        new BukkitRunnable() {
            @Override
            public void run() {
                getSquidGameManager().getSquidGamePlugin().getSidebar().registerAddon(cycleAddon, SidebarPriority.FIRST);

                players.forEach(player -> {
                    if(player.isOnline()){
                        //getSquidGameManager().getWorldsManager().teleport(SquidLocation.DORTOIR, player);
                        //player.teleport(location);
                        player.setLevel(0);
                        player.setExp(0.0f);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,20));
                    }
                });
                startNight();

            }
        }.runTaskLater(getSquidGameManager().getSquidGamePlugin(),50);

    }



    private void startNight(){
        Bukkit.broadcastMessage("§3La nuit va tomber...");
        setTime(15);

        new HysekaiRunTaskTimer(getSquidGameManager(),20,0) {
            @Override
            public void run() {
                time--;

                getSquidGameManager().getSquidGamePlugin().getSidebar().update();
                getSquidGameManager().getMessageUtils().sendTimerToPlayingPlayers(getTime());

                if(time == 0){
                    setCycleStatus(CycleStatus.NIGHT);
                    new NightTask(Cycle.this).start();
                    cancelTask();
                }
            }
        }.start();
    }

    public void startDay(){
        //WorldsManager.SquidGameWorld.DORMITORY.setDay();
        setCycleStatus(CycleStatus.DAY);

        this.getSquidGameManager().setPvp(PvPState.DENY);
        this.getSquidGameManager().getParticipantManager().getPlayingPlayers().forEach(participantPlayer -> participantPlayer.sendActions(player -> player.setHealth(20)));
        loadNpc();
    }


    private void loadNpc(){

        Bukkit.broadcastMessage("§7Npc loaded cinematique... (passage direct a la fin)");
        time = 10;
        new HysekaiRunTaskTimer(getSquidGameManager(),20,0) {
            @Override
            public void run() {
                time--;
                getSquidGameManager().getSquidGamePlugin().getSidebar().update();

                //getSquidGameManager().getScoreboardManager().getScoreboards().values().forEach(fastBoard -> updateScoreBoardLines(fastBoard));
                if(time == 0){
                    setCycling(false);
                    getSquidGameManager().getSquidGamePlugin().getSidebar().unregisterAddon(cycleAddon);
                    squidGameManager.getMiniGamesManager().getCurrentGame().onLoad();

                    cancelTask();
                }
            }
        }.start();
        //TODO runnable pour jouer la cinematique des NPC
        // et a la fin du runnable load :
        // squidGameManager.getMiniGamesManager().getCurrentGame().onLoad();

    }



    public SquidGameManager getSquidGameManager() {return squidGameManager;}
    public void setTime(int time) {this.time = time;}
    public int getTime() {return time;}
    public void setCycling(boolean cycling) {this.cycling = cycling;}
    public boolean isCycling() {return cycling;}
    public CycleStatus getCycleStatus() {return cycleStatus;}
    public void setCycleStatus(CycleStatus cycleStatus) {this.cycleStatus = cycleStatus;}
    public void setPvPState(PvPState pvpState){getSquidGameManager().setPvp(pvpState);}

    public List<String> getDayText() {return dayText;}
    public void setDayText(List<String> dayText) {this.dayText = dayText;}
    public List<String> getNightText() {return nightText;}
    public void setNightText(List<String> nightText) {this.nightText = nightText;}



    public enum CycleStatus{

        DAY("§eJour"),
        NIGHT("§9Nuit");

        String name;

        CycleStatus(String name){ this.name = name;}

        public String getName() {return name;}
    }
}
