package io.hysekai.game.squidgame.utils;

import io.hysekai.game.squidgame.players.ParticipantPlayer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface CustomInventory {

    public static Map<Class<? extends CustomInventory>, CustomInventory> registeredMenus = new HashMap<>();

    public abstract String name();

    public abstract void contents(ParticipantPlayer participantPlayer, Inventory inv);


    public abstract void onClick(ParticipantPlayer participantPlayer, InventoryView inv, ItemStack current, int slot);

    public abstract void onClose(ParticipantPlayer participantPlayer,  InventoryView inv);

    public abstract int getSize();

    public static void addMenu(CustomInventory m){ registeredMenus.put(m.getClass(), m); }

    public static void open(ParticipantPlayer participantPlayer, Class<? extends CustomInventory> gClass){

        if(!registeredMenus.containsKey(gClass)) return;
        CustomInventory menu = registeredMenus.get(gClass);
        Inventory inv  = Bukkit.createInventory(null, menu.getSize(), menu.name());
        menu.contents(participantPlayer, inv);
        participantPlayer.sendActions(player -> player.openInventory(inv));
    }
}

