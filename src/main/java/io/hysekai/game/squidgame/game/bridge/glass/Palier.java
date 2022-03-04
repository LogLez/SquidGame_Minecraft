package io.hysekai.game.squidgame.game.bridge.glass;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Palier {

    public static final List<Palier> paliers = new ArrayList<>();
    private final int id;
    private PlateForm plateForm1, plateForm2;
    private boolean reveal = false;

    public Palier(int id){
        this.id = id;
        paliers.add(this);
    }

    public List<Palier> getPaliers() { return paliers; }
    public PlateForm getPlateForm1() { return plateForm1; }
    public PlateForm getPlateForm2() { return plateForm2; }
    public void setPlateForm1(PlateForm plateForm1) { this.plateForm1 = plateForm1; }
    public void setPlateForm2(PlateForm plateForm2) { this.plateForm2 = plateForm2; }
    public int getId() { return id; }


    public void revealPlateform(){
        getFakePlateform().setRedPlateform();
        getTruePlateform().setGreenPlateform();
        setReveal(true);
    }

    public PlateForm getTruePlateform(){
        return !getPlateForm1().isFake() ? getPlateForm1() : getPlateForm2();
    }
    public PlateForm getFakePlateform(){
        return getPlateForm1().isFake() ? getPlateForm1() : getPlateForm2();
    }
    public PlateForm isPlayerInside(Location location){

        for(Block block : getPlateForm1().getCuboid().getBlocks()){
            if(location.distance(block.getLocation()) < 1.3)
                return getPlateForm1();

        }
        for(Block block : getPlateForm2().getCuboid().getBlocks()){
            if(location.distance(block.getLocation()) < 1.3)
                return getPlateForm2();

        }

        return null;
    }

    public boolean isReveal() { return reveal; }
    public void setReveal(boolean reveal) { this.reveal = reveal; }
}
