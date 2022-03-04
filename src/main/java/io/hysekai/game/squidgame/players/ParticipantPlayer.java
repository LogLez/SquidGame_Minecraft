package io.hysekai.game.squidgame.players;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.game.AbstractGame;

public class ParticipantPlayer {

    protected final UUID uuid;

    private final Player player;
    private ParticipantState state;
    private Map<AbstractGame, Object> data;


    public ParticipantPlayer(Player player, ParticipantState participantState) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.state = participantState;
        cleanUpPlayer(ParticipantState.ALIVE);
    }

    public void qualified() {
        sendActions(player -> {
            player.playSound(player.getLocation(), Sound.LEVEL_UP,5,5);
            HAPI.getUtils().sendAboveHotbarMessage(player,"§a§k!§r §2§lVICTOIRE §a§k!");
            HAPI.getUtils().sendTitle(player, 1,40,1,"§a✔","");
        });
    }
    public void eliminate() {
        setState(ParticipantState.DEATH);
        sendActions(player -> {
            player.setHealth(20);
            player.setGameMode(GameMode.SPECTATOR);
            HAPI.getUtils().sendTitle(player,1,30,1,"§cEliminé !" ,"");
        });

    }
    public void cleanUpPlayer() {
        this.cleanUpPlayer(getState());
    }

    public void cleanUpPlayer(ParticipantState state) {
        setState(state);


        if (!isConnected()) return;

        if(state == ParticipantState.ALIVE)
            player.setGameMode(GameMode.SURVIVAL);
        if(state == ParticipantState.DEATH)
            player.setGameMode(GameMode.SPECTATOR);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.setTotalExperience(0);
        player.setExp(0);
        player.setLevel(0);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));


        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().clear();

        player.setFireTicks(0);

    }


    public ParticipantPlayer(Player player ){
        this(player,ParticipantState.ALIVE);
    }

    public UUID getUuid() {return uuid;}
    public Player getPlayer() {return player;}
    public boolean isConnected(){ return this.player.isOnline(); }

    public boolean sendActions(Consumer<? super Player> actions){
        if(isConnected()) {
            actions.accept(player);
            return true;
        }
        return false;
    }


    public ParticipantState getState() { return state; }

    public void setState(ParticipantState state) { this.state = state; }

    public boolean isPlaying(){ return this.state == ParticipantState.ALIVE; }

    public void setData(AbstractGame game, Object data) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(game, data);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(AbstractGame game) {
        if (data == null) {
            return null;
        }
        return (T) data.get(game);
    }


}
