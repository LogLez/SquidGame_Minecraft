package io.hysekai.game.squidgame.game.bille.gui;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.item.ItemProperties;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.bukkit.api.menu.MenuItem;
import io.hysekai.bukkit.api.menu.action.MenuCloseAction;
import io.hysekai.game.squidgame.SquidGamePlugin;
import io.hysekai.game.squidgame.game.bille.items.PairMenuItem;
import io.hysekai.game.squidgame.game.bille.items.RandomPairMenuItem;
import io.hysekai.game.squidgame.players.ParticipantPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class DevineGui extends Menu {

    private boolean close;
    private boolean pair;
    private SquidGamePlugin plugin;
    private ParticipantPlayer participantPlayer;

    public DevineGui(SquidGamePlugin plugin, ParticipantPlayer participantPlayer) {
        super("Deviner le pari", 9 * 3);
        this.plugin = plugin;
        this.close = false;
        
        int[] stained = {0,1,2,3,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26};
        for (int x = 0; x < stained.length; x++) setItem(stained[x],new MenuItem("", new ItemProperties(Material.STAINED_GLASS_PANE).setDurability((short) 7)));

        setItem(4, new RandomPairMenuItem(this));
        setItem(11, new PairMenuItem(this,true));
        setItem(15, new PairMenuItem(this,false));
        this.participantPlayer = participantPlayer;
    }

    @Override
    public void onClose(MenuCloseAction action) {
	if (!close)
	    Bukkit.getScheduler().runTask(plugin, this::open);
    }

    public void setClose(boolean close) {
        this.close = close;
    }
    
    public boolean isClose() {
        return close;
    }

    public void open() {
	HAPI.getMenuManager().displayMenuToPlayer(this, participantPlayer.getPlayer());
    }
    
    public void messagePari(boolean pair) {
   	plugin.getSquidGameManager().getMessageUtils().sendMessageToPlayer(
   		participantPlayer,
   		"§7Vous avez parié §e" + (pair ? "pair" : "impair") + "§7."
   		);
    }

    public boolean isPair() {
        return pair;
    }

    public void setPair(boolean pair) {
        this.pair = pair;
    }

    public ParticipantPlayer getParticipantPlayer() {
        return participantPlayer;
    }

    public void setParticipantPlayer(ParticipantPlayer participantPlayer) {
        this.participantPlayer = participantPlayer;
    }
}
