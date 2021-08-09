package net.gameinbox.voidserver.server.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.gameinbox.voidserver.server.PlayerConnection;
import net.gameinbox.voidserver.server.VoidNetworkingManager;
import net.gameinbox.voidserver.server.packet.CommunicationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewChannelHandler.class);

    private final VoidNetworkingManager networkingManager;

    public NewChannelHandler(VoidNetworkingManager networkingManager) {
        this.networkingManager = networkingManager;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        LOGGER.info("New channel registered {}", channel.remoteAddress().toString());

        channel.attr(networkingManager.getConnectionKey()).set(
                new PlayerConnection(channel, CommunicationState.STATUS)
        );

        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        if(channel.attr(networkingManager.getConnectionKey()).get().communicationState == CommunicationState.PLAY) {
            PlayerConnection playerConnection = channel.attr(networkingManager.getConnectionKey()).get();

            networkingManager.getServer().unregisterPlayer(playerConnection.getParent());

            LOGGER.info("{} [{}] left the game", playerConnection.getParent().getUsername(), playerConnection.getParent().getUuid().toString());
        }

        super.channelUnregistered(ctx);
    }

}
