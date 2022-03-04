package io.hysekai.game.squidgame.scoreboard.games;

import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.scoreboard.sidebar.SidebarAddon;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.KeyValueSidebarEntry;
import io.hysekai.bukkit.api.scoreboard.sidebar.entries.SidebarEntry;
import io.hysekai.game.squidgame.game.rope.Rope;
import io.hysekai.game.squidgame.game.rope.team.Team;
import io.hysekai.game.squidgame.players.ParticipantPlayer;
import io.hysekai.game.squidgame.team.AbstractTeam;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/*
 * This file is part of hysekai.
 *
 * Copyright © 2021, Valentin ACCART <contact@v-accart.fr>. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class RopeGameAddon extends SidebarAddon {

    private final Rope rope;

    public RopeGameAddon(Rope rope) {
        super(rope.getSquidGameManager().getSquidGamePlugin(), "Rope Game");
        this.rope = rope;
    }

    @Override
    public Iterable<? extends SidebarEntry> registerEntries(Sidebar sidebar, List<SidebarEntry> list) {
        return List.of(new KeyValueSidebarEntry(sidebar, "Equipe") {

            @Override
            public String getValueFor(Player player) {
                String line;

                ParticipantPlayer participantPlayer = rope.getSquidGameManager().getParticipantManager().getParticipant(player);

                Optional<AbstractTeam> optTeam = rope.getTeam(participantPlayer);
                if (optTeam.isPresent()) {
                    Team team = (Team) optTeam.get();
                    line = "§9" + team.getId() + "§7(" + team.getPlayers().size() + ")";
                } else {
                    line = "§c✘";
                }

                int leftTeams = rope.getMaxTeams() - rope.getTeams().size();
                return line + " §8(" + leftTeams + ")";
            }
        });
    }
}
