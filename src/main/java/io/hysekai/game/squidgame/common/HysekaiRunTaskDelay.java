package io.hysekai.game.squidgame.common;

import io.hysekai.game.squidgame.SquidGameManager;
import org.bukkit.scheduler.BukkitTask;

public abstract class HysekaiRunTaskDelay extends HysekaiTask{

    private SquidGameManager squidGameManager;

    protected HysekaiRunTaskDelay(SquidGameManager squidGameManager) {
        this(squidGameManager, 0,  true);
    }
    protected HysekaiRunTaskDelay(SquidGameManager squidGameManager, double delayInSec) {
        this(squidGameManager, delayInSec, true);
    }

    protected HysekaiRunTaskDelay(SquidGameManager squidGameManager,  double delayInSec, boolean forceStop) {
        this.squidGameManager = squidGameManager;
        this.setDelay((long) delayInSec*20);
        this.setForcestop(forceStop);

    }


    @Override
    public BukkitTask start() {
        return this.runTaskLater(squidGameManager.getSquidGamePlugin(), getDelay());
    }
    public SquidGameManager getSquidGameManager() {return squidGameManager;}

    public static abstract class Asynchronously extends HysekaiRunTaskDelay {

        protected Asynchronously(SquidGameManager squidGameManager, double delay) {
            super(squidGameManager, delay);
        }

        @Override
        public BukkitTask start() {
            return this.runTaskLaterAsynchronously(getSquidGameManager().getSquidGamePlugin(), (long) getDelay());
        }

    }
}
