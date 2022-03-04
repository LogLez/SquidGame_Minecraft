package io.hysekai.game.squidgame.game;

import io.hysekai.game.squidgame.state.PvPState;

import java.util.List;

/*
 * This file is part of hysekai.
 *
 * Copyright © 2021, Valentin ACCART <contact@v-accart.fr>. All rights reserved.
 *
 * Unauthorized using, copying, modifying and/or distributing of this file,
 * via any medium is strictly prohibited. This code is confidential.
 */
public class TimeManager {

    private PvPState pvpState;

    private boolean hasNight;

    private List<String> dayTexts;
    private List<String> nightTexts;

    /*
    this.getSquidGameManager().setPvp(pvp);

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        getSquidGameManager().getScoreboardManager().getScoreboards().values().forEach(this::loadScoreboard);

        new HysekaiRunTaskDelay(getSquidGameManager(),3) {
            @Override
            public void run() {
                getSquidGameManager().getScoreboardManager().getScoreboards().values().forEach(Night.this::loadScoreboard);
                players.forEach(player -> {
                    if(player.isOnline()){
                        getSquidGameManager().getWorldsManager().teleport(SquidLocation.DORTOIR, player);
                        //player.teleport(location);
                        player.setLevel(0);
                        player.setExp(0.0f);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,60,20));
                    }
                });

                if(hasNight)
                    startNight();
                else
                    startDay();

                cancelTask();
            }
        }.start();
     */

    public void tick() {

    }
}
