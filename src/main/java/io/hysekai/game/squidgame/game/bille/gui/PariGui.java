package io.hysekai.game.squidgame.game.bille.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.item.ItemProperties;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuCloseAction;
import io.hysekai.game.squidgame.SquidGamePlugin;
import io.hysekai.game.squidgame.game.bille.Bille;
import io.hysekai.game.squidgame.game.bille.items.BilleMenuItem;
import io.hysekai.game.squidgame.game.bille.items.ExclamationSkullItem;
import io.hysekai.game.squidgame.game.bille.items.PariRandomSkullItem;
import io.hysekai.game.squidgame.game.bille.items.PariValidateSkullItem;
import io.hysekai.game.squidgame.game.bille.items.TurnLeftSkullItem;
import io.hysekai.game.squidgame.game.bille.items.TurnRightSkullItem;
import io.hysekai.game.squidgame.players.ParticipantPlayer;

public class PariGui extends Menu {

    private String[] billeTexture = {
            "a7f381a20a9c640428077070cc7bd95d688592d1104ccbcd713649a49e41ebfb",
            "eef162def845aa3dc7d46cd08a7bf95bbdfd32d381215aa41bffad5224298728",
            "52dd11da04252f76b6934bc26612f54f264f30eed74df89941209e191bebc0a2",
            "76387fc246893d92a6dd9ea1b52dcd581e991eeee2e263b27fff1bcf1b154eb7",
            "4f85522ee815d110587fffc74113f419d929598e2463b8ce9d39caa9fb6ff5ab",
            "7a2df315b43583b1896231b77bae1a507dbd7e43ad86c1cfbe3b2b8ef3430e9e",
            "a26ec7cd3b6ae249997137c1b94867c66e97499da071bf50adfd37034132fa03",
            "506e6d83cf7ed5cf7bf0e018ecb6039b046d8dc6db5569014fcab37b617f1399",
            "f868e6a5c4a445d60a3050b5bec1d37af1b25943745d2d479800c8436488065a",
            "f052be1c06a4a325129d6f41bb84f0ea1ca6f9f69ebdfff4316e742451c79c21"
    };

    private boolean close;
    private int pari;
    private int bille;
    private boolean pageOne;
    private ParticipantPlayer player;
    private final SquidGamePlugin plugin;
    private List<BilleMenuItem> billeMenuItems = new ArrayList<>();

    public PariGui(Bille bille, ParticipantPlayer player) {
        super("Pariez vos billes", 5 * 9);
        this.plugin = bille.getSquidGameManager().getSquidGamePlugin();
        this.player = player;
        this.bille = player.getData(bille);
        this.close = false;
        reset();
    }
    
    public void messagePari(int pari) {
	plugin.getSquidGameManager().getMessageUtils().sendMessageToPlayer(player, "§7Vous avez misé §e" + pari + " §7bille(s).");
    }

    @Override
    public void onClose(MenuCloseAction action) {
	if (!close)
	    Bukkit.getScheduler().runTask(plugin, this::open);
    }
    
    public void setClose(boolean close) {
        this.close = close;
    }
    
    public void open() {
	HAPI.getMenuManager().displayMenuToPlayer(this, player.getPlayer());
    }

    public void reset() {
	 int[] slots = {30,31,32,33,34,39,40,41,42,43};
	 for (int slot : slots)
	    setItem(slot, null);
	 
	 this.pageOne = true;

	 for (int x = 0;x < 2; x++) {
	     for(int y = 0; y < billeTexture.length; y++){
		 billeMenuItems.add(new BilleMenuItem(this,0,billeTexture[y]));
	     }
	 }

	 int[] stained = {0,1,2,8,9,11,17,18,20,21,22,23,24,25,26,27,29,35,36,37,38,44};
	 for (int x = 0; x < stained.length;x++)
	     this.setItem(stained[x],new MenuItem(" ", new ItemProperties(Material.STAINED_GLASS_PANE).setDurability((short) 7)));

	 setItem(10, new PariRandomSkullItem(this));
	 setItem(19, new ExclamationSkullItem(this));
	 setItem(28, new PariValidateSkullItem(this));

	 setItem((pageOne ? 24 : 22), (pageOne ? new TurnRightSkullItem(this) : new TurnLeftSkullItem(this)));
	 for(int x = 0; x < 10; x++)
	     if((pageOne ? this.bille > x : this.bille > x + 10) && !billeMenuItems.get((pageOne ? x : x + 10)).isPari()) setItem((x >= 5 ? 7 : 3) + x, billeMenuItems.get((pageOne ? x : x + 10)).setSlot((x >= 5 ? 7 : 3) + x));
    }
    
    public void move(BilleMenuItem menuItem, boolean hasPari) {
        if (hasPari) {
            for (int x = 29; x < 9 * 5 ; x++) {
                if (getItem(x) == null) {
                    setItem(menuItem.getSlot(), null);
                    setItem(x, menuItem.setSlot(x).setPari(true));
                }
            }
        } else {
            for (int x = 0; x < 17 ; x++) {
                if (getItem(x) == null) {
                    setItem(menuItem.getSlot(), null);
                    setItem(x, menuItem.setSlot(x).setPari(false));
                }
            }
        }
        updateDisplayFor(player.getPlayer());
    }

    public void setPageOne(boolean pageOne) {
        this.pageOne = pageOne;

        for(int x = 0; x < 10; x++){
            if((pageOne ? bille > x : bille > x + 10)  && !billeMenuItems.get((pageOne ? x : x + 10)).isPari()) setItem((x >= 5 ? 7 : 3) + x, billeMenuItems.get((pageOne ? x : x + 10)).setSlot((x >= 5 ? 7 : 3) + x));
            else setItem((x >= 5 ? 7 : 3) + x,null);
        }

        if(pageOne){
            setItem(24, new TurnRightSkullItem(this));
            setItem(22, new MenuItem(" ", new ItemProperties(Material.STAINED_GLASS_PANE).setDurability((short) 7)));
        }else{
            setItem(22, new TurnLeftSkullItem(this));
            setItem(24, new MenuItem(" ", new ItemProperties(Material.STAINED_GLASS_PANE).setDurability((short) 7)));
        }

        this.updateDisplayFor(player.getPlayer());
    }

    public int getPari(){
        return pari;
    }

    public void setPari(int pari) {
        this.pari = pari;
    }

    public int getBille() {
        return bille;
    }

    public void setBille(int bille) {
        this.bille = bille;
    }

    public ParticipantPlayer getPlayer() {
        return player;
    }
    
    public SquidGamePlugin getPlugin() {
        return plugin;
    }

}
