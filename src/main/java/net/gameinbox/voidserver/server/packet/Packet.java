package net.gameinbox.voidserver.server.packet;

import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public abstract class Packet<T> {

    public abstract int id();

    public abstract PacketBound packetBound();

    public abstract ProtocolVersion[] supportedProtocols();

    public abstract T decode(BufferReader data);

    public abstract EncodedPacket encode();

}
