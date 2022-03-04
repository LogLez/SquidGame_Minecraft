package io.hysekai.game.squidgame.common;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public abstract class HysekaiTask extends BukkitRunnable {

    public static List<HysekaiTask> tasks = new ArrayList<>();

    private long delay;
    private boolean forcestop = false;

    public HysekaiTask(){
        tasks.add(this);
    }
    public abstract BukkitTask start();

    public void cancelTask(){
        this.cancel();
        tasks.remove(this);
    }

    public long getDelay() { return delay; }
    public void setDelay(long delay) { this.delay = delay; }

    public boolean isForcestop() { return forcestop; }
    public void setForcestop(boolean forcestop) { this.forcestop = forcestop; }
}
