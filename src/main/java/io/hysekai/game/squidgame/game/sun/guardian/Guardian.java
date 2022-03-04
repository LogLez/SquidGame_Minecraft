package io.hysekai.game.squidgame.game.sun.guardian;

import io.hysekai.bukkit.api.effect.particle.ParticleBuilder;
import io.hysekai.bukkit.api.effect.particle.ParticleType;
import io.hysekai.game.squidgame.game.sun.Sun;
import io.hysekai.game.squidgame.game.sun.tasks.GuardianTask;
import io.hysekai.game.squidgame.manager.WorldsManager;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Guardian  {

    private static final ParticleBuilder WEAPON_SHOOT_PARTICLE = new ParticleBuilder(ParticleType.REDSTONE).count(1).radius(128);

    private final Sun sun;
    private final Location spawnGuardian;

    private final Location weapon1 = new Location(Bukkit.getWorld("world"), -1202, 10, -269);
    private final Location weapon2 = new Location(Bukkit.getWorld("world"), -1110, 10, -269);

    private Minecart guardian;
    private Speed speed = Speed.SLOW;
    private GuardianTask guardianTask;


    public Guardian(Sun sun, Location location) {
        this.sun = sun;
        this.spawnGuardian = location;
    }

    public Location getNeabyWeapon(Location targetLocation) {
        return targetLocation.distance(weapon1) < targetLocation.distance(weapon2) ? weapon1 : weapon2;
    }

    public void onWeaponsShoot(ParticipantPlayer participantPlayer, Location weapon) {
        if (!participantPlayer.isConnected()) return;
        Player player = participantPlayer.getPlayer();
        Location deathLocation = player.getLocation().add(0, 1.5, 0);
        Location shootLocation = weapon.clone();

        Vector increase = deathLocation.toVector().subtract(weapon.toVector()).normalize().multiply(0.2);

        while (shootLocation.distance(deathLocation) > 1) {
            shootLocation.add(increase);
            WEAPON_SHOOT_PARTICLE.spawn(shootLocation);
        }

        player.getWorld().playSound(player.getLocation(), Sound.BLAZE_HIT, 5, 5);
        getSun().onEliminate(participantPlayer);
    }

    public void spawnGuardian() {
        //TODO SPAWN GUARDIAN
    }

    public void startGuardian() {
        if (getGuardianTask() != null) getGuardianTask().cancel();
        setGuardianTask(new GuardianTask(this,getSpeed().getPeriod()));
        getGuardianTask().start();
    }

    public void stopGuardian() {
        if (this.getSun().getGuardian().getGuardian() != null) this.getSun().getGuardian().getGuardian().remove();
        if (getGuardianTask() != null) getGuardianTask().cancelTask();
        this.setGuardian(null);
        this.setGuardianTask(null);

    }

    public Sun getSun() {
        return sun;
    }

    public Location getSpawnGuardian() {
        return spawnGuardian;
    }

    public Entity getGuardian() {
        return guardian;
    }

    public void setGuardian(Minecart guardian) {
        this.guardian = guardian;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Speed getSpeed() {
        return speed;
    }

    public Location getWeapon1() {
        return weapon1;
    }

    public Location getWeapon2() {
        return weapon2;
    }

    public GuardianTask getGuardianTask() {
        return guardianTask;
    }

    public void setGuardianTask(GuardianTask guardianTask) {
        this.guardianTask = guardianTask;
    }

    public enum Speed {

        SLOW(21, Sound.BAT_TAKEOFF), MIDDLE(13, Sound.BLAZE_BREATH), FAST(10, Sound.BAT_DEATH);

        private static int i = 0;
        private final int period;
        private final Sound sound;

        Speed(int period, Sound sound) {
            this.period = period;
            this.sound = sound;
        }

        public Sound getSound() {
            return sound;
        }

        public int getPeriod() {
            return period;
        }

        public Speed upgrade() {
            i++;
            if (i < 3) return SLOW;
            if (i < 6) return MIDDLE;

            return FAST;
        }


    }
}
