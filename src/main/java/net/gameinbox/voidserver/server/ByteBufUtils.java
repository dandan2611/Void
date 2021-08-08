package net.gameinbox.voidserver.server;

import io.netty.buffer.ByteBuf;

public class ByteBufUtils {

    public static int varInt(ByteBuf buf) {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public static void writeVarInt(int value, ByteBuf buf) {
        do {
            byte currentByte = (byte) (value & 0b01111111);

            value >>>= 7;
            if (value != 0) currentByte |= 0b10000000;

            buf.writeByte(currentByte);
        } while (value != 0);
    }

}
