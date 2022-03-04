package io.hysekai.game.squidgame.game.bille.items;

import io.hysekai.bukkit.api.material.CustomHead;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuClickAction;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;

public class PariValidateSkullItem extends MenuItem {

    private PariGui pariGui;

    public PariValidateSkullItem(PariGui pariGui) {
        super("§aValider votre parie", new CustomHead("a86c97159cd658858d12b833d1671b84529e1f116d8be379d43824cb17963bb"));
        this.pariGui = pariGui;
    }

    @Override
    public void onClick(MenuClickAction menuClickAction) {
        //Player player = menuClickAction.getPlayer();
        Menu menu = (Menu) menuClickAction.getMenu();

        int[] slots = {30,31,32,33,34,39,40,41,42,43};
        int pari = 0;
        for (int slot : slots)
            if(menu.getItem(slot) != null) pari++;
        
        pariGui.setPari(pari);
        pariGui.messagePari(pari);
        pariGui.setClose(true);
        menu.closeFor(menuClickAction.getPlayer());
    }
}
