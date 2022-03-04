package io.hysekai.game.squidgame.game.bridge.glass;

import io.hysekai.game.squidgame.game.bridge.Bridge;
import io.hysekai.game.squidgame.manager.WorldsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GlassBridge {

    private final Bridge bridge;
    private final List<Palier> paliers = new ArrayList<>();
    private final Location startLeft = new Location(Bukkit.getWorld("world"),-76, 46, 102);
    private final Location startRight = new Location(Bukkit.getWorld("world"),  -75, 46, 102);
    private final Location secondPlate = new Location(Bukkit.getWorld("world"),-76, 46, 101);

    public GlassBridge(Bridge bridge){

        this.bridge = bridge;
        createBridge();
    }

    public void createBridge(){

        Location start = startLeft.clone();
        Vector rightDirection = startRight.toVector().subtract(startLeft.toVector()).normalize();
        Vector fowardDirection = secondPlate.toVector().subtract(startLeft.toVector()).normalize();

        Random r = new Random();
        boolean isLeftFake;

        for(int i = 1 ; i <17;i++){
            isLeftFake = r.nextBoolean();

            Palier palier = new Palier(i);
            if(isLeftFake){
                palier.setPlateForm1(createPlateforme(start,rightDirection.clone(),fowardDirection.clone(), true));
                start.add(rightDirection.clone().multiply(5));
                palier.setPlateForm2(createPlateforme(start, rightDirection.clone(),fowardDirection.clone(),false));
                start.add(rightDirection.clone().multiply(-5));
            }else{
                palier.setPlateForm1(createPlateforme(start,rightDirection.clone(),fowardDirection.clone(), false));
                start.add(rightDirection.clone().multiply(5));
                palier.setPlateForm2(createPlateforme(start, rightDirection.clone(),fowardDirection.clone(),true));
                start.add(rightDirection.clone().multiply(-5));
            }

            this.paliers.add(palier);
            start = start.add(fowardDirection.clone().multiply(4));
        }

        showGlassFake(bridge.getSquidGameManager().getParticipantManager().getPlayingPlayers().size());
    }
    public Optional<Palier> getPalier(int id){
        return getPaliers().stream().filter(palier -> palier.getId() == id).findFirst();
    }

    public void showGlassFake(int players){

        if(players < 16){

            for(int i = 17;i >players;i--){
                Optional<Palier> optionnalPalier = getPalier(i);
                optionnalPalier.ifPresent(Palier::revealPlateform);
            }
        }

    }

    private PlateForm createPlateforme(Location start, Vector right, Vector foward, boolean isFake){
        PlateForm plateForm = new PlateForm(start,right,foward, isFake);
        for (Block block : plateForm.getCuboid().getBlocks()) {
            start.getWorld().getBlockAt(block.getLocation().subtract(0,1,0)).setType(Material.GLASS);
        }
        return plateForm;
    }

    public List<Palier> getPaliers() { return paliers; }
}
