package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.DevineGui;

public class PairMenuItem extends MenuItem {

    private DevineGui devineGui;
    private boolean pair;

    public PairMenuItem(DevineGui devineGui, boolean pair) {
        super((pair ? "§aPair" : "§aImpair"), new CustomHead(
                (pair ? "4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a"
                        : "d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763")
        ));

        this.devineGui = devineGui;
        this.pair = pair;
    }

    @Override
    public void onClick(MenuClickAction action) {
        devineGui.setPair(pair);
        devineGui.messagePari(pair);
        Menu menu = (Menu) action.getMenu();
        devineGui.setClose(true);
        menu.closeFor(action.getPlayer());
    }
}
