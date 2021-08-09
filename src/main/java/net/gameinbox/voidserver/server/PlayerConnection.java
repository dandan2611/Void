package net.gameinbox.voidserver.server;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.gameinbox.voidserver.server.packet.CommunicationState;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.Packet;

public class PlayerConnection {

    private final Channel channel;

    public CommunicationState communicationState;

    public PlayerConnection(Channel channel, CommunicationState communicationState) {
        this.channel = channel;
        this.communicationState = communicationState;
    }

    public void sendPacket(EncodedPacket packet) {
        if(channel.isWritable()) {
            channel.writeAndFlush(packet.getEncodedPacket());
        }
    }

}
