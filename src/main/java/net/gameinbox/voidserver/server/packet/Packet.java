package net.gameinbox.voidserver.server.packet;

import io.netty.buffer.ByteBuf;
import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public abstract class Packet<T> {

    public abstract int id();

    public abstract ProtocolVersion[] supportedProtocols();

    public abstract T decode(BufferReader data);

    public abstract ByteBuf encode();

}
