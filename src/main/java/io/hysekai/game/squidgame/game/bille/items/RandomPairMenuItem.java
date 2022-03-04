package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.DevineGui;

public class RandomPairMenuItem extends MenuItem {

    private DevineGui devineGui;

    public RandomPairMenuItem(DevineGui devineGui) {
        super("§aRandom", new CustomHead("bc8ea1f51f253ff5142ca11ae45193a4ad8c3ab5e9c6eec8ba7a4fcb7bac40"));

        this.devineGui = devineGui;
    }

    @Override
    public void onClick(MenuClickAction action) {
	boolean pari = HAPI.getRandom().nextBoolean();
        devineGui.setPair(pari);
        devineGui.messagePari(pari);
        Menu menu = (Menu) action.getMenu();
	devineGui.setClose(true);
        menu.closeFor(action.getPlayer());
    }
}
