package net.gameinbox.voidserver.config;

public class VoidConfiguration {

    /**
     * Limbo user access public port
     */
    int serverPort = 25565;

    /**
     * Is limbo using plugins or being vanilla
     */
    boolean usePlugins = false;

    /**
     * Max number of players currently connected to limbo server (-1 for infinite)
     */
    int maxPlayers = -1;

}
