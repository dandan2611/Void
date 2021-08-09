package net.gameinbox.voidserver.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.gameinbox.voidserver.server.handlers.NewChannelHandler;
import net.gameinbox.voidserver.server.handlers.PacketDecoder;

public class VoidChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final VoidNetworkingManager networkingManager;

    public VoidChannelInitializer(VoidNetworkingManager networkingManager) {
        this.networkingManager = networkingManager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new NewChannelHandler(networkingManager))
                .addLast(new PacketDecoder(networkingManager));
    }

}
