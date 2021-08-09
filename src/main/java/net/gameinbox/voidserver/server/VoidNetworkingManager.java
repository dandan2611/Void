package net.gameinbox.voidserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoidNetworkingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoidNetworkingManager.class);

    private final int serverPort;

    private EventLoopGroup acceptationGroup;
    private EventLoopGroup processingGroup;

    private ChannelFuture channelFuture;

    public VoidNetworkingManager(int serverPort) {
        this.serverPort = serverPort;
    }

    public void init() {
        // Acceptation group
        acceptationGroup = new NioEventLoopGroup();

        // Processing group
        processingGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(acceptationGroup, processingGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new VoidChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channelFuture = serverBootstrap.bind(serverPort).sync();

            ServerSyncThread serverThread = new ServerSyncThread(channelFuture);
            serverThread.start();

            LOGGER.info("Server started!");
        }
        catch (Exception exception) {
            LOGGER.error("UNABLE TO START VOID NETTY SERVER");
            exception.printStackTrace();
            processingGroup.shutdownGracefully();
            acceptationGroup.shutdownGracefully();
        }
    }

    public void close() {
        try {
            channelFuture.channel().close().sync();
            channelFuture.channel().parent().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processingGroup.shutdownGracefully();
        acceptationGroup.shutdownGracefully();
    }

}
