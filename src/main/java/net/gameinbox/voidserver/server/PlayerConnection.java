package net.gameinbox.voidserver.server;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.gameinbox.voidserver.player.VoidPlayer;
import net.gameinbox.voidserver.server.packet.CommunicationState;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.Packet;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class PlayerConnection {

    public final Channel channel;

    public CommunicationState communicationState;

    private VoidPlayer parent;

    private byte[] verifyToken;

    public PlayerConnection(Channel channel, CommunicationState communicationState) {
        this.channel = channel;
        this.communicationState = communicationState;
    }

    public void sendPacket(EncodedPacket packet) {
        if(channel.isWritable()) {
            channel.writeAndFlush(packet.getEncodedPacket());
        }
    }

    public VoidPlayer getParent() {
        return parent;
    }

    public void setParent(VoidPlayer parent) {
        this.parent = parent;
    }

    public byte[] generateVerifyToken() {
        verifyToken = new byte[4];
        try {
            SecureRandom.getInstanceStrong().nextBytes(verifyToken);
            return verifyToken;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearVerifyToken() {
        verifyToken = null;
    }

    public boolean compareVerifyToken(byte[] token) {
        return Arrays.equals(verifyToken, token);
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }

}
