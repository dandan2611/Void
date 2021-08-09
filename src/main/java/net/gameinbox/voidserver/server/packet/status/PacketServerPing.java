package net.gameinbox.voidserver.server.packet.status;

import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketServerPing extends PacketStatus<PacketServerPing> {

    public long value;

    @Override
    public int id() {
        return 0x01;
    }

    @Override
    public PacketBound packetBound() {
        return PacketBound.CLIENTBOUND;
    }

    @Override
    public ProtocolVersion[] supportedProtocols() {
        return new ProtocolVersion[] {
                ProtocolVersion.V1_17_1
        };
    }

    @Override
    public PacketServerPing decode(BufferReader data) {
        value = data.getBuffer().readLong();
        return this;
    }

    @Override
    public EncodedPacket encode() {
        return null;
    }
}
