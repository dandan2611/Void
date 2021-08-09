package net.gameinbox.voidserver.server;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import net.gameinbox.voidserver.player.VoidPlayer;
import net.gameinbox.voidserver.server.packet.CommunicationState;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.Packet;

public class PlayerConnection {

    public final Channel channel;

    public CommunicationState communicationState;

    private VoidPlayer parent;

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

}
