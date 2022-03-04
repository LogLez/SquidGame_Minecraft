package io.hysekai.game.squidgame.scoreboard;

import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.RegularSidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.SidebarEntry;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.GameStatus;
import io.hysekai.game.squidgame.state.SquidGameState;
import org.bukkit.Bukkit;

import java.util.List;

/*
 * This file is part of hysekai.
 *
 * Copyright © 2021, Valentin ACCART <contact@v-accart.fr>. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class TimeAddon extends SidebarAddon {

    private final SquidGameManager squidGameManager;

    public TimeAddon(SquidGameManager squidGameManager) {
        super(squidGameManager.getSquidGamePlugin(), "Timing Phase");
        this.squidGameManager = squidGameManager;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return List.of(new RegularSidebarEntry(sidebar) {

            @Override
            public String getValue() {

                if(squidGameManager.getSquidGameState() == SquidGameState.WAITING || squidGameManager.getSquidGameState() == SquidGameState.PEREGAME)
                    return "Départ dans §6" + squidGameManager.getTimeUtils().formatedTime(squidGameManager.getBeginTask().getTime());

                if(squidGameManager.getCycle().isCycling())
                    return "Temps restant : §6" + squidGameManager.getTimeUtils().formatedTime(squidGameManager.getCycle().getTime());

                if (squidGameManager.getMiniGamesManager().getCurrentGame().getGameStatus() == GameStatus.PREGAME) {
                    return "Départ dans §6" + squidGameManager.getTimeUtils().formatedTimeInString(squidGameManager.getMiniGamesManager().getCurrentGame().getLoadGameTask().getTimeLoad());
                }else if (squidGameManager.getMiniGamesManager().getCurrentGame().getGameStatus() == GameStatus.GAME) {
                    return "Temps restant : §6" + squidGameManager.getTimeUtils().formatedTime(squidGameManager.getMiniGamesManager().getMiniGamesCountDownTask().getTime());
                }else {
                    return "Fin de jeu §6";
                }
            }
        });
    }
}
