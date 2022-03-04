package io.hysekai.game.squidgame.game;

import io.hysekai.game.squidgame.game.bille.Bille;
import io.hysekai.game.squidgame.game.bridge.Bridge;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.squidgame.SquidGame;
import io.hysekai.game.squidgame.game.sun.Sun;

public enum GameType {

    SUN(1,"§e","1,2,3 Soleil !", false, true, 20,320, Sun.class),
    //CUT(2,"§7","Jeu du Biscuit",false, false,10,10, Cut.class),
    ROPE(2,"§7","Jeu de la Corde", true, false,40,120, Rope.class),
    BILLE(3,"§d","Jeu des billes", true, false, 10,320, Bille.class),
    BRIDGE(4,"§9","Jeu du pont", false, true, 30,320, Bridge.class),
    SQUIDGAME(5,"","SquidGame", true, true, 10, 320, SquidGame.class);

    private final int id, preTime, time;
    private final String name, color;
    private final boolean isTeam, hasPvP;
    private final Class<? extends AbstractGame> abstractGame;

    GameType(int id, String color, String name, boolean isTeam, boolean hasPvP, int preTime, int time, Class<? extends AbstractGame> abstractGame){
        this.id = id;
        this.name= name;
        this.color = color;
        this.isTeam = isTeam;
        this.hasPvP= hasPvP;
        this.preTime = preTime;
        this.time = time;
        this.abstractGame = abstractGame;
    }

    public int getId() { return id; }
    public String getColor() {return color;}
    public String getName() { return name; }
    public boolean isTeam() { return isTeam; }
    public boolean isHasPvP() { return hasPvP; }

    public int getPreTime() {return preTime;}
    public int getTime() {return time;}

    public Class<? extends AbstractGame> getAbstractGame() { return abstractGame; }


}
