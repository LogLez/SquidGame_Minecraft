package io.hysekai.game.squidgame.game.bridge.glass;

import io.hysekai.game.squidgame.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

public class PlateForm {

    private final Cuboid cuboid;
    private final boolean isFake;
    private boolean isDelete = false;

    public PlateForm( Location start, Vector right, Vector foward, boolean isFake){
        this.cuboid = new Cuboid(start, start.clone().add(right).add(foward));
        this.isFake = isFake;
    }

    public Cuboid getCuboid() { return cuboid; }
    public boolean isFake() { return isFake; }

    public void cleanPlateForm(){
        if(isDelete()) return;

        setDelete(true);
        getCuboid().getBlocks().forEach(block -> {
            block.getWorld().getBlockAt(block.getLocation().subtract(0,1,0)).setType(Material.AIR);
            block.getWorld().playSound(block.getLocation(), Sound.GLASS,5,5);
        });
    }

    public void setRedPlateform(){
        if(isDelete()) return;

        getCuboid().getBlocks().forEach(block -> {
            block.getWorld().getBlockAt(block.getLocation().clone().subtract(0,1,0)).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte)14,false);
        });
    }
    public void setGreenPlateform(){
        if(isDelete()) return;

        getCuboid().getBlocks().forEach(block -> {
            block.getWorld().getBlockAt(block.getLocation().clone().subtract(0,1,0)).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte)5,false);
        });
    }

    public boolean isDelete() { return isDelete; }
    public void setDelete(boolean delete) { isDelete = delete; }
}
