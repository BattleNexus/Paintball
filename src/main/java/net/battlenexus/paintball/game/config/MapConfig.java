package net.battlenexus.paintball.game.config;

import net.battlenexus.paintball.entities.Team;
import net.battlenexus.paintball.game.config.impl.ArrayListConfig;
import net.battlenexus.paintball.game.config.impl.LocationConfig;
import org.bukkit.Location;

import java.security.InvalidParameterException;

public class MapConfig extends ReflectionConfig {
    @ConfigItem
    private Team blue_team;
    @ConfigItem
    private Team red_team;
    @ConfigItem
    private String map_name;
    @ConfigItem
    private Integer playerMax = 16;
    @ConfigItem
    public ArrayListConfig<LocationConfig> chests = new ArrayListConfig<LocationConfig>();

    public MapConfig() {
        blue_team = new Team();
        red_team = new Team();
    }

    public MapConfig(MapConfig toClone) {
        if (toClone == null)
            throw new InvalidParameterException("toClone cannot be null!");
        this.map_name = toClone.map_name;
        //this.map_config = new Map(toClone.map_config);
        this.blue_team = new Team(toClone.blue_team);
        this.red_team = new Team(toClone.red_team);
    }

    public void setMapName(String mapName) {
        this.map_name = mapName;
    }

    public void setTeamName(int team, String name) {
        if (team == 0) {
            blue_team.setTeamName(name);
        } else if (team == 1) {
            red_team.setTeamName(name);
        }
    }

    public void setTeamSpawn(int team, Location spawn) {
        if (team == 0) {
            blue_team.setSpawn(spawn);
        } else if (team == 1) {
            red_team.setSpawn(spawn);
        }
    }

    public Team getRedTeam() {
        return red_team;
    }

    public Team getBlueTeam() {
        return blue_team;
    }

    public String getMapName() {
        return map_name;
    }

    public int getPlayerMax() {
        return playerMax;
    }

    public void setPlayerMax(int playerMax) {
        this.playerMax = playerMax;
    }
}