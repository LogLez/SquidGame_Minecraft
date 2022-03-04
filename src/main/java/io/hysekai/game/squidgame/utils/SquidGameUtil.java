package io.hysekai.game.squidgame.utils;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;

public class SquidGameUtil {

    public static TextComponent getClickableTextComponent(String msg, String cmd, String hoverText) {
        TextComponent textComponent = new TextComponent(msg);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,cmd));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).bold(true).create()));
        return textComponent;
    }

    public static ArrayList<org.bukkit.Chunk> getChunks(Location location) {
        ArrayList<org.bukkit.Chunk> chunks = new ArrayList<org.bukkit.Chunk>();

        org.bukkit.Chunk c = location.getChunk();
        Location center = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(7, 0, 7);

        for (int x = -17; x <= 17; x++) {
            for (int z = -17; z <= 17; z++) {
                Location loc = new Location(c.getWorld(), center.getX() + x, center.getY(), center.getZ() + z);
                if (loc.getChunk() != c) {
                    if (!chunks.contains(loc.getChunk())) {
                        chunks.add(loc.getChunk());
                    }
                }
            }
        }
        return chunks;
    }

    public static void loadChunks(ArrayList<org.bukkit.Chunk> chunks) {
        for (Chunk chunk : chunks) {
            if (!chunk.isLoaded()) {
                chunk.load(true);
            }
        }
    }
}
