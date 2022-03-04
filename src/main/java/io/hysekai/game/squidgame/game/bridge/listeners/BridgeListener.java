package io.hysekai.game.squidgame.game.bridge.listeners;

import io.hysekai.bukkit.api.effect.particle.ParticleType;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.bridge.Bridge;
import io.hysekai.game.squidgame.game.bridge.GlassBridgePlayer;
import io.hysekai.game.squidgame.game.bridge.glass.Palier;
import io.hysekai.game.squidgame.game.bridge.glass.PlateForm;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class BridgeListener implements Listener {

    protected final Bridge bridge;
    public BridgeListener(Bridge bridge) { this.bridge = bridge; }

   /* @EventHandler
    public void onDeath(PlayerInteractEvent event) {
        event.setCancelled(true);
    }*/
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ParticipantPlayer participantPlayer = getBridge().getSquidGameManager().getParticipantManager().getParticipant(event.getEntity());
        if (participantPlayer == null) return;
        event.getEntity().setHealth(20);
        getBridge().onEliminate(participantPlayer);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (getBridge().getGlassBridge() == null) return;
        if( getBridge().getGameStatus() != GameStatus.PREGAME && getBridge().getGameStatus() != GameStatus.GAME) return;

        Player player = event.getPlayer();
        Location to = event.getTo();
        ParticipantPlayer participantPlayer = getBridge().getSquidGameManager().getParticipantManager().getParticipant(player);
        if (participantPlayer == null || !participantPlayer.isPlaying()) return;

        Optional<GlassBridgePlayer> glassBridgePlayer = bridge.getGlassBridgePlayer(participantPlayer);
        if (!glassBridgePlayer.isPresent()) return;

        if (getBridge().getGameStatus() == GameStatus.PREGAME && !getBridge().isCanChoseChasuble() ) {
            if (!getBridge().getSpawnZone().isInside(event.getTo()))
                getBridge().getSquidGameManager().getParticipantManager().cancelMove(participantPlayer, getBridge().getSpawnLocation(), ParticleType.FLAME, Sound.ENDERMAN_TELEPORT, "§cVeuillez attendre pour choisir votre chasuble...");

            return;
        }

        if (getBridge().getGameStatus() != GameStatus.GAME) return;

        if (!glassBridgePlayer.get().isCanMove()) {
            //if (!getBridge().getStartZone().isInside(event.getTo())) getBridge().getSquidGameManager().getParticipantManager().cancelMove(
            //        participantPlayer, SquidLocation.START_BRIDGE.getLocation(), ParticleType.FLAME, Sound.ENDERMAN_TELEPORT, "§cVeuillez attendre votre tour...");
            return;
        }

        if(glassBridgePlayer.get().isQualified()){
            if (!getBridge().getFinishZone().isInside(event.getFrom()))
                getBridge().getSquidGameManager().getParticipantManager().cancelMove(participantPlayer, new Location(to.getWorld(),-1151,47, - 131), ParticleType.FLAME, Sound.ENDERMAN_TELEPORT,"Vous êtes qualifié, n'allez pas plus loin");
            return;
        }

        if(getBridge().getFinishZone().isInside(to) && !glassBridgePlayer.get().isQualified()){
            getBridge().onPlayerQualified(participantPlayer);
            return;
        }


        for (Palier palier : getBridge().getGlassBridge().getPaliers()) {

            PlateForm plateForm = palier.isPlayerInside(to);
            if (plateForm == null ) continue;
            if (plateForm.isFake())
                plateForm.cleanPlateForm();

            if(palier.isReveal()) continue;
            palier.setReveal(true);
            if (getBridge().getCurrent() < getBridge().getGlassBridgePlayers().size() - 1) {
                getBridge().setCurrent(getBridge().getCurrent() + 1);
                getBridge().setCurrentPlayer( getBridge().getGlassBridgePlayers().get(getBridge().getCurrent()).setCanMove(true) );
            }


            return;

        }
    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if(getBridge().getGameStatus() == GameStatus.FINISH || getBridge().getGameStatus() == GameStatus.PREGAME ) event.setCancelled(true);
        if (!(event.getDamager() instanceof Player) ) return;
        GlassBridgePlayer glassBridgePlayer = getBridge().getGlassBridgePlayer((Player) event.getDamager());
        if (glassBridgePlayer == null || !glassBridgePlayer.getParticipantPlayer().isPlaying()) return;
        if(!(event.getEntity() instanceof ArmorStand)) return;

        if(!getBridge().getChasubleManager().isChasuble((ArmorStand) event.getEntity())) return;
        event.setCancelled(true);
        getBridge().getChasubleManager().setChasubleToPlayer(glassBridgePlayer, getBridge().getChasubleManager().getChasuble((ArmorStand) event.getEntity()));
    }

    @EventHandler
    public void onDamageEntity(InventoryClickEvent event) {
        event.setCancelled(true);
    }
    @EventHandler
    public void onDamageEntity2(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof  Player)) return;
        ParticipantPlayer damagerParticipant = getBridge().getSquidGameManager().getParticipantManager().getParticipant((Player) event.getDamager());
        ParticipantPlayer victimParticipant = getBridge().getSquidGameManager().getParticipantManager().getParticipant((Player) event.getEntity());

        if (damagerParticipant == null || !damagerParticipant.isPlaying()) return;
        if (victimParticipant == null || !victimParticipant.isPlaying()) return;

        GlassBridgePlayer victimGlassBridgePlayer = getBridge().getGlassBridgePlayer((Player) event.getEntity());
        GlassBridgePlayer damagerGlassBridgePlayer = getBridge().getGlassBridgePlayer((Player) event.getDamager());

        if(victimGlassBridgePlayer == null || damagerGlassBridgePlayer == null) return;

        if(victimGlassBridgePlayer.isQualified() || !victimGlassBridgePlayer.isCanMove() ||
                damagerGlassBridgePlayer.isQualified() || !damagerGlassBridgePlayer.isCanMove()) event.setCancelled(true);


    }

    @EventHandler
    public void onClickEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        ParticipantPlayer participantPlayer = getBridge().getSquidGameManager().getParticipantManager().getParticipant(player);
        if (participantPlayer == null || !participantPlayer.isPlaying() || !(event.getRightClicked() instanceof ArmorStand))
            return;

        event.setCancelled(true);
        if (!getBridge().getChasubleManager().isChasuble((ArmorStand) event.getRightClicked())) return;
        getBridge().getChasubleManager().setChasubleToPlayer(participantPlayer, getBridge().getChasubleManager().getChasuble((ArmorStand) event.getRightClicked()));

    }

    public Bridge getBridge() { return bridge; }
}
