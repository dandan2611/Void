package net.gameinbox.voidserver.config;

public class VoidConfiguration {

    /**
     * Limbo user access public port
     */
    int serverPort = 25565;

    /**
     * Max number of players currently connected to limbo server (-1 for infinite)
     */
    int maxPlayers = -1;

    /**
     * Server description in server list
     */
    String motdText = "A Void-powered limbo server!";

    /**
     * Do the server need to use encryption? (online mode)
     */
    boolean useEncryption = false;

    public int getServerPort() {
        return serverPort;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getMotdText() {
        return motdText;
    }

    public boolean isUseEncryption() {
        return useEncryption;
    }

}
