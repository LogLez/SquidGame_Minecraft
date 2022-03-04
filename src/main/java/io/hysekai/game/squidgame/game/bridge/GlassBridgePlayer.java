package io.hysekai.game.squidgame.game.bridge;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.utils.Numbers;
import io.hysekai.game.squidgame.utils.UtilItem;
import io.hysekai.game.squidgame.game.bridge.chasubles.Chasuble;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.Optional;

public class GlassBridgePlayer implements Comparable<GlassBridgePlayer>{

    private final Bridge bridge;
    private final ParticipantPlayer participantPlayer;
    private Chasuble chasuble;
    private boolean canMove = false, qualified = false;

    public GlassBridgePlayer(Bridge bridge, ParticipantPlayer participantPlayer){
        this.bridge = bridge;
        this.participantPlayer = participantPlayer;
    }


    public ParticipantPlayer getParticipantPlayer() {return participantPlayer;}
    public boolean isQualified() { return qualified; }
    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }
    public boolean isCanMove() { return canMove; }
    public GlassBridgePlayer setCanMove(boolean canMove) {
        this.canMove = canMove;
        getParticipantPlayer().sendActions(player -> getBridge().getSquidGameManager().getMessageUtils().sendMessageToPlayer(player, "§aC'est maintenant votre tour !"));
        return  this;
    }
    public Bridge getBridge() { return bridge; }
    public boolean hasChasuble(){ return this.chasuble != null;}
    public Chasuble getChasuble() {return chasuble;}
    public boolean setChasuble(Chasuble chasuble) {
        if(this.chasuble != null){
            getParticipantPlayer().sendActions(player -> player.sendMessage("§cVous avez déjà un chasuble !"));
            return false;
        }
        this.chasuble = chasuble;
        this.chasuble.setTaken(true);
        Optional<Numbers> number = Numbers.getNumber(chasuble.getId());
        number.ifPresent(numbers -> {
            UtilItem itemStack = new UtilItem().createItem(Material.LEATHER_CHESTPLATE).setName(number.get().getData()).setArmorColor(Color.WHITE);
            participantPlayer.sendActions(player -> player.getInventory().setChestplate(itemStack));
        });


        getParticipantPlayer().sendActions(player -> HAPI.getUtils().sendTitle(player,0,60,0,"§7Numéro : §9" + getChasuble().getId(),""));
       /* if(getBridge().getSquidGameManager().getScoreboardManager().getScoreboards().containsKey(participantPlayer))
            getBridge().getSquidGameManager().getScoreboardManager().getScoreboards().get(participantPlayer).updateLine(4,"§8▏ §7Numéro : §9" + chasuble.getId());
*/
        return true;
    }


    @Override
    public int compareTo(GlassBridgePlayer o) {
        return (this.getChasuble().getId() - o.getChasuble().getId());
    }


}
