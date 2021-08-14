package net.gameinbox.voidserver.server.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.gameinbox.voidserver.VoidServer;
import net.gameinbox.voidserver.player.VoidPlayer;
import net.gameinbox.voidserver.server.PlayerConnection;
import net.gameinbox.voidserver.server.VoidNetworkingManager;
import net.gameinbox.voidserver.server.packet.login.PacketClientEncryptionRequest;
import net.gameinbox.voidserver.server.packet.login.PacketClientLoginSuccess;
import net.gameinbox.voidserver.server.packet.login.PacketLogin;
import net.gameinbox.voidserver.server.packet.login.PacketServerEncryptionResponse;
import net.gameinbox.voidserver.server.packet.status.*;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;
import net.gameinbox.voidserver.utils.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

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
            case LOGIN:
                loginPacket(playerConnection, packet);
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

                    playerConnection.communicationState = CommunicationState.LOGIN;

                    LOGGER.info("{} is logging in!", packetServerListPing.username);

                    if(server.getConfigurationManager().getConfig().isUseEncryption()) {
                        PacketClientEncryptionRequest packetClientEncryptionRequest = new PacketClientEncryptionRequest();

                        packetClientEncryptionRequest.serverId = "";
                        packetClientEncryptionRequest.publicKeyLength = server.getNetworkingManager().getEncryptionManager().getPublicKey().getEncoded().length;
                        packetClientEncryptionRequest.publicKey = server.getNetworkingManager().getEncryptionManager().getPublicKey().getEncoded();

                        byte[] verifyToken = playerConnection.generateVerifyToken();

                        packetClientEncryptionRequest.verifyTokenLength = verifyToken.length;
                        packetClientEncryptionRequest.verifyToken = verifyToken;

                        playerConnection.sendPacket(packetClientEncryptionRequest.encode());
                    }
                    else {
                        playerConnection.communicationState = CommunicationState.PLAY;

                        UUID uuid = UUID.randomUUID();

                        VoidPlayer player = new VoidPlayer(packetServerListPing.username, uuid, playerConnection);

                        playerConnection.setParent(player);

                        server.getPlayers().add(player);

                        PacketClientLoginSuccess packetClientLoginSuccess = new PacketClientLoginSuccess();

                        packetClientLoginSuccess.uuid = UuidUtils.asBytes(uuid);
                        packetClientLoginSuccess.username = player.getUsername();

                        playerConnection.sendPacket(packetClientLoginSuccess.encode());

                        LOGGER.info("{} [{}] logged in!", packetServerListPing.username, uuid.toString());
                    }

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

    private void loginPacket(PlayerConnection playerConnection, Packet<?> packet) {
        PacketLogin<?> packetLogin = (PacketLogin<?>) packet;

        if(packetLogin instanceof PacketServerEncryptionResponse) {
            PacketServerEncryptionResponse packetServerEncryptionResponse = (PacketServerEncryptionResponse) packetLogin;

            byte[] verifyToken = playerConnection.getVerifyToken();
            byte[] encryptedVerifyToken = packetServerEncryptionResponse.verifyToken;

            // Decrypt received verify token
            byte[] decryptedVerifyToken = server.getNetworkingManager().getEncryptionManager().decrypt(encryptedVerifyToken);

            // Compare tokens
            LOGGER.info(Arrays.equals(verifyToken, decryptedVerifyToken) + "");

        }
    }

}
