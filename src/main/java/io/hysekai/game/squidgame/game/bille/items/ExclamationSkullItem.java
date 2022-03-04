package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;

public class ExclamationSkullItem extends MenuItem {

    private PariGui pariGui;

    public ExclamationSkullItem(PariGui pariGui) {
        super("§aRéinitialisez vos paris",new CustomHead("e7f9c6fef2ad96b3a5465642ba954671be1c4543e2e25e56aef0a47d5f1f"));
        this.pariGui = pariGui;
    }

    @Override
    public void onClick(MenuClickAction menuClickAction) {
	pariGui.reset();
        pariGui.updateDisplayFor(pariGui.getPlayer().getPlayer());
    }
}
