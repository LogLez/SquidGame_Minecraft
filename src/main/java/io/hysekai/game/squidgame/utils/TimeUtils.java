package io.hysekai.game.squidgame.utils;

import io.hysekai.game.squidgame.SquidGameManager;

public class TimeUtils {

    private final SquidGameManager squidGameManager;

    public TimeUtils(SquidGameManager squidGameManager){
        this.squidGameManager = squidGameManager;
    }

    public String formatedTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String disMinu = (minutes < 10 ? "0" : "") + minutes;
        String disSec = (seconds < 10 ? "0" : "") + seconds;
        return disMinu + ":" + disSec;
    }
    public String formatedTimeInString(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String disMinu = (minutes == 0 ? "" : minutes + "m") ;
        String disSec = ""+ seconds + "s";
        return disMinu + " " + disSec;
    }

    public char getNumber(String time, int i){
        return time.charAt(i);
    }
    public SquidGameManager getSquidGameManager() { return squidGameManager; }
}
