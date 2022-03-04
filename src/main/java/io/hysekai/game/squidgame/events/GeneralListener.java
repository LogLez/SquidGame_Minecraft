package io.hysekai.game.squidgame.events;

import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.state.PvPState;
import io.hysekai.game.squidgame.state.SquidGameState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GeneralListener implements Listener {

    private final SquidGameManager squidGameManager;
    public GeneralListener(SquidGameManager squidGameManager){ this.squidGameManager = squidGameManager;}

   @EventHandler
   public void onSpawn(PlayerDeathEvent event){
       event.setDeathMessage(null);
   }

    @EventHandler
    public void onClick(InventoryClickEvent event){
       if(event.getWhoClicked() instanceof  Player && event.getWhoClicked().isOp()) return;
        if(squidGameManager.isSquidGameState(SquidGameState.WAITING)) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event){
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(squidGameManager.isSquidGameState(SquidGameState.WAITING)) event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(squidGameManager.getSquidGameState() != SquidGameState.WAITING) return;
        Location to = event.getTo();
        Player player = event.getPlayer();

        /*if(to.getY() <= 15) {

            if(to.getBlock().getType() != Material.ICE && !to.add(0,-1,0).getBlock().getType().toString().contains("STAINED_GLASS")) return;
            getSquidGameManager().getWorldsManager().teleport(SquidLocation.SPAWN, player);
            HAPI.getUtils().sendAboveHotbarMessage(player, "§cNe vous éloignez pas du bateau...");
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_LAND,5,0);

        }*/
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(squidGameManager.isSquidGameState(SquidGameState.WAITING)) {
            event.setCancelled(true);
            return;
        }

        if(squidGameManager.getMiniGamesManager().getCurrentGame() != null && squidGameManager.getMiniGamesManager().getCurrentGame().getGameStatus() != GameStatus.GAME)
            event.setCancelled(true);


        if(squidGameManager.getPvPState() == PvPState.DENY)
            if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) event.setCancelled(true);

    }

    public SquidGameManager getSquidGameManager() {return squidGameManager;}
}
