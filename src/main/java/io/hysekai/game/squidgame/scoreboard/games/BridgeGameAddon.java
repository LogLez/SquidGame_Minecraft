package io.hysekai.game.squidgame.scoreboard.games;

import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.KeyValueSidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.SidebarEntry;
import io.hysekai.game.squidgame.game.bridge.Bridge;
import io.hysekai.game.squidgame.game.bridge.GlassBridgePlayer;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BridgeGameAddon extends SidebarAddon {

    private final Bridge bridge;

    public BridgeGameAddon(Bridge bridge) {
        super(bridge.getSquidGameManager().getSquidGamePlugin(), "Bridge Game");
        this.bridge = bridge;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return Arrays.asList(
                new KeyValueSidebarEntry(sidebar, "Ordre de passage"){
                    @Override
                    public String getValue() {
                        String passage = "§cX";
                        if(bridge.getDirection() != null)
                            passage = bridge.getDirection().getName();

                        return passage;
                    }
                },

                new KeyValueSidebarEntry(sidebar, "Numéro") {

                    @Override
                    public String getValueFor(Player player) {
                        GlassBridgePlayer glassBridgePlayer = bridge.getGlassBridgePlayer(player);
                        if(glassBridgePlayer == null || !glassBridgePlayer.hasChasuble()) return "§cX";

                        return  "§7"+glassBridgePlayer.getChasuble().getId();
                    }
            });
    }
}
