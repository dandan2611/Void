package net.gameinbox.voidserver.server.packet.login;

import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.buffer.BufferWriter;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketClientEncryptionRequest extends PacketLogin<PacketClientEncryptionRequest> {

    public String serverId;
    public int publicKeyLength;
    public byte[] publicKey;
    public int verifyTokenLength;
    public byte[] verifyToken;

    @Override
    public int id() {
        return 0x01;
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
    public PacketClientEncryptionRequest decode(BufferReader data) {
        return null;
    }

    @Override
    public EncodedPacket encode() {
        BufferWriter writer = BufferWriter.newWriter();

        writer.writeVarInt(id());
        writer.writeString(true, serverId);
        writer.writeVarInt(publicKeyLength);
        writer.getBuffer().writeBytes(publicKey);
        writer.writeVarInt(verifyTokenLength);
        writer.getBuffer().writeBytes(verifyToken);

        EncodedPacket encodedPacket = new EncodedPacket(writer.getBuffer());

        writer.release();

        return encodedPacket;
    }
}
