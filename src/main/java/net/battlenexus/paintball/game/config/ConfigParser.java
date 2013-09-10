package net.battlenexus.paintball.game.config;

import org.w3c.dom.NodeList;

import java.util.ArrayList;

public interface ConfigParser {

    public void parse(Config config, NodeList childNodes);

    public void save(ArrayList<String> lines);
}
