package io.hysekai.game.squidgame.common;

import io.hysekai.game.squidgame.SquidGameManager;
import org.bukkit.scheduler.BukkitTask;

public abstract class HysekaiRunTaskTimer extends HysekaiTask{

    private SquidGameManager plugin;
    private double ticks = 0;

    protected HysekaiRunTaskTimer(SquidGameManager plugin) {
        this(plugin, 20, 0, true);
    }
    protected HysekaiRunTaskTimer(SquidGameManager plugin, double ticks) {
        this(plugin, ticks, 0, true);
    }
    protected HysekaiRunTaskTimer(SquidGameManager plugin, double ticks, long delay) {
        this(plugin, ticks, delay, true);
    }

    protected HysekaiRunTaskTimer(SquidGameManager plugin, double ticks, double delay, boolean forceStop) {
        this.plugin = plugin;
        this.setDelay((long) delay);
        this.ticks = ticks;
        this.setForcestop(forceStop);
    }


    @Override
    public BukkitTask start() {
        return this.runTaskTimer(plugin.getSquidGamePlugin(), getDelay(), (long) ticks);
    }


    public SquidGameManager getPlugin() {return plugin;}
    public double getTicks() { return ticks; }

    public void setTicks(double ticks) { this.ticks = ticks; }

    public static abstract class Asynchronously extends HysekaiRunTaskTimer {

        protected Asynchronously(SquidGameManager squidGameManager, double period) {
            super(squidGameManager, period, 0);
        }

        protected Asynchronously(SquidGameManager squidGameManager, double period, long delay) {
            super(squidGameManager, period, delay);
        }

        @Override
        public BukkitTask start() {
            return this.runTaskTimerAsynchronously(getPlugin().getSquidGamePlugin(),   getDelay(), (long)getTicks());
        }

    }

}
