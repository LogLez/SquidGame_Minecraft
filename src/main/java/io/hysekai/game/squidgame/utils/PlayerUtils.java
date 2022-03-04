package io.hysekai.game.squidgame.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class PlayerUtils {

    public static void launch(Location l, Color... color){
        Firework fw = l.getWorld().spawn(l,Firework.class);
        FireworkMeta meta = fw.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BALL).trail(false).withColor(color).build());
        fw.setFireworkMeta(meta);
        ((CraftFirework)fw).getHandle().expectedLifespan = 1;


    }
}
