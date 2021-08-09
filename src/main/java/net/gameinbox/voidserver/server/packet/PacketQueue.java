package net.gameinbox.voidserver.server.packet;

import net.gameinbox.voidserver.VoidServer;
import net.gameinbox.voidserver.server.PlayerConnection;
import net.gameinbox.voidserver.server.VoidNetworkingManager;
import net.gameinbox.voidserver.server.packet.status.*;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketQueue.class);

    private VoidServer server;
    private VoidNetworkingManager networkingManager;

    public PacketQueue(VoidServer server) {
        this.server = server;
        this.networkingManager = server.getNetworkingManager();
    }

    public void process(PlayerConnection playerConnection, Packet<?> packet) {
        CommunicationState communicationState = playerConnection.communicationState;
        switch (communicationState) {
            case STATUS:
                statusPacket(playerConnection, packet);
                break;
        }
    }

    private void statusPacket(PlayerConnection playerConnection, Packet<?> packet) {
        PacketStatus<? extends PacketStatus<?>> packetStatus = (PacketStatus<? extends PacketStatus<?>>) packet;

        if(packetStatus instanceof PacketServerListPing) {
            PacketServerListPing packetServerListPing = (PacketServerListPing) packetStatus;

            switch (packetServerListPing.nextState) {
                case 1:
                    // Infos

                    PacketClientServerInfo packetClientServerInfo = new PacketClientServerInfo();
                    packetClientServerInfo.versionName = "Void 1.17.1";
                    packetClientServerInfo.protocol = ProtocolVersion.V1_17_1.getProtocol();
                    packetClientServerInfo.maxPlayers = server.getConfigurationManager().getConfig().getMaxPlayers();
                    packetClientServerInfo.onlinePlayers = 1;
                    packetClientServerInfo.descriptionText = server.getConfigurationManager().getConfig().getMotdText();

                    playerConnection.sendPacket(packetClientServerInfo.encode());
                    break;
                case 2:
                    // Login

                    break;
            }
        }
        else if(packetStatus instanceof PacketServerPing) {
            PacketServerPing packetServerPing = (PacketServerPing) packetStatus;

            PacketClientPing packetClientPing = new PacketClientPing();
            packetClientPing.value = packetServerPing.value;

            playerConnection.sendPacket(packetClientPing.encode());
        }
    }

}
