package io.hysekai.game.squidgame.manager;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.effect.particle.ParticleBuilder;
import io.hysekai.bukkit.api.effect.particle.ParticleType;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ParticipantManager {

    private final SquidGameManager squidGameManager;
    private final Map<UUID, ParticipantPlayer> participants = new HashMap<>();

    public ParticipantManager(SquidGameManager squidGameManager) {
        this.squidGameManager = squidGameManager;

    }

    public ParticipantPlayer addParticipantPlayer(Player player){
        if (!this.isParticipant(player)){
            ParticipantPlayer participantPlayer =  new ParticipantPlayer(player);
            //squidGameManager.getScoreboardManager().add(participantPlayer);
            this.participants.put(player.getUniqueId(), participantPlayer);
            return participantPlayer;
        }
        return getParticipant(player);
    }

    public void addParticipant(Player player, ParticipantPlayer participantPlayer) {
        if (!this.isParticipant(player))
            this.participants.put(player.getUniqueId(), participantPlayer);
    }

    public void removeParticipant(Player player) {
        if (this.isParticipant(player))
            this.participants.remove(player.getUniqueId());
    }

    public boolean isParticipant(Player player) {
        return this.participants.containsKey(player.getUniqueId());
    }

    public ParticipantPlayer getParticipant(Player player) {
        return this.participants.get(player.getUniqueId());
    }

    public List<ParticipantPlayer> getPlayingPlayers(){
        return getParticipants().values().stream().filter(ParticipantPlayer::isPlaying).collect(Collectors.toList());
    }

    public void cancelMove(ParticipantPlayer participantPlayer, Location location, ParticleType particleType, Sound sound, String msg){
        participantPlayer.sendActions(player -> {
            HAPI.getUtils().sendAboveHotbarMessage(player, msg);
            if (location != null)
                HAPI.getUtils().teleportPlayer(player, location);
            player.playSound(player.getLocation(), sound, 5, 5);
            for (int i = 0; i < 10; i++) {
                Random r = new Random();
                double x = Math.cos(new Random().nextDouble() * (2 * Math.PI - 0) + 0) * (r.nextDouble() * (1.3 - (-1.3)) + (-1.3));
                double y = Math.random() * ((1.0 - (0)) + 1.0) + (-0);
                double z = Math.cos(new Random().nextDouble() * (2 * Math.PI - 0) + 0) * (r.nextDouble() * (1.3 - (-1.3)) + (-1.3));

                Location playerLocation = player.getLocation();

                ParticleBuilder particle = new ParticleBuilder(particleType).count(1);
                particle.spawn(playerLocation.clone().add(x, y, z));
                particle.spawn(playerLocation.clone().add(x, y, z - 2));
            }
        });
    }

    // Si besoin d'utiliser un vecteur, ne pas dupliquer de code mais ajuster la méthode ci-dessus pour supporter un vecteur en +
    /*public void cancelMove(ParticipantPlayer participantPlayer, Vector vector, ParticleEffect particleEffect, Sound sound, String msg){
        participantPlayer.sendActions(player -> {
            HAPI.getUtils().sendAboveHotbarMessage(player, msg);
            player.setVelocity(vector);
            player.playSound(player.getLocation(), sound, 5, 5);
            for (int i = 0; i < 10; i++) {
                Random r = new Random();
                double x = Math.cos(new Random().nextDouble() * (2 * Math.PI - 0) + 0) * (r.nextDouble() * (1.3 - (-1.3)) + (-1.3));
                double y = Math.random() * ((1.0 - (0)) + 1.0) + (-0);
                double z = Math.cos(new Random().nextDouble() * (2 * Math.PI - 0) + 0) * (r.nextDouble() * (1.3 - (-1.3)) + (-1.3));
                particleEffect.display(0.0F, 0.0f, 0.0f, 0.0f, 1, player.getPlayer().getLocation().add(x, y, z), 10);
                particleEffect.display(0.0F, 0.0f, 0.0f, 0.0f, 1, player.getPlayer().getLocation().add(x, y, z - 2), 10);

            }
        });
    }*/

    public Map<UUID, ParticipantPlayer> getParticipants() { return participants; }
    public SquidGameManager getSquidGameManager() {
        return squidGameManager;
    }

}
