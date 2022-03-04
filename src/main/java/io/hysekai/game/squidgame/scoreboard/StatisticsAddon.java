package io.hysekai.game.squidgame.scoreboard;

import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.addons.SeparatorSidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.KeyValueSidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.SidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.TitleSidebarEntry;
import io.hysekai.game.squidgame.SquidGameManager;
import org.bukkit.ChatColor;

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
public class StatisticsAddon extends SidebarAddon {

    private final SquidGameManager squidGameManager;

    public StatisticsAddon(SquidGameManager squidGameManager) {
        super(squidGameManager.getSquidGamePlugin(), "Statistics");
        this.squidGameManager = squidGameManager;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return Arrays.asList(
                new SeparatorSidebarAddon.SeparatorEntry(sidebar),
                new TitleSidebarEntry(sidebar, "Statistiques", ChatColor.GOLD.toString()),
                new KeyValueSidebarEntry(sidebar, "Record",
                        "§c✘")
        );
    }
}
