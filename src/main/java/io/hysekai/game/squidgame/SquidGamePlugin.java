package io.hysekai.game.squidgame;

import io.hysekai.bukkit.api.HAPI;
import io.hysekai.bukkit.api.scoreboard.sidebar.Sidebar;
import io.hysekai.bukkit.api.world.HWorld;
import io.hysekai.game.squidgame.commands.*;
import io.hysekai.game.squidgame.manager.CommandManager;
import io.hysekai.game.squidgame.manager.SquidLocations;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;


public class SquidGamePlugin extends JavaPlugin {

    private SquidGameManager squidGameManager;
    private CommandManager commandManager;

    private Sidebar sidebar;

    @Override
    public void onEnable() {
        super.onEnable();

        // Setup worlds
       // HAPI.getWorldManager().useWorldProtocolEvent(this, true);

        World defaultWorld = Bukkit.getWorld("world");

        // Scoreboard
        sidebar = HAPI.getScoreboardManager().getSidebar(defaultWorld);
        sidebar.updateTitle("§6§lSQUID GAME");
        sidebar.setDomainDisplay(true);

        // Disable mob spawning
        defaultWorld.setSpawnFlags(false, false);
        defaultWorld.setGameRuleValue("doMobSpawning", "false");

        // Get HWorld
        HWorld defaultHWorld = HAPI.getWorldManager().getHWorld(defaultWorld);

        // Disables explosions
        defaultHWorld.setExplosionsDisabled(true);
        // Disable interaction
        defaultHWorld.setInteractionDisabled(true);
        // Disable fire spreading
        defaultHWorld.setFireSpreadingDisabled(true);
        // Disable hunger
        defaultHWorld.setHungerDisabled(true);
        defaultWorld.setDifficulty(Difficulty.PEACEFUL);
        defaultWorld.setKeepSpawnInMemory(false);
        // Game worlds

        Arrays.asList(SquidLocations.values()).forEach(SquidLocations::setLocation);

        this.commandManager = new CommandManager();
        this.squidGameManager = new SquidGameManager(this);

        commandManager.registerCommandArgs(new StartCommand(squidGameManager));
        commandManager.registerCommandArgs(new StopCommand(squidGameManager));
        commandManager.registerCommandArgs(new TeamCommand(squidGameManager));
        commandManager.registerCommandArgs(new InvCommand());
        //commandManager.registerCommandArgs(new TestCommand());

        getCommand("team").setExecutor(new TestCmd(getSquidGameManager()));
    }

    @Override
    public void onDisable() {
        //Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("§bMise à jour..."));
        //CitizensAPI.getNPCRegistries().forEach(NPCRegistry::deregisterAll);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("squidgame")){

            if(args.length == 0){
                player.sendMessage("/sg start");
                player.sendMessage("/sg stop");
                player.sendMessage("/sg freeze");
                return true;
            }

            commandManager.executeCommand(player,args);
        }
        return false;
    }

    public SquidGameManager getSquidGameManager() {return squidGameManager;}

    public Sidebar getSidebar() {
        return sidebar;
    }
}
