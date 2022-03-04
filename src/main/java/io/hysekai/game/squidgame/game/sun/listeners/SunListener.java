package io.hysekai.game.squidgame.game.sun.listeners;

import io.hysekai.bukkit.api.effect.particle.ParticleType;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.sun.Sun;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SunListener implements Listener {
    
    private  final Sun sun;
    public SunListener(Sun sun){ this.sun = sun;}

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        if(getSun().getGameStatus() != GameStatus.GAME) return;

        ParticipantPlayer participantPlayer = getSun().getSquidGameManager().getParticipantManager().getParticipant(event.getPlayer());

        if(participantPlayer == null|| !participantPlayer.isPlaying() || getSun().getQualifiedParticipantPlayer().contains(participantPlayer)) return;
        if(!getSun().isCanMove()) getSun().getGuardian().onWeaponsShoot(participantPlayer, getSun().getGuardian().getNeabyWeapon(event.getPlayer().getLocation()));

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(getSun().getGameStatus() != GameStatus.GAME) return;

        ParticipantPlayer participantPlayer = getSun().getSquidGameManager().getParticipantManager().getParticipant(event.getPlayer());
        Action action = event.getAction();

        if( participantPlayer == null || !participantPlayer.isPlaying() || getSun().getQualifiedParticipantPlayer().contains(participantPlayer)) return;
        if(action != Action.LEFT_CLICK_AIR && action != Action.LEFT_CLICK_BLOCK) return;

        if(!getSun().isCanMove()) getSun().getGuardian().onWeaponsShoot(participantPlayer, getSun().getGuardian().getNeabyWeapon(event.getPlayer().getLocation()));


    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(getSun().getGameStatus() != GameStatus.GAME) return;
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof Player)) return;
        ParticipantPlayer damager = getSun().getSquidGameManager().getParticipantManager().getParticipant((Player) event.getDamager());
        if(damager == null || !damager.isPlaying() || getSun().getQualifiedParticipantPlayer().contains(damager)) return;

        int cooldownTime = 15;

        if(getSun().getCountdownPunch().containsKey(damager)) {
            long secondsLeft = ((getSun().getCountdownPunch().get(damager)/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
            if(secondsLeft>0) {
                event.setCancelled(true);
                getSun().getSquidGameManager().getMessageUtils().sendMessageToPlayer((Player) event.getDamager(), "§7§oAttendez " + secondsLeft + " secondes avant de pousser une personne de nouveau!");
                return;
            }
        }
        getSun().getCountdownPunch().put(damager, System.currentTimeMillis());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        ParticipantPlayer participantPlayer = getSun().getSquidGameManager().getParticipantManager().getParticipant(((Player) event.getEntity()).getPlayer());
        if(participantPlayer == null) return;

        if(getSun().getQualifiedParticipantPlayer().contains(participantPlayer))
             event.setCancelled(true);

    }


    @EventHandler
    public void onMove(PlayerMoveEvent event){

        Player player = event.getPlayer();
        ParticipantPlayer participantPlayer = getSun().getSquidGameManager().getParticipantManager().getParticipant(player);
        if(participantPlayer == null || !participantPlayer.isPlaying() )
            return;


        if(getSun().getGameStatus() == GameStatus.PREGAME){
            if(event.getTo().getZ() <= 151){

                getSun().getSquidGameManager().getParticipantManager().cancelMove(participantPlayer,
                        player.getLocation().add(0,0.5,2), ParticleType.FLAME, Sound.ENDERMAN_TELEPORT, "§cAttendez le début de la partie...");
            }

            return;
        }

        if(getSun().getGameStatus() != GameStatus.GAME) return;
        if( getSun().getQualifiedParticipantPlayer().contains(participantPlayer)){
            if(event.getTo().getZ() >= 2) {
                getSun().getSquidGameManager().getParticipantManager().cancelMove(participantPlayer,
                        player.getLocation().subtract(0, 0, 2), ParticleType.FLAME, Sound.ENDERMAN_TELEPORT, "§cVous êtes qualifié, n'allez pas plus loin...");

            }
            return;
        }

        if(!getSun().isCanMove()) {
            Location currentWeapon = null;
            if(getSun().getGuardian().getWeapon1().distance(player.getLocation()) < getSun().getGuardian().getWeapon2().distance(player.getLocation()))
                currentWeapon = getSun().getGuardian().getWeapon1();
            else
                currentWeapon = getSun().getGuardian().getWeapon2();

            getSun().getGuardian().onWeaponsShoot(participantPlayer, currentWeapon);

        }else{
            if(!getSun().getBattlefield().isInside(event.getTo())) {
                getSun().onPlayerQualified(participantPlayer);
            }
        }
    }




    public Sun getSun(){ return sun;}
}
