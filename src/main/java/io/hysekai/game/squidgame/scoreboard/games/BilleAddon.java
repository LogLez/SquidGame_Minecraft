package io.hysekai.game.squidgame.scoreboard.games;

import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.KeyValueSidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.SidebarEntry;
import io.hysekai.game.squidgame.game.bille.Bille;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class BilleAddon  extends SidebarAddon {

    private final Bille bille;

    public BilleAddon(Bille bille) {
        super(bille.getSquidGameManager().getSquidGamePlugin(), "Bille Game");
        this.bille = bille;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return List.of(new KeyValueSidebarEntry(sidebar, "Billes") {

            @Override
            public String getValueFor(Player player) {
                ParticipantPlayer participantPlayer = bille.getSquidGameManager().getParticipantManager().getParticipant(player);
                if(participantPlayer == null || participantPlayer.getData(bille) == null) return "§cX";
                return ""+ (int) participantPlayer.getData(bille);
            }
        });
    }
}
