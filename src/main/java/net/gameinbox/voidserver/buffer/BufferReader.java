package net.gameinbox.voidserver.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class BufferReader {

    private final ByteBuf buffer;

    private BufferReader(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public static BufferReader newReader(ByteBuf buffer) {
        return new BufferReader(buffer);
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = buffer.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public long readVarLong() {
        long value = 0;
        int bitOffset = 0;
        byte currentByte;
        do {
            if (bitOffset == 70) throw new RuntimeException("VarLong is too big");

            currentByte = buffer.readByte();
            value |= (currentByte & 0b01111111) << bitOffset;

            bitOffset += 7;
        } while ((currentByte & 0b10000000) != 0);

        return value;
    }

    public String readString() {
        int length = readVarInt();
        return buffer.readCharSequence(length, StandardCharsets.UTF_8).toString();
    }

    public void release() {
        buffer.release();
    }

    public ByteBuf getBuffer() {
        return buffer;
    }

}
