package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;

public class TurnLeftSkullItem extends MenuItem {

    private PariGui pariGui;

    public TurnLeftSkullItem(PariGui pariGui) {
        super("§aPage avant", new CustomHead("cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5"));
        this.pariGui = pariGui;
    }

    @Override
    public void onClick(MenuClickAction menuClickAction) {
        pariGui.setPageOne(true);
    }
}
