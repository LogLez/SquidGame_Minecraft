package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;

public class TurnRightSkullItem extends MenuItem {

    private PariGui pariGui;

    public TurnRightSkullItem(PariGui pariGui) {
        super("§aPage suivant", new CustomHead("956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311"));
        this.pariGui = pariGui;
    }

    @Override
    public void onClick(MenuClickAction menuClickAction) {
        pariGui.setPageOne(false);
    }
}
