package net.gameinbox.voidserver.player;

import net.gameinbox.voidserver.server.PlayerConnection;

import java.util.UUID;

public class VoidPlayer {

    private String username;
    private UUID uuid;

    private PlayerConnection playerConnection;

    public VoidPlayer(String username, UUID uuid, PlayerConnection playerConnection) {
        this.username = username;
        this.uuid = uuid;
        this.playerConnection = playerConnection;
    }

    public boolean isOnline() {
        return playerConnection.channel.isActive();
    }

    public String getUsername() {
        return username;
    }

    public UUID getUuid() {
        return uuid;
    }

}
