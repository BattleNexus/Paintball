package net.battlenexus.paintball;

import net.battlenexus.paintball.commands.PBCommandHandler;
import net.battlenexus.paintball.entities.PBPlayer;
import net.battlenexus.paintball.game.GameService;
import net.battlenexus.paintball.listeners.PlayerListener;
import net.battlenexus.paintball.listeners.TickBukkitTask;
import net.battlenexus.paintball.scoreboard.ScoreManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Paintball extends JavaPlugin {
    public static Paintball INSTANCE;
    public World paintball_world;
    public Location lobby_spawn;
    private TickBukkitTask tasks;
    private GameService game;
    ScoreManager scoreboard;

    @Override
    public void onEnable() {
        INSTANCE = this;

        loadPluginConfig();

        scoreboard = new ScoreManager();

        PBCommandHandler handler = new PBCommandHandler();
        getCommand("createpbmap").setExecutor(handler);
        getCommand("cpbmap").setExecutor(handler);

        //Register Listeners
        registerListeners();

        tasks = new TickBukkitTask();
        tasks.runTaskTimerAsynchronously(this, 1, 1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                game = new GameService();
                game.loadMaps();
                game.play();
            }
        }).start();
    }

    private void registerListeners() {
        new PlayerListener(this);
    }

    public TickBukkitTask getTicker() {
        return tasks;
    }

    public void error(String message) {
        getLogger().info("[BN Paintball] ERROR: " + message);
    }

    public void sendWorldMessage(String message) {
        Player[] players = getServer().getOnlinePlayers();
        for (Player p : players) {
            if (p.getLocation().getWorld().getName().equals(paintball_world.getName())) {
                p.sendMessage(message);
            }
        }
    }

    public boolean isPlayingPaintball(Player player) {
        return player.getLocation().getWorld().getName().equals(paintball_world.getName());
    }

    public boolean isPlayingPaintball(PBPlayer player) {
        return isPlayingPaintball(player.getBukkitPlayer());
    }

    public static void sendGlobalWorldMessage(String message) {
        INSTANCE.sendWorldMessage(formatMessage(message));
    }

    public static String formatMessage(String message) {
        return ChatColor.WHITE + "[" + ChatColor.RED + "Paintball" + ChatColor.WHITE + "] " + ChatColor.GRAY + message;
    }

    public void loadPluginConfig() {
        saveDefaultConfig();

        String world_name = getConfig().getString("game.world.name", "world");
        WorldCreator creator = new WorldCreator(world_name);
        paintball_world = getServer().createWorld(creator);


        double world_x = getConfig().getDouble("game.world.lobbyx", paintball_world.getSpawnLocation().getX());
        double world_y = getConfig().getDouble("game.world.lobbyy", paintball_world.getSpawnLocation().getY());
        double world_z = getConfig().getDouble("game.world.lobbyz", paintball_world.getSpawnLocation().getZ());
        float world_yaw = (float) getConfig().getDouble("game.world.lobby_yaw", paintball_world.getSpawnLocation().getYaw());
        float world_pitch = (float)getConfig().getDouble("game.world.lobby_pitch", paintball_world.getSpawnLocation().getPitch());

        lobby_spawn = new Location(paintball_world, world_x, world_y, world_z, world_yaw, world_pitch);
    }
}