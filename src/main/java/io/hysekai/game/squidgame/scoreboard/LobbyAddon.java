package io.hysekai.game.squidgame.scoreboard;

import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.addons.SeparatorSidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.KeyValueSidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.SidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.TitleSidebarEntry;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.GameType;

import java.util.Arrays;
import java.util.List;

public class LobbyAddon extends SidebarAddon {

    private final SquidGameManager squidGameManager;

    public LobbyAddon(SquidGameManager squidGameManager) {
        super(squidGameManager.getSquidGamePlugin(), "Waiting Phase");
        this.squidGameManager = squidGameManager;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return Arrays.asList(
                new SeparatorSidebarAddon.SeparatorEntry(sidebar),
                new TitleSidebarEntry(sidebar, "Phase d'attente" , "§7"),
                new KeyValueSidebarEntry(sidebar, "Joueurs en vie"){
                    @Override
                    public String getValue() {
                        return "§9" + squidGameManager.getParticipantManager().getPlayingPlayers().size();
                    }
                }
        );
    }
}
