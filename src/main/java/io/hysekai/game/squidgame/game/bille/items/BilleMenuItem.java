package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;

public class BilleMenuItem extends MenuItem {

    private PariGui pariGui;
    private int slot;
    private boolean pari = false;

    public BilleMenuItem(PariGui pariGui, int slot, String texture) {
        super("§aBille", new CustomHead(texture));
        this.pariGui = pariGui;
        this.slot = slot;
    }

    @Override
    public void onClick(MenuClickAction action) {
        if (action.getMenuSlot() < 17)
            pariGui.move(this, true);
        else 
            pariGui.move(this, false);
    }

    public int getSlot() {
        return slot;
    }

    public BilleMenuItem setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public boolean isPari() {
        return pari;
    }

    public BilleMenuItem setPari(boolean pari) {
        this.pari = pari;
        return this;
    }
}
