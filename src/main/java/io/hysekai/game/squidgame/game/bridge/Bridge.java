package io.hysekai.game.squidgame.game.bridge;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.GameType;
import io.hysekai.game.squidgame.game.bridge.chasubles.ChasubleManager;
import io.hysekai.game.squidgame.game.bridge.glass.GlassBridge;
import io.hysekai.game.squidgame.game.bridge.listeners.BridgeListener;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.scoreboard.games.BridgeGameAddon;
import io.hysekai.game.squidgame.utils.Cuboid;
import io.hysekai.game.squidgame.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class Bridge extends AbstractGame {

    private GlassBridge glassBridge;

    private final BridgeListener bridgeListener;
    private final List<GlassBridgePlayer> glassBridgePlayers = new ArrayList<>();
    private final ChasubleManager chasubleManager;


    private final Cuboid spawnZone = new Cuboid(
            new Location(Bukkit.getWorld("world"),-19,9,7),
            new Location(Bukkit.getWorld("world"),-60,15,2)
    );

    private final Cuboid startZone = new Cuboid(
            new Location(Bukkit.getWorld("world"),-82.5,44,124),
            new Location(Bukkit.getWorld("world"),-62,56,109)
    );

    private final Cuboid finishZone = new Cuboid(
            new Location(Bukkit.getWorld("world"),-82,43,37),
            new Location(Bukkit.getWorld("world"),-62,55,16)
    );

    private Direction direction = null;
    private boolean canChoseChasuble = false;
    private GlassBridgePlayer currentPlayer;
    private int current = 0;
    private BridgeGameAddon bridgeGameAddon;

    public Bridge(SquidGameManager squidGameManager) {
        super(squidGameManager, GameType.BRIDGE,
                "§7§o[§6§oSquidGame§o§7] §7Bienvenue dans le jeu : §e" + GameType.BRIDGE.getName(),
                "§7§o[§6§oSquidGame§o§7] §7Dans quelques instants, vous allez pouvoir choisir votre §achasuble §7qui définira votre §eordre de passage§7.",
                "§7§o[§6§oSquidGame§o§7] §cAttention§7, l'ordre de passage est §9aléatoire§7... §7Soit §acroissant§7 soit §cdécroissant§7.");
        this.chasubleManager = new ChasubleManager(this);
        this.bridgeListener = new BridgeListener(this);
        this.bridgeGameAddon = new BridgeGameAddon(this);


        //this.setSpawnLocation(SquidLocation.WHITE_ROOM.getLocation());

        this.getTimeLocation()[0] = new Location(Bukkit.getWorld("world"), -81, 68, 26,0,0);
        this.getTimeLocation()[1] = new Location(Bukkit.getWorld("world"), -77, 68, 26, 0,0);
        this.getTimeLocation()[3] = new Location(Bukkit.getWorld("world"), -71, 68, 26, 0,0);
        this.getTimeLocation()[4] = new Location(Bukkit.getWorld("world"), -67, 68, 26, 0,0);


    }

    /*
    /////////////OVERRIDE METHODS//////////////////////////
     */

    @Override
    public void onLoad() {
        Bukkit.getPluginManager().registerEvents(this.bridgeListener, squidGameManager.getSquidGamePlugin());
        super.onLoad();

        int random = new Random().nextInt(2);
        if(random == 0)direction = Direction.INCREASE;
        if(random == 1) direction = Direction.DECREASE;

        Location location = new Location(Bukkit.getWorld("world"), -12.5, 4, 36.5,(float) 152.7,(float)-0.7);
        Location end = new Location(Bukkit.getWorld("world"),-55.5,4,36.5,159,-3);
        Vector direction = end.toVector().subtract(location.toVector()).normalize().multiply(2);

        getChasubleManager().spawnChasubles(location, direction);
        this.glassBridge = new GlassBridge(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        setDirectionOfList(getGlassBridgePlayers(),direction);
        Bukkit.broadcastMessage("§7§o[§b§oSquidGame§o§7] §7L'ordre définie est : " + direction.getName());
        getSquidGameManager().getParticipantManager().getParticipants().values().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
           // getSquidGameManager().getWorldsManager().teleport(SquidLocation.START_BRIDGE, player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
        }));

        getSquidGameManager().getSquidGamePlugin().getSidebar().registerAddon(this.bridgeGameAddon);
        getSquidGameManager().getSquidGamePlugin().getSidebar().update();

        //getSquidGameManager().getScoreboardManager().updateScoreBoards(5,"§8▏ §7Ordre : §c" + getDirection().getName());

        new HysekaiRunTaskTimer(getSquidGameManager(),10,0) {
            int time = 0;
            @Override
            public void run() {

                if(getGameStatus() == GameStatus.FINISH || getGlassBridgePlayers().size() == 1) {
                    cancelTask();
                    return;
                }

                if(currentPlayer == null)
                    return;

                if(current + 1 >= getGlassBridgePlayers().size()){
                    cancelTask();
                    return;
                }

                if(!currentPlayer.getParticipantPlayer().isPlaying()) {
                    current++;
                    currentPlayer = getGlassBridgePlayers().get(current).setCanMove(true);
                }

                time++;
                if(time == 15)
                    getGlassBridgePlayers().get(current).getParticipantPlayer().sendActions(player -> player.sendMessage("§cAttention, vous allez être éliminé si vous n'avancez pas..."));

                if(time == 30){

                    time = 0;
                    current++;
                    currentPlayer = getGlassBridgePlayers().get(current).setCanMove(true);
                    if(currentPlayer == null || !currentPlayer.getParticipantPlayer().isPlaying()) {
                        current++;
                        currentPlayer = getGlassBridgePlayers().get(current).setCanMove(true);
                    }

                }

            }
        }.start();

    }

    @Override
    public void onStop() {
        getSquidGameManager().getSquidGamePlugin().getSidebar().unregisterAddon(bridgeGameAddon);
        HandlerList.unregisterAll(this.bridgeListener);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();
            player.removePotionEffect(PotionEffectType.SPEED);
            player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR)});

        });

        super.onStop();


        if(getSquidGameManager().getParticipantManager().getPlayingPlayers().size() == 0){
            getSquidGameManager().finishGame();
        }
    }

    @Override
    public void onEliminate(ParticipantPlayer participantPlayer) {

        super.onEliminate(participantPlayer);
        checkWin();
    }

    @Override
    public void onPlayerQualified(ParticipantPlayer participantPlayer) {
        super.onPlayerQualified(participantPlayer);
        participantPlayer.sendActions(player -> player.removePotionEffect(PotionEffectType.SPEED));
        Optional<GlassBridgePlayer> glassBridgePlayer = getGlassBridgePlayer(participantPlayer);
        if (glassBridgePlayer.isEmpty()) return;

        glassBridgePlayer.get().setQualified(true);
        PlayerUtils.launch(participantPlayer.getPlayer().getLocation().subtract(0,0.2,0), Color.YELLOW, Color.AQUA, Color.GREEN, Color.RED);
        checkWin();

    }

    @Override
    public void onPreTime(int time) {



        if(time == 25){
            setCanChoseChasuble(true);
            getSquidGameManager().getParticipantManager().getPlayingPlayers().forEach(participantPlayer -> participantPlayer.sendActions(player -> {
                HAPI.getUtils().sendTitle(player, 1, 60, 1, "§aChoisissez un chasuble", "");
            }));
        }
        if(time == 5)
            getChasubleManager().randomChasuble();

    }


    /*
   ////////////////////////////////////////////
     */

    public List<GlassBridgePlayer> getGlassBridgePlayers() {return glassBridgePlayers;}
    public Optional<GlassBridgePlayer> isGamePlayer(ParticipantPlayer participantPlayer){ return this.glassBridgePlayers.stream().filter(glassBridgePlayer -> glassBridgePlayer.getParticipantPlayer().equals(participantPlayer)).findFirst(); }
    public ChasubleManager getChasubleManager() { return chasubleManager; }
    public Optional<GlassBridgePlayer> getGlassBridgePlayer(ParticipantPlayer participantPlayer){ return getGlassBridgePlayers().stream().filter(glassBridgePlayer -> glassBridgePlayer.getParticipantPlayer().equals(participantPlayer)).findFirst(); }
    public GlassBridgePlayer getGlassBridgePlayer(Player player) {
        for (GlassBridgePlayer glassBridgePlayer : getGlassBridgePlayers()) {
            if (glassBridgePlayer.getParticipantPlayer().isConnected() && glassBridgePlayer.getParticipantPlayer().getPlayer().equals(player)) return glassBridgePlayer;
        }
        return null;
    }
    public GlassBridge getGlassBridge() { return glassBridge; }
    public Direction getDirection() { return direction; }
    public GlassBridgePlayer getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(GlassBridgePlayer currentPlayer) { this.currentPlayer = currentPlayer; }
    public int getCurrent() { return current; }
    public void setCurrent(int current) { this.current = current; }

    public void checkWin(){
        List<GlassBridgePlayer> playingGlassPlayers = getGlassBridgePlayers().stream().filter(glassBridgePlayer -> glassBridgePlayer.getParticipantPlayer().isPlaying()).collect(Collectors.toList());
        List<GlassBridgePlayer> qualified = playingGlassPlayers.stream().filter(GlassBridgePlayer::isQualified).collect(Collectors.toList());


        if(playingGlassPlayers.size() == qualified.size()){
            setGameStatus(GameStatus.FINISH);
            onStop();
        }
    }

    private List<GlassBridgePlayer> setDirectionOfList(List<GlassBridgePlayer> glassBridgePlayers, Direction direction){
        switch (direction){
            case DECREASE:
                Collections.sort(glassBridgePlayers, Collections.reverseOrder());
                break;
            case INCREASE:
                Collections.sort(glassBridgePlayers);
                break;
        }

        currentPlayer = getGlassBridgePlayers().get(0).setCanMove(true);
        return glassBridgePlayers;
    }

    public Cuboid getFinishZone() { return finishZone; }
    public Cuboid getStartZone() { return startZone; }
    public Cuboid getSpawnZone() { return spawnZone; }

    public boolean isCanChoseChasuble() { return canChoseChasuble; }
    public void setCanChoseChasuble(boolean canChoseChasuble) { this.canChoseChasuble = canChoseChasuble; }

    public BridgeGameAddon getBridgeGameAddon() {return bridgeGameAddon;}
    public void setBridgeGameAddon(BridgeGameAddon bridgeGameAddon) {this.bridgeGameAddon = bridgeGameAddon;}

    public enum Direction{

        INCREASE("§aCroissant"),
        DECREASE("§cDécroissant");

        private final String name;

        Direction(String name){
            this.name = name;
        }

        public String getName() {return name;}
    }
}

