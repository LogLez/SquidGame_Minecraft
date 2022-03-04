package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;

public class PariRandomSkullItem extends MenuItem {

    private PariGui pariGui;

    public PariRandomSkullItem(PariGui pariGui) {
        super("§aFaire un pari random", new CustomHead("bc8ea1f51f253ff5142ca11ae45193a4ad8c3ab5e9c6eec8ba7a4fcb7bac40"));
        this.pariGui = pariGui;
    }

    @Override
    public void onClick(MenuClickAction action) {
	int pari = HAPI.getRandom().nextInt(pariGui.getBille()) + 1;
	pariGui.setPari(pari);
	pariGui.messagePari(pari);
        Menu menu = (Menu) action.getMenu();
        pariGui.setClose(true);
        menu.closeFor(action.getPlayer());
    }
}
