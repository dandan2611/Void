package net.gameinbox.voidserver.server.packet;

import io.netty.buffer.ByteBuf;
import net.gameinbox.voidserver.buffer.BufferWriter;

public class EncodedPacket {

    private ByteBuf buffer;

    public EncodedPacket(ByteBuf data) {
        BufferWriter writer = BufferWriter.newWriter();
        writer.writeVarInt(data.readableBytes());
        writer.getBuffer().writeBytes(data);
        buffer = writer.getBuffer();
    }

    public ByteBuf getEncodedPacket() {
        return buffer;
    }

    public void release() {
        buffer.release();
    }

}
