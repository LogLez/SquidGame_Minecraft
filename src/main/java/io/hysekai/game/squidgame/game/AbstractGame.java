package io.hysekai.game.squidgame.game;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarPriority;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.common.tasks.LoadGameTask;
import io.hysekai.game.squidgame.common.tasks.MiniGamesCountDownTask;
import io.hysekai.game.squidgame.manager.MiniGamesManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.scoreboard.GameBaseAddon;
import io.hysekai.game.squidgame.state.PvPState;
import io.hysekai.game.squidgame.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractGame {

    protected final SquidGameManager squidGameManager;

    private final GameType tempGame;
    private final LoadGameTask loadGameTask;
    private final List<ParticipantPlayer> qualifiedParticipantPlayer = new ArrayList<>();
    private Location location;
    private final List<String> announce = new ArrayList<>();
    private GameStatus gameStatus = GameStatus.WAITING;
    private final Location[] timeLocation = new Location[5];

    protected final Sidebar sidebar;
    protected GameBaseAddon gameBaseAddon;

    public AbstractGame(SquidGameManager squidGameManager, GameType tempGame, String... announce){
        this.squidGameManager = squidGameManager;
        this.tempGame = tempGame;
        this.announce.addAll(Arrays.asList(announce));
        this.loadGameTask = new LoadGameTask(this);
        this.sidebar = squidGameManager.getSquidGamePlugin().getSidebar();
    }

    /**
     * Methods relatifs a tous les games...
     */

    private void teleportAll(){
        Bukkit.getOnlinePlayers().forEach(player -> HAPI.getUtils().teleportPlayer(player, location));
    }

    public void onLoad(){
        setGameStatus(GameStatus.PREGAME);
        //getSquidGameManager().getScoreboardManager().getScoreboards().values().forEach(((AbstractScoreBoard) AbstractGame.this)::loadScoreboard);

        if(this.getSquidGameManager().getMiniGamesManager().getMiniGamesCountDownTask() != null)
            this.getSquidGameManager().getMiniGamesManager().getMiniGamesCountDownTask().cancel();

        this.getSquidGameManager().getMiniGamesManager().setMiniGamesCountDownTask(new MiniGamesCountDownTask(getSquidGameManager(), this));

        gameBaseAddon = new GameBaseAddon(squidGameManager, getGameType());
        sidebar.registerAddon(gameBaseAddon, SidebarPriority.FIRST);

        teleportAll();
        getLoadGameTask().start();
    }


    public void onPlayerQualified(ParticipantPlayer participantPlayer){
        participantPlayer.qualified();
        participantPlayer.sendActions(player -> getSquidGameManager().getMessageUtils().sendMessageToPlayer(player,"§aFélicitation, vous venez de terminer le §e"+ getGameType().getName()));
    }

    public void onEliminate(ParticipantPlayer participantPlayer){
        participantPlayer.eliminate();
        squidGameManager.getSquidGamePlugin().getSidebar().update();
        //getSquidGameManager().getScoreboardManager().updateScoreBoards(2,"§8▏ §7Joueurs en vie : §9" + getSquidGameManager().getParticipantManager().getPlayingPlayers().size());
        participantPlayer.sendActions(player -> getSquidGameManager().getMessageUtils().sendBroadcast("§c"+player.getName() + " §7a été eliminé."));
    }

    public void onStart(){
        if(tempGame.isHasPvP())
            squidGameManager.setPvp(PvPState.ALLOWED);
        else
            squidGameManager.setPvp(PvPState.DENY);

        //getSquidGameManager().getScoreboardManager().getScoreboards().values().forEach(((AbstractScoreBoard) AbstractGame.this)::setUpGamingScoreboard);
        squidGameManager.getParticipantManager().getPlayingPlayers().forEach(participantPlayer -> participantPlayer.sendActions(player -> HAPI.getUtils().sendTitle(player,10,30,10,"§aLa partie commence !","§7Bonne chance")));

        this.getSquidGameManager().getMiniGamesManager().getMiniGamesCountDownTask().start();
    }

    public void onStop(){
        setGameStatus(GameStatus.FINISH);
        getSquidGameManager().getMiniGamesManager().setMiniGamesCountDownTask(null);
        getSquidGameManager().getSquidGamePlugin().getSidebar().update();
        getSquidGameManager().getMessageUtils().sendBroadcast("§eLe jeu est terminé ! §7Tous les particpants sont soit §aqualifiés§7, soit §cdécédés§7.");
        getSquidGameManager().getMessageUtils().sendBroadcast("§7§oVous allez retourné au dortoir dans quelques instants...");

        if (gameBaseAddon != null)
            sidebar.unregisterAddon(gameBaseAddon);


        MiniGamesManager miniGamesManager = squidGameManager.getMiniGamesManager();
        miniGamesManager.nextGame(getGameType().getId());


        getSquidGameManager().getCycle().addCycle();
    }

    public abstract void onPreTime(int time);

    public GameType getGameType() { return tempGame; }
    public Location getSpawnLocation() { return location; }
    public void setSpawnLocation(Location location) { this.location = location; }
    public List<String> getAnnounce() {return announce;}
    public GameStatus getGameStatus() {return gameStatus;}
    public void setGameStatus(GameStatus gameStatus) {this.gameStatus = gameStatus;}
    public SquidGameManager getSquidGameManager() {return squidGameManager;}
    public Location[] getTimeLocation() { return timeLocation; }
    public LoadGameTask getLoadGameTask() {return loadGameTask;}
    public List<ParticipantPlayer> getQualifiedParticipantPlayer() {return qualifiedParticipantPlayer;}
}
