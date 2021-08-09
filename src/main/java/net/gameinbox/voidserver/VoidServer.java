package net.gameinbox.voidserver;

import net.gameinbox.voidserver.config.ConfigurationManager;
import net.gameinbox.voidserver.config.VoidConfiguration;
import net.gameinbox.voidserver.server.VoidNetworkingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoidServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoidServer.class);

    private final ConfigurationManager configurationManager;

    private VoidNetworkingManager networkingManager;

    public VoidServer(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void init() {
        int serverPort = configurationManager.getConfig().getServerPort();

        // Init VoidNetworkingManager
        networkingManager = new VoidNetworkingManager(this);

        LOGGER.info("Initializing networking manager...");
        networkingManager.init();
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

}
