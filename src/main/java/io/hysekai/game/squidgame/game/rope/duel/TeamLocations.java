package io.hysekai.game.squidgame.game.rope.duel;

import io.hysekai.game.squidgame.game.rope.Rope;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class TeamLocations {

    private final Rope rope;
    private final Location spawn;
    private ArmorStand armorStandPlay, armorStandSpectator;

    public TeamLocations(Rope rope, Location teamSpawn){
        this.rope = rope;
        this.spawn = teamSpawn ;

    }

    public void setArmorstands(Location playerLocation, Location spectatorLocation){
        armorStandPlay = (ArmorStand) spawn.getWorld().spawnEntity(playerLocation, EntityType.ARMOR_STAND);
        armorStandSpectator = (ArmorStand) spawn.getWorld().spawnEntity(spectatorLocation, EntityType.ARMOR_STAND);
        armorStandPlay.setGravity(false);
        armorStandPlay.setVisible(false);

        armorStandSpectator.setGravity(false);
        armorStandSpectator.setVisible(false);
    }

    public void setName(String name){
        this.getArmorStandPlay().setCustomName(name);
        this.getArmorStandPlay().setCustomNameVisible(true);
        this.getArmorStandSpectator().setCustomName(name);
        this.getArmorStandSpectator().setCustomNameVisible(true);

    }


    public Location getSpawn() {return spawn;}

    public ArmorStand getArmorStandPlay() {return armorStandPlay;}
    public ArmorStand getArmorStandSpectator() {return armorStandSpectator;}
    public Rope getRope() {return rope;}
}
