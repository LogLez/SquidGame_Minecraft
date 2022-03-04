package io.hysekai.game.squidgame.game.bridge.chasubles;

import io.hysekai.game.squidgame.game.bridge.Bridge;
import io.hysekai.game.squidgame.game.bridge.GlassBridgePlayer;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.utils.Numbers;
import io.hysekai.game.squidgame.utils.UtilItem;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChasubleManager {

    private final List<Chasuble> chasubles = new ArrayList<>();

    private final Bridge bridge;
    public ChasubleManager(Bridge bridge){
        this.bridge = bridge;
    }

    public Chasuble addChasuble(int id){
        Chasuble chasuble = new Chasuble(id);
        this.chasubles.add(chasuble);
        return chasuble;
    }
    public void removeChasuble(Chasuble chasuble){chasubles.remove(chasuble);}
    public Bridge getBridge() {return bridge;}
    public List<Chasuble> getChasubles() {return chasubles;}

    private void spawnChasuble(Chasuble chasuble, Location location, Vector direction){
        chasuble.setChasuble( (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND));
        chasuble.getChasuble().setGravity(true);
        chasuble.getChasuble().setVisible(true);
        Optional<Numbers> number = Numbers.getNumber(chasuble.getId());
        number.ifPresent(numbers -> {
            UtilItem itemStack = new UtilItem().createItem(Material.LEATHER_CHESTPLATE).setName(number.get().getData()).setArmorColor(Color.WHITE);
            chasuble.getChasuble().setChestplate(itemStack);
        });

        chasuble.setBanners(direction);

    }
    public boolean isChasuble(ArmorStand armorStand){
        for(Chasuble chasuble : chasubles){
            if(chasuble.getChasuble() != null && chasuble.getChasuble().equals(armorStand))return true;
        }
        return false;
    }
    public Chasuble getChasuble(ArmorStand armorStand){
        for(Chasuble chasuble : chasubles){
            if(chasuble.getChasuble() != null && chasuble.getChasuble().equals(armorStand))return chasuble;
        }
        return null;
    }

    public boolean setChasubleToPlayer(ParticipantPlayer participantPlayer, Chasuble chasuble){
        Optional<GlassBridgePlayer> glassBridgePlayer = getBridge().isGamePlayer(participantPlayer);
        if(!glassBridgePlayer.isPresent())
            return false;

        glassBridgePlayer.get().setChasuble(chasuble);
        return true;
    }
    public boolean setChasubleToPlayer(GlassBridgePlayer glassBridgePlayer, Chasuble chasuble){
        glassBridgePlayer.setChasuble(chasuble);
        return true;
    }

    public void randomChasuble(){
        for(GlassBridgePlayer glassBridgePlayer : getBridge().getGlassBridgePlayers()){
            if (glassBridgePlayer.hasChasuble())
                continue;



            secondLoop : {
                for(Chasuble chasuble : getBridge().getChasubleManager().getChasubles()){
                    if(chasuble.isTaken())
                        continue;

                    glassBridgePlayer.setChasuble(chasuble);
                    break secondLoop;
                }
            }
        }
        getBridge().getSquidGameManager().getMessageUtils().sendBroadcast("§7§oTous les participants ont choisi leur chasuble...");
        getBridge().getSquidGameManager().getMessageUtils().sendBroadcast("§7§oLe jeu va debuter");

    }


    //TODO ligne de chasubles | faire plusieurs lignes de
    // chasubles pour qu'elle puissent rentréer dans le build
    //Il y a quune ligne pour le mment qui espace les chasubles de 2 blocks de longs

    public void spawnChasubles(Location location, Vector direction){

        new BukkitRunnable() {
            int i = 1;
            @Override
            public void run() {

                for(ParticipantPlayer participantPlayer : getBridge().getSquidGameManager().getParticipantManager().getPlayingPlayers()){
                    participantPlayer.sendActions(participantPlayer1 -> participantPlayer1.getPlayer().sendMessage("Debug 001 "));

                    Chasuble chasuble = addChasuble(i);
                    spawnChasuble(chasuble, location, direction.clone().multiply(-0.5));
                    location.add(direction.clone().multiply(1.5));
                    getBridge().getGlassBridgePlayers().add(new GlassBridgePlayer(getBridge(), participantPlayer));

                    i++;
                    if(i == 25) return;
                }

            }
        }.runTaskLater(getBridge().getSquidGameManager().getSquidGamePlugin(),40);
    }

    public int getPlayerWithChasuble(){
        int i = 0;
        for(GlassBridgePlayer glassBridgePlayer : getBridge().getGlassBridgePlayers())
            if(glassBridgePlayer.hasChasuble()) i++;

        return i;
    }

}
