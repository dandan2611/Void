package net.gameinbox.voidserver.server.packet.login;

import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketServerEncryptionResponse extends PacketLogin<PacketServerEncryptionResponse> {

    public int sharedSecretLength;
    public byte[] sharedSecret;
    public int verifyTokenLength;
    public byte[] verifyToken;

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
    public PacketServerEncryptionResponse decode(BufferReader data) {
        sharedSecretLength = data.readVarInt();
        sharedSecret = new byte[sharedSecretLength];
        int readBytes = 0;
        while(readBytes < sharedSecretLength) {
            sharedSecret[readBytes] = data.getBuffer().readByte();
            readBytes++;
        }
        verifyTokenLength = data.readVarInt();
        verifyToken = new byte[verifyTokenLength];
        readBytes = 0;
        while(readBytes < verifyTokenLength) {
            verifyToken[readBytes] = data.getBuffer().readByte();
            readBytes++;
        }
        return this;
    }

    @Override
    public EncodedPacket encode() {
        return null;
    }

}
