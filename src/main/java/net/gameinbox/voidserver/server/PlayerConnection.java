package net.gameinbox.voidserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.gameinbox.voidserver.player.VoidPlayer;
import net.gameinbox.voidserver.server.packet.CommunicationState;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.Packet;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

public class PlayerConnection {

    public final Channel channel;

    public CommunicationState communicationState;

    private VoidPlayer parent;

    // Temporary data

    private HashMap<String, Object> cache;

    // Encryption

    private Cipher cipher;
    private SecretKey sharedSecret;
    private byte[] verifyToken;

    public PlayerConnection(Channel channel, CommunicationState communicationState) {
        this.channel = channel;
        this.communicationState = communicationState;
        this.cache = new HashMap<>();
    }

    public void sendPacket(EncodedPacket packet) {
        if(channel.isWritable()) {
            ByteBuf dataBuf = packet.getEncodedPacket();
            byte[] data = dataBuf.array();
            dataBuf.release();
            if(hasEncryption())
                data = encryptPacket(data);
            channel.writeAndFlush(Unpooled.copiedBuffer(data));
        }
    }

    private byte[] encryptPacket(byte[] data) {
        try {
            return cipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public VoidPlayer getParent() {
        return parent;
    }

    public void setParent(VoidPlayer parent) {
        this.parent = parent;
    }

    public void setSharedSecret(byte[] secret) {
        this.sharedSecret = new SecretKeySpec(secret, "AES");
        try {
            this.cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, sharedSecret);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public SecretKey getSharedSecret() {
        return sharedSecret;
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

    public byte[] getVerifyToken() {
        return verifyToken;
    }

    public boolean hasEncryption() {
        return sharedSecret != null;
    }

    public HashMap<String, Object> getCache() {
        return cache;
    }

}
