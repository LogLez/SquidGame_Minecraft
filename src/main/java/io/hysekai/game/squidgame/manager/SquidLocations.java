package io.hysekai.game.squidgame.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum SquidLocations {

    SPAWN(0,80,0,(float) 90,(float) 1),
    DORTOIR(66.5,80,0.5,(float) -180,(float) 0),
    START_SUN(999.5,80,180.5,(float) 180,(float) 1),
    START_BILLE(2524.5, 80.5, -15.5,(float) 180,(float)-1),
    START_BRIDGE(-72, 46.5, 117,(float) 180,(float)-1),
    WHITE_ROOM(-495.5, 86, -26.5,(float) 0,(float)0);


    final double x,y,z;
    final float yaw, pitch;
    Location location;

    SquidLocations( double x, double y, double z, float yaw, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }


    public double getX() {return x;}
    public double getY() {return y;}
    public double getZ() {return z;}

    public float getYaw() {return yaw;}
    public float getPitch() {return pitch;}

    public Location getLocation() {return location;}

    public void setLocation() {
        this.location = new Location(Bukkit.getWorld("world"), getX(), getY(), getZ(), getYaw(), getPitch());
    }
}