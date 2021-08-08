package net.gameinbox.voidserver.server;

import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSyncThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSyncThread.class);

    private final ChannelFuture channelFuture;

    public ServerSyncThread(ChannelFuture channelFuture) {
        super("void-server-sync");
        this.channelFuture = channelFuture;
    }

    @Override
    public void run() {
        try {
            channelFuture.channel().closeFuture().sync();
            LOGGER.info("Server stopped");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
