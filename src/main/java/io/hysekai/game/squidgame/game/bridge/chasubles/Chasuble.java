package io.hysekai.game.squidgame.game.bridge.chasubles;

import io.hysekai.game.squidgame.utils.Numbers;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Optional;

public class Chasuble {

    private final int id;
    private boolean isTaken = false;
    private ArmorStand chasuble;
    private final Banner[] numbers = new Banner[2];

    public Chasuble(int id){
        this.id = id;
    }

    public int getId() {return id;}
    public Banner[] getNumbers() {return numbers;}
    public ArmorStand getChasuble() {return chasuble;}
    public void setChasuble(ArmorStand chasuble) {this.chasuble = chasuble;}
    public boolean isTaken() {return isTaken;}
    public void setTaken(boolean taken) {
        isTaken = taken;
        this.chasuble.remove();
        for(int i = 0; i < this.numbers.length; i++){
            this.numbers[i].getLocation().getWorld().getBlockAt(this.numbers[i].getLocation()).setType(Material.AIR);
            this.numbers[i] = null;
        }

        this.setChasuble(null);
    }

    public void setBanners(Vector direction){

        Location armorstand = getChasuble().getLocation().clone().subtract(0,0,0);

        Optional<Numbers> numbers_1 = Numbers.getNumber(this.id % 10);
        numbers_1.ifPresent(numbers1 -> {
            Block block = armorstand.getWorld().getBlockAt(armorstand.clone().add(direction.clone()));
            block.setType(Material.STANDING_BANNER);
            Banner banner = (Banner) block.getState();

            banner.setBaseColor(DyeColor.BLACK);
            banner.setPatterns(numbers1.getPatterns());

            Directional dir = (Directional) block.getState().getData();
            dir.setFacingDirection(BlockFace.NORTH);
            banner.setData((MaterialData) dir);

            banner.update();

            this.numbers[0] = banner;
        });

        Optional<Numbers> numbers_2 = Numbers.getNumber(this.id / 10);
        Block block = armorstand.getWorld().getBlockAt(armorstand.clone().add(direction.clone().multiply(2)));
        block.setType(Material.STANDING_BANNER);
        Banner banner = (Banner) block.getState();
        banner.setBaseColor(DyeColor.BLACK);
        if(numbers_2.isPresent())
            banner.setPatterns(numbers_2.get().getPatterns());
        else
            banner.setPatterns(Numbers.NUMBER_0.getPatterns());


        Directional dir = (Directional) block.getState().getData();
        dir.setFacingDirection(BlockFace.NORTH);
        banner.setData((MaterialData) dir);

        banner.update();
        this.numbers[1] = banner;

    }
}
