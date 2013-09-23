package net.battlenexus.paintball.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreManager {
    private final ScoreboardManager scoreboardManager;
    private Scoreboard scoreboard;
    private Objective objective;

    public ScoreManager() {
        this.scoreboardManager = Bukkit.getScoreboardManager();
    }

    /**
     * Initialises the scoreboard
     *
     * @param name String Objective Name
     * @param key String Objective Key
     */
    public void setupScoreboard(String name, String key) {
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective(key, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(name);
    }

    /**
     * Creates a new team with the given players
     *
     * @param teamName String The team name
     * @param players OfflinePlayer[] Players you want to add to the team
     */
    public void addTeam(String teamName, OfflinePlayer[] players) {
        //Create the new team
        Team team = scoreboard.registerNewTeam(teamName);
        char[] teamChars = teamName.toCharArray();
        boolean hasColor = teamChars[0] == ChatColor.COLOR_CHAR;
        team.setPrefix(ChatColor.translateAlternateColorCodes(ChatColor.COLOR_CHAR, (String) (hasColor ? ChatColor.COLOR_CHAR + teamChars[1] : ChatColor.GRAY)));

        //Add players to that team
        for (OfflinePlayer player : players) {
            team.addPlayer(player);
        }
    }

    /**
     * Shows this scoreboard to everyone on the server
     */
    public void showScoreboard() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    /**
     * Removes this scoreboard to everyone on the server
     */
    public void hideScoreboard() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboardManager.getNewScoreboard());
        }
    }

    /**
     * Adds players to a team
     *
     * @param teamName String The name of the team
     * @param players OfflinePlayer[] Players you want to add to the team
     */
    public void addPlayersToTeam(String teamName, OfflinePlayer[] players) {
        //Get the team
        Team team = scoreboard.getTeam(teamName);

        for (OfflinePlayer player : players) {
            team.addPlayer(player);
        }
    }

    /**
     * Adds points to a player's score
     *
     * @param player OfflinePlayer The Player you want to modify
     * @param amount int The points you want to add
     */
    public void addPoints(OfflinePlayer player, int amount) {
        Score score = objective.getScore(player);
        score.setScore(score.getScore() + amount);
    }

    /**
     * Takes points from a player's score
     *
     * @param player OfflinePlayer The player you want to modify
     * @param amount int The points you want to subtract
     */
    public void takePoints(OfflinePlayer player, int amount) {
        Score score = objective.getScore(player);
        score.setScore(score.getScore() - amount);
    }
}