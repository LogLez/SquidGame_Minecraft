package io.hysekai.game.squidgame.game.squidgame;

import io.hysekai.bukkit.api.effect.particle.ParticleType;
import io.hysekai.game.squidgame.game.GameType;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.utils.Cuboid;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import static io.hysekai.game.squidgame.game.squidgame.SquidGameData.CrossingBy.*;
import static io.hysekai.game.squidgame.game.squidgame.SquidGameData.GameLocation.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SquidGame extends AbstractGame implements Listener {

    private Collection<Location> squidHeadCache = new ArrayList<>();
    private final Cuboid squidBody;
    private final Cuboid squidNeckLeftSide;
    private final Cuboid squidNeckMiddleSide;
    private final Cuboid squidNeckRightSide;
    private final Cuboid squidWinningZone;
    private final Cuboid squidEnteringZone;
    private boolean friendlyFire = false;

    public SquidGame(SquidGameManager squidGameManager) {
        super(squidGameManager, GameType.SQUIDGAME,
                "§7§o[§b§oSquidGame§o§7] §7Bienvenue sur le jeu du :§e " + GameType.SQUIDGAME.getName(),
                "§7§o[§b§oSquidGame§o§7] §7Deux équipes seront formées:",
                "§7§o[§b§oSquidGame§o§7] §7Les attaquants, placés en dehors de la zone, devront rentrer dans celle-ci par le corps de la pieuvre, atteindre la gauche du cou et traverser pour le lui couper.",
                "§7§o[§b§oSquidGame§o§7] §7Ils seront ralentis durant cette tâche et récupèreront leur vitesse de course une fois le cou tranché.",
                "§7§o[§b§oSquidGame§o§7] §7Les défenseurs, placés à l'intérieur de la zone, devront expulser à tout prix les attaquants de la zone une fois ceux-ci rentrés dans celle-ci.",
                "§7§o[§b§oSquidGame§o§7] §7Il leur faut cependant éviter de quitter la zone, sous peine de mort instantanée.");

        setSpawnLocation(new Location(Bukkit.getWorld("world"), -68, 5, 150));

        World world = Bukkit.getWorld("world");

        Polygon squidHead = new Polygon();

        Location pos1 = new Location(world, -37, 0, 431); // position 1
        Location pos2 = new Location(world, -59, 0, 431); // position 2
        Location pos3 = new Location(world, -48, 0, 453); // position 3

        squidHead.addPoint(pos1.getBlockX(), pos1.getBlockZ());
        squidHead.addPoint(pos2.getBlockX(), pos2.getBlockZ());
        squidHead.addPoint(pos3.getBlockX(), pos3.getBlockZ());

        Location posMin = pos1.clone();
        Location posMax = pos3.clone();

        posMin.setX(Math.min(pos1.getX(), Math.min(pos2.getX(), pos3.getX())));
        posMin.setY(0);
        posMin.setZ(Math.min(pos1.getZ(), Math.min(pos2.getZ(), pos3.getZ())));

        posMax.setX(Math.max(pos1.getX(), Math.max(pos2.getX(), pos3.getX())));
        posMax.setY(256);
        posMax.setZ(Math.max(pos1.getZ(), Math.max(pos2.getZ(), pos3.getZ())));

        for (int x = posMin.getBlockX(); x <= posMax.getBlockX(); ++x) {
            for (int z = posMin.getBlockZ(); z <= posMax.getBlockZ(); ++z) {
                if (squidHead.contains(x, z)) {
                    for (int y = posMin.getBlockY(); y <= posMax.getBlockY(); ++y) {
                        squidHeadCache.add(new Location(world, x, y, z));
                    }
                }
            }
        }

        for (int y = 0; y <= 256; ++y) {
            squidHeadCache.add(new Location(world, pos1.getBlockX(), y, pos1.getBlockZ()));
            squidHeadCache.add(new Location(world, pos2.getBlockX(), y, pos2.getBlockZ()));
            squidHeadCache.add(new Location(world, pos3.getBlockX(), y, pos3.getBlockZ()));
        }

        squidHeadCache = Collections.unmodifiableCollection(squidHeadCache);

        squidBody = new Cuboid(-36, -60, 10, 0, 426, 405,
                Bukkit.getWorld("world"));

        squidNeckLeftSide = new Cuboid(
                new Location(Bukkit.getWorld("world"), -34, 0, 427),
                new Location(Bukkit.getWorld("world"), -45, 10, 430)
        );

        squidNeckMiddleSide = new Cuboid(
                new Location(Bukkit.getWorld("world"), -50, 0, 427),
                new Location(Bukkit.getWorld("world"), -46, 10, 430)
        );

        squidNeckRightSide = new Cuboid(
                new Location(Bukkit.getWorld("world"), -51, 0, 427),
                new Location(Bukkit.getWorld("world"), -62, 10, 430)
        );

        squidWinningZone = new Cuboid(
                new Location(Bukkit.getWorld("world"), -51, 0, 450),
                new Location(Bukkit.getWorld("world"), -45, 10, 456)
        );

        squidEnteringZone = new Cuboid(
                new Location(Bukkit.getWorld("world"), -51, 0, 402),
                new Location(Bukkit.getWorld("world"), -45, 10, 408)
        );
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Bukkit.getPluginManager().registerEvents(this, squidGameManager.getSquidGamePlugin());

        List<ParticipantPlayer> list = new ArrayList<>(squidGameManager.getParticipantManager().getPlayingPlayers());
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            ParticipantPlayer currentPlayer = list.get(i);
            currentPlayer.setData(this, new SquidGameData(currentPlayer, i % 2 == 0));
        }
    }

    @Override
    public void onEliminate(ParticipantPlayer participantPlayer) {
        super.onEliminate(participantPlayer);
    }

    @Override
    public void onPreTime(int time) {

    }

    @Override
    public void onStart() {
        super.onStart();

        squidGameManager.getParticipantManager().getPlayingPlayers().forEach((player) -> ((SquidGameData) player.getData(this)).teleportToSpawn());
    }

    @Override
    public void onStop() {
        HandlerList.unregisterAll(this);
        super.onStop();
   }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (getGameStatus() != GameStatus.GAME)
            return;
        Location from = event.getFrom();
        Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())
            return;

        Player player = event.getPlayer();
        ParticipantPlayer pplayer = squidGameManager.getParticipantManager().getParticipant(player);
        if (!pplayer.isPlaying())
            return;
        SquidGameData playerData = pplayer.getData(this);
        SquidGameData.GameLocation currentLocation = SquidGameData.GameLocation.OUTSIDE;

        if (squidBody.isInside(event.getTo()))
            currentLocation = SquidGameData.GameLocation.INSIDE_SQUARE;
        else if (squidHeadCache.contains(event.getTo().getBlock().getLocation()))
            currentLocation = SquidGameData.GameLocation.INSIDE_TRIANGLE;
        else if (squidNeckLeftSide.isInside(event.getTo()))
            currentLocation = SquidGameData.GameLocation.INSIDE_NECK_LEFT;
        else if (squidNeckMiddleSide.isInside(event.getTo()))
            currentLocation = SquidGameData.GameLocation.INSIDE_NECK_MIDDLE;
        else if (squidNeckRightSide.isInside(event.getTo()))
            currentLocation = SquidGameData.GameLocation.INSIDE_NECK_RIGHT;
        else if (squidWinningZone.isInside(event.getTo()))
            currentLocation = INSIDE_WIN_ZONE;
        else if (squidEnteringZone.isInside(event.getTo()))
            currentLocation = INSIDE_ENTER_ZONE;

        if (playerData.isAttacker()) {

            if (player.getFoodLevel() <= 6 && currentLocation.is(INSIDE_TRIANGLE, INSIDE_SQUARE) && !playerData.isCrossingBy(NONE)) {
                getSquidGameManager().getParticipantManager().cancelMove(pplayer,
                        null, ParticleType.FLAME, Sound.ENDERMAN_TELEPORT, "§eTraversez ou faites demi-tour !");
                player.setVelocity(to.toVector().subtract(player.getLocation().toVector()).normalize().multiply(-1.5));
                return;
            } else if (playerData.getGameLocation().is(OUTSIDE) && currentLocation.isNot(OUTSIDE)) {
                if (currentLocation.is(INSIDE_ENTER_ZONE)) {
                    player.setFoodLevel(6);
                } else if (currentLocation.is(INSIDE_NECK_LEFT)) {
                    player.setFoodLevel(6);
                    playerData.setCrossingBy(LEFT);
                } else if (currentLocation.is(INSIDE_NECK_RIGHT)) {
                    player.setFoodLevel(6);
                    playerData.setCrossingBy(RIGHT);
                } else {
                    Bukkit.broadcastMessage("Debug 001 " + player.getName());
                    getSquidGameManager().getParticipantManager().cancelMove(pplayer,
                            null, ParticleType.FLAME, Sound.ENDERMAN_TELEPORT, "§eEntrez par la zone verte");
                    player.setVelocity(to.toVector().subtract(player.getLocation().toVector()).normalize().multiply(-1.5));
                    return;
                }
            } else if (playerData.getGameLocation().is(INSIDE_TRIANGLE) && currentLocation.is(INSIDE_WIN_ZONE)) {
                Bukkit.broadcastMessage("SquidGame » " + player.getName() + " a gagné le SquidGame !");
                onPlayerQualified(pplayer);
                squidGameManager.getParticipantManager().getPlayingPlayers().stream().filter((participantPlayer -> participantPlayer != pplayer)).forEach(this::onEliminate);
                onStop();
                PlayerUtils.launch(player.getLocation(), Color.LIME, Color.GREEN);
            } else if (playerData.getGameLocation().isNot(OUTSIDE) && currentLocation.is(OUTSIDE)) {
                if (playerData.getGameLocation().is(INSIDE_ENTER_ZONE)) {
                    player.setFoodLevel(20);
                } else {
                    if ((playerData.isCrossingBy(LEFT) && playerData.getGameLocation().is(INSIDE_NECK_LEFT)) || (playerData.isCrossingBy(RIGHT) && playerData.getGameLocation().is(INSIDE_NECK_RIGHT))) {
                        playerData.setCrossingBy(NONE);
                        player.setFoodLevel(20);
                    } else {
                        player.sendMessage("SquidGame » Vous avez été expulsé de la zone de jeu" + playerData.getCrossingBy() + " " + playerData.getGameLocation() + " " + currentLocation);
                        onEliminate(pplayer);
                    }
                }
            } else if (playerData.getGameLocation().is(INSIDE_NECK_MIDDLE) && currentLocation.is(INSIDE_NECK_RIGHT)) {
                if (playerData.isCrossingBy(LEFT)) {
                    player.setFoodLevel(20);
                    playerData.setCrossingBy(NONE);
                }
            } else if (playerData.getGameLocation().is(INSIDE_NECK_MIDDLE) && currentLocation.is(INSIDE_NECK_LEFT)) {
                if (playerData.isCrossingBy(RIGHT)) {
                    player.setFoodLevel(20);
                    playerData.setCrossingBy(NONE);
                }
            }
        } else if (currentLocation.isNot(INSIDE_SQUARE, INSIDE_TRIANGLE, INSIDE_NECK_MIDDLE, INSIDE_NECK_LEFT, INSIDE_NECK_RIGHT) && !friendlyFire) {
            player.sendMessage("SquidGame » Vous avez été expulsé de la zone de jeu");
            onEliminate(pplayer);
        }

        playerData.setGameLocation(currentLocation);

        if (squidGameManager.getParticipantManager().getPlayingPlayers().size() == 1) {
            onStop();
            return;
        }

        for (ParticipantPlayer participantPlayer : squidGameManager.getParticipantManager().getPlayingPlayers()) {
            if (((SquidGameData) participantPlayer.getData(this)).isAttacker())
                return;
        }

        // NO ATTACKER
        if (!friendlyFire) {
            Bukkit.broadcastMessage("Il ne reste plus aucun attaquant ! Vous pouvez désormais vous battre.");
            friendlyFire = true;
        }
    }
}
