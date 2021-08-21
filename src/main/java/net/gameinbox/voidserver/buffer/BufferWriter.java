package net.gameinbox.voidserver.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class BufferWriter {

    private final ByteBuf buffer;

    public BufferWriter(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public static BufferWriter newWriter() {
        return new BufferWriter(Unpooled.buffer());
    }

    public static BufferWriter newWriter(ByteBuf buffer) {
        return new BufferWriter(buffer);
    }

    public BufferWriter writeVarInt(int value) {
        do {
            byte currentByte = (byte) (value & 0b01111111);

            value >>>= 7;
            if (value != 0) currentByte |= 0b10000000;

            buffer.writeByte(currentByte);
        } while (value != 0);
        return this;
    }

    public BufferWriter writeVarLong(long value) {
        do {
            byte currentByte = (byte) (value & 0b01111111);

            value >>>= 7;
            if (value != 0) currentByte |= 0b10000000;

            buffer.writeByte(currentByte);
        } while (value != 0);
        return this;
    }

    public BufferWriter writeString(boolean hasLengthPrefix, String str) {
        if(hasLengthPrefix)
            writeVarInt(str.length());
        buffer.writeCharSequence(str, StandardCharsets.UTF_8);
        return this;
    }

    public void release() {
        buffer.release();
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

}
