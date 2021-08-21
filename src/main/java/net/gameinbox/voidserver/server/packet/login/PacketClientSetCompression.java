package net.gameinbox.voidserver.server.packet.login;

import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.buffer.BufferWriter;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketClientSetCompression extends PacketLogin<PacketClientSetCompression> {

    public int thresold;

    public PacketClientSetCompression() {}

    @Override
    public int id() {
        return 0x03;
    }

    @Override
    public PacketBound packetBound() {
        return PacketBound.SERVERBOUND;
    }

    @Override
    public ProtocolVersion[] supportedProtocols() {
        return new ProtocolVersion[] {
                ProtocolVersion.V1_17_1
        };
    }

    @Override
    public PacketClientSetCompression decode(BufferReader data) {
        return null;
    }

    @Override
    public EncodedPacket encode() {
        BufferWriter writer = BufferWriter.newWriter();

        writer.writeVarInt(thresold);

        EncodedPacket encodedPacket = new EncodedPacket(writer.getBuffer());

        writer.release();

        return encodedPacket;
    }

}
