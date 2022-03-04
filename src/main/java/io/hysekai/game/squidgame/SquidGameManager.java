package io.hysekai.game.squidgame;

import java.util.Arrays;

import org.bukkit.Bukkit;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarPriority;
import io.hysekai.game.squidgame.commands.TestCmd;
import io.hysekai.game.squidgame.common.HysekaiTask;
import io.hysekai.game.squidgame.common.tasks.BeginTask;
import io.hysekai.game.squidgame.events.GeneralListener;
import io.hysekai.game.squidgame.events.PlayerConnectionListener;
import io.hysekai.game.squidgame.game.Cycle;
import io.hysekai.game.squidgame.manager.MiniGamesManager;
import io.hysekai.game.squidgame.manager.ParticipantManager;
import io.hysekai.game.squidgame.manager.SquidLocations;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantState;
import io.hysekai.game.squidgame.scoreboard.LobbyAddon;
import io.hysekai.game.squidgame.scoreboard.StatisticsAddon;
import io.hysekai.game.squidgame.scoreboard.TimeAddon;
import io.hysekai.game.squidgame.state.PvPState;
import io.hysekai.game.squidgame.state.SquidGameState;
import io.hysekai.game.squidgame.utils.MessageUtils;
import io.hysekai.game.squidgame.utils.Numbers;
import io.hysekai.game.squidgame.utils.TimeUtils;

public class SquidGameManager {

    private final SquidGamePlugin squidGamePlugin;

    private SquidGameState gameState;
    private final WorldsManager worldsManager;
    private final ParticipantManager participantManager;
    private final MiniGamesManager miniGamesManager;

    private final MessageUtils messageUtils;
    private final TimeUtils timeUtils;

    private BeginTask beginTask;
    private PvPState pvpState = PvPState.DENY;
    private Cycle cycle;
    private LobbyAddon lobbyAddon;

    public SquidGameManager(SquidGamePlugin squidGamePlugin){

        this.squidGamePlugin = squidGamePlugin;
        this.gameState = SquidGameState.WAITING;

        this.worldsManager = new WorldsManager(this);
        this.participantManager = new ParticipantManager(this);
        this.miniGamesManager = new MiniGamesManager(this);

        this.timeUtils = new TimeUtils(this);
        this.messageUtils = new MessageUtils(this);
        this.beginTask = new BeginTask(this,20,0);
        this.cycle = new Cycle(this);

        this.lobbyAddon = new LobbyAddon(this);
        this.squidGamePlugin.getSidebar().registerAddon(lobbyAddon, SidebarPriority.FIRST);
        this.squidGamePlugin.getSidebar().registerAddon(new TimeAddon(this), SidebarPriority.TOP);
        this.squidGamePlugin.getSidebar().registerAddon(new StatisticsAddon(this), SidebarPriority.LAST);

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(this), squidGamePlugin);
        Bukkit.getPluginManager().registerEvents(new GeneralListener(this), squidGamePlugin);

        squidGamePlugin.getCommand("team").setExecutor(new TestCmd(this));


    }

    public void stopGame() {
        if(getSquidGameState() == SquidGameState.WAITING) return;

        HysekaiTask.tasks.forEach(HysekaiTask::cancelTask);
        HysekaiTask.tasks.clear();
        setBeginTask(null);

        setSquidGameState(SquidGameState.WAITING);
        getParticipantManager().getPlayingPlayers().forEach(participantPlayer -> {
            participantPlayer.setState(ParticipantState.ALIVE);
            participantPlayer.cleanUpPlayer();
            getWorldsManager().teleport(SquidLocations.SPAWN.getLocation(), participantPlayer);
        });


    }


    public void startPreGame() {
        Arrays.asList(Numbers.values()).forEach(numbers -> {
            if(numbers.getSchematic() != null)numbers.getSchematic().load();
        });

        setSquidGameState(SquidGameState.PEREGAME);
        beginTask = new BeginTask(this,20,0);
        getBeginTask().start();
    }

    public void startGame() {
        if (lobbyAddon != null)
            squidGamePlugin.getSidebar().unregisterAddon(lobbyAddon);


        setSquidGameState(SquidGameState.GAMING);
        Bukkit.getOnlinePlayers().forEach(player -> {
            //getWorldsManager().teleport(SquidLocation.DORTOIR, player);
            HAPI.getUtils().sendTitle(player,10,20,10,"§aLancement", "§eBonne chance...");
        });

        getMiniGamesManager().getCurrentGame().onLoad();

        /*new Night(this, true, PvP.ALLOWED) {
            @Override
            public List<String> getDayText() {
                return null;
            }

            @Override
            public List<String> getNightText() {
                return null;
            }
        };*/
    }
    public void finishGame() {
        setSquidGameState(SquidGameState.FINISH);
        Bukkit.getScheduler().cancelAllTasks();
    }



    public ParticipantManager getParticipantManager() { return participantManager; }
    public MiniGamesManager getMiniGamesManager() { return miniGamesManager; }
    public WorldsManager getWorldsManager() {return worldsManager;}

    public MessageUtils getMessageUtils() { return messageUtils; }
    public TimeUtils getTimeUtils() { return timeUtils; }

    public SquidGamePlugin getSquidGamePlugin() { return squidGamePlugin; }
    public SquidGameState getSquidGameState() { return gameState; }
    public void setSquidGameState(SquidGameState gameState) {
        this.gameState = gameState;

    }
    public boolean isSquidGameState(SquidGameState gameState){ return this.gameState == gameState; }

    public PvPState getPvPState() { return pvpState; }
    public void setPvp(PvPState pvpState) { this.pvpState = pvpState; }
    public Cycle getCycle() {return cycle;}
    public BeginTask getBeginTask() {return beginTask;}
    public void setBeginTask(BeginTask beginTask) {this.beginTask = beginTask;}


}
