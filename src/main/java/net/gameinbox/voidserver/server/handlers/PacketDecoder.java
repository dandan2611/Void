package net.gameinbox.voidserver.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.PlayerConnection;
import net.gameinbox.voidserver.server.VoidNetworkingManager;
import net.gameinbox.voidserver.server.packet.Packet;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.packet.PacketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDecoder.class);

    private VoidNetworkingManager networkingManager;
    private PacketRegistry registry;

    public PacketDecoder(VoidNetworkingManager networkingManager) {
        this.networkingManager = networkingManager;
        this.registry = networkingManager.getPacketRegistry();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        ByteBuf buf = (ByteBuf) msg;

        BufferReader reader = BufferReader.newReader(buf.asReadOnly());

        LOGGER.info("{} > Data received", channel.remoteAddress().toString());

        try {
            int packetLength = reader.readVarInt();
            int packetId = reader.readVarInt();

            PlayerConnection playerConnection = channel.attr(networkingManager.getConnectionKey()).get();

            Packet<?> decodedPacket = registry.decodePacket(playerConnection.communicationState, packetId,
                    PacketBound.CLIENTBOUND, reader);

            networkingManager.getPacketQueue().process(playerConnection, decodedPacket);

            LOGGER.info("{} > Packet received {}", channel.remoteAddress().toString(), decodedPacket.getClass()
                    .getSimpleName());

            super.channelRead(ctx, msg);
        }
        catch (Exception exception) {
            LOGGER.info("{} > Unable to decode packet sent {}", channel.remoteAddress().toString(),
                    exception.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

}
