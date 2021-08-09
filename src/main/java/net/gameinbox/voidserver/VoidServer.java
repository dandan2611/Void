package net.gameinbox.voidserver;

import net.gameinbox.voidserver.config.ConfigurationManager;
import net.gameinbox.voidserver.config.VoidConfiguration;
import net.gameinbox.voidserver.player.VoidPlayer;
import net.gameinbox.voidserver.server.VoidNetworkingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoidServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoidServer.class);

    private final ConfigurationManager configurationManager;

    private VoidNetworkingManager networkingManager;

    private ArrayList<VoidPlayer> players;

    public VoidServer(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void init() {
        int serverPort = configurationManager.getConfig().getServerPort();

        // Init VoidNetworkingManager
        networkingManager = new VoidNetworkingManager(this);

        LOGGER.info("Initializing networking manager...");
        networkingManager.init();

        players = new ArrayList<>();
    }

    public void close() {
        LOGGER.info("Closing networking manager");
        networkingManager.close();
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public VoidNetworkingManager getNetworkingManager() {
        return networkingManager;
    }

    public List<VoidPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void registerPlayer(VoidPlayer player) {
        players.add(player);
    }

    public void unregisterPlayer(VoidPlayer player) {
        players.remove(player);
    }

}
