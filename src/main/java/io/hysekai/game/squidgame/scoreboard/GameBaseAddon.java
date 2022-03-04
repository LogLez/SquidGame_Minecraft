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

/*
 * This file is part of hysekai.
 *
 * Copyright © 2021, Valentin ACCART <contact@v-accart.fr>. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class GameBaseAddon extends SidebarAddon {

    private final SquidGameManager squidGameManager;

    private final GameType game;

    public GameBaseAddon(SquidGameManager squidGameManager, GameType game) {
        super(squidGameManager.getSquidGamePlugin(), game.getName() + " Waiting Phase");
        this.squidGameManager = squidGameManager;
        this.game = game;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return Arrays.asList(
                new SeparatorSidebarAddon.SeparatorEntry(sidebar),
                new TitleSidebarEntry(sidebar, game.getName() , game.getColor()),
                new KeyValueSidebarEntry(sidebar, "Joueurs en vie",
                        "§9" + squidGameManager.getParticipantManager().getPlayingPlayers().size())
        );
    }
}
