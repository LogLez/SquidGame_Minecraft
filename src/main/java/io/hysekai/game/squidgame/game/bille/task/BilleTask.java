package io.hysekai.game.squidgame.game.bille.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.menu.Menu;
import io.hysekai.game.squidgame.SquidGamePlugin;
import io.hysekai.game.squidgame.common.HysekaiRunTaskTimer;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.game.bille.Bille;
import io.hysekai.game.squidgame.game.bille.gui.DevineGui;
import io.hysekai.game.squidgame.game.bille.gui.PariGui;
import io.hysekai.game.squidgame.game.bille.teams.BilleTeam;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import io.hysekai.game.squidgame.utils.MessageUtils;

public class BilleTask extends HysekaiRunTaskTimer {

    private final SquidGamePlugin plugin;
    private MessageUtils msg;
    private final Bille bille;
    private int time = 0;
    private boolean pari = true;
    private boolean firstPlayer = false;
    private List<Menu> menus = new ArrayList<>();

    public BilleTask(Bille bille) {
		super(bille.getSquidGameManager(), 20);
		this.bille = bille;
		this.plugin = bille.getSquidGameManager().getSquidGamePlugin();
    }

    @Override
    public void run() {
	if (bille.getGameStatus() == GameStatus.FINISH) {
	    Bukkit.getOnlinePlayers().forEach(player -> HAPI.getUtils().sendAboveHotbarMessage(player, ""));
	    cancelTask();
	    
	} else {
	    if (pari) {
		if (time == 2) {
		    menus.clear();
		    for (AbstractTeam abstractTeam : bille.getTeams()) {
			BilleTeam billeTeam = (BilleTeam) abstractTeam;
			if (!billeTeam.isQualified()) {
			    PariGui pariGui = new PariGui(bille, billeTeam.getPlayer(firstPlayer));
			    msg.sendMessageToPlayer(billeTeam.getPlayer(firstPlayer), "§7Vous devez parier vos billes.");
			    msg.sendMessageToPlayer(billeTeam.getPlayer(!firstPlayer), "§7Votre partenaire est en train de parier ses billes.");
			    pariGui.open();
			    menus.add(pariGui);
			}
		    }
		} else if(time == 12) {
		    for (Menu menu : menus) {
			if(menu instanceof PariGui pariGui) {
			    pariGui.setClose(true);
			    Optional<AbstractTeam> abstractTeam = bille.getTeam(pariGui.getPlayer());

			    BilleTeam billeTeam = (BilleTeam) abstractTeam.get();
			    if (pariGui.getPari() == 0) {
				int pari = HAPI.getRandom().nextInt(pariGui.getBille()) + 1;
				pariGui.messagePari(pari);
				billeTeam.setBillePari(pari);
			    } else {
				billeTeam.setBillePari(pariGui.getPari());
			    }
			    
			    HAPI.getMenuManager().closeCurrentMenuForPlayer(pariGui.getPlayer().getPlayer());
			}
		    }
		    this.changeMode();
		}
	    } else {
		if (time == 2) {
		    menus.clear();
		    for (AbstractTeam abstractTeam : bille.getTeams()) {
			BilleTeam billeTeam = (BilleTeam) abstractTeam;
			if (!billeTeam.isQualified()) {
			    DevineGui devineGui = new DevineGui(plugin, (!firstPlayer ? billeTeam.getPlayerOne() : billeTeam.getPlayerTwo()));
			    msg.sendMessageToPlayer(billeTeam.getPlayer(!firstPlayer), "§7Vous devez parier pair ou impair.");
			    msg.sendMessageToPlayer(billeTeam.getPlayer(firstPlayer), "§7Votre partenaire tente de deviner votre mise de billes.");
			    devineGui.open();
			    menus.add(devineGui);
			}
		    }
		} else if(time == 8) {
		    for (Menu menu : menus) {
			if (menu instanceof DevineGui devineGui) {
			    if (!devineGui.isClose()) {
				boolean pari = HAPI.getRandom().nextBoolean();
				devineGui.setPair(pari);
				devineGui.messagePari(pari);
			    }
			    devineGui.setClose(true);
			    
			    Optional<AbstractTeam> abstractTeam = bille.getTeam(devineGui.getParticipantPlayer());
			    BilleTeam billeTeam = (BilleTeam) abstractTeam.get();
			    boolean pair = billeTeam.getBillePari() % 2 == 0;
			    int playerOneBille = billeTeam.getPlayerOne().getData(bille);
			    int playerTwoBille = billeTeam.getPlayerTwo().getData(bille);
			    //set max pari
			    int pari = billeTeam.getBillePari();
			    if (pari > playerOneBille)
				pari = playerOneBille;
			    if (pari > playerTwoBille)
				pari = playerTwoBille;
			    
			    //resultat
			    
			    if (pair == devineGui.isPair()) {
				if (firstPlayer) {
				    sendResult(billeTeam.getPlayerOne(), false, playerOneBille, pari);
				    sendResult(billeTeam.getPlayerTwo(), true, playerTwoBille, pari);
				} else {
				    sendResult(billeTeam.getPlayerOne(), true, playerOneBille, pari);
				    sendResult(billeTeam.getPlayerTwo(), false, playerTwoBille, pari);
				}
			    } else {
				if (firstPlayer) {
				    sendResult(billeTeam.getPlayerOne(), true, playerOneBille, pari);
				    sendResult(billeTeam.getPlayerTwo(), false, playerTwoBille, pari);
				} else {
				    sendResult(billeTeam.getPlayerOne(), false, playerOneBille, pari);
				    sendResult(billeTeam.getPlayerTwo(), true, playerTwoBille, pari);
				}
			    }

			    //elimination
			    if ((int) billeTeam.getPlayerOne().getData(bille) <= 0) {
				bille.onEliminate(billeTeam.getPlayerOne());
			    }
			    if ((int) billeTeam.getPlayerTwo().getData(bille) <= 0) {
				bille.onEliminate(billeTeam.getPlayerTwo());
			    }

			    //affichage des joueur en cours
			    HAPI.getMenuManager().closeCurrentMenuForPlayer(devineGui.getParticipantPlayer().getPlayer());
			}
		    }

		    this.changeMode();
		    firstPlayer = !firstPlayer;
		}
	    }

	    time++;
	}
    }

    private void changeMode(){
	this.pari = !pari;
	this.time = 0;
    }

	public void setMessageUtils(MessageUtils msg){
		this.msg = msg;
	}
    
    private void sendResult(ParticipantPlayer player, boolean win, int bille, int pari) {
	if (win) {
	    player.setData(this.bille, bille + pari);
	    msg.sendMessageToPlayer(player, "§7Vous §agagnez " + pari + " §7bille(s).");
	} else {
	    player.setData(this.bille, bille - pari);
	    msg.sendMessageToPlayer(player, "§7Vous §cperdez " + pari + " §7bille(s).");
	}
    }
    
}
