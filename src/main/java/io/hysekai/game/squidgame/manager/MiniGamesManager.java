package io.hysekai.game.squidgame.manager;

import io.hysekai.game.squidgame.game.GameType;
import io.hysekai.game.squidgame.common.tasks.MiniGamesCountDownTask;
import io.hysekai.game.squidgame.SquidGameManager;
import io.hysekai.game.squidgame.game.AbstractGame;
import org.bukkit.Bukkit;

import java.util.*;

public class MiniGamesManager {

    protected final SquidGameManager squidGameManager;
    private final List<AbstractGame> games = new ArrayList<>();

    private AbstractGame currentGame;
    private MiniGamesCountDownTask miniGamesCountDownTask;

    public MiniGamesManager(SquidGameManager squidGameManager) {
        this.squidGameManager = squidGameManager;
        loadAllGames();

        this.currentGame = this.games.get(0);

    }

    public Optional<AbstractGame> getGame(GameType gameType){
        return getGames().stream().filter(abstractGame -> abstractGame.getGameType() == gameType).findFirst();
    }
    private void addGame(GameType gameType){ this.games.add(getGameInstance(gameType)); }
    public void loadAllGames(){ Arrays.asList(GameType.values()).forEach(this::addGame); }
    public AbstractGame getCurrentGame() { return currentGame; }
    public void setCurrentGame(AbstractGame currentGame) {
        Bukkit.broadcastMessage("next game " + currentGame.getGameType().getName());
        this.currentGame = currentGame; }

    public SquidGameManager getSquidGameManager() { return squidGameManager; }
    public List<AbstractGame> getGames() {return games;}


    public void nextGame(int current){
        int nextId = current + 1;
        Bukkit.broadcastMessage(nextId + "");
        Optional<AbstractGame> optionalAbstractGame = getGames().stream().filter(abstractGame -> abstractGame.getGameType().getId() == nextId).findFirst();

        if(optionalAbstractGame.isPresent())
            setCurrentGame(optionalAbstractGame.get());
        else
            getSquidGameManager().stopGame();
    }

    private AbstractGame getGameInstance(GameType gameType){
        try {
            return (AbstractGame) gameType.getAbstractGame().getConstructors()[0].newInstance(squidGameManager);
        }catch (Exception e){
            return null;
        }
    }

    public MiniGamesCountDownTask getMiniGamesCountDownTask() { return miniGamesCountDownTask; }
    public void setMiniGamesCountDownTask(MiniGamesCountDownTask miniGamesCountDownTask) { this.miniGamesCountDownTask = miniGamesCountDownTask; }

}
