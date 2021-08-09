package net.gameinbox.voidserver.server.packet.status;

import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketServerListPing extends PacketStatus<PacketServerListPing> {

    public int protocolVersion;
    public String serverAddress;
    public short serverPort;
    public int nextState;
    public String username;

    @Override
    public int id() {
        return 0x00;
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
    public PacketServerListPing decode(BufferReader data) {
        protocolVersion = data.readVarInt();
        serverAddress = data.readString();
        serverPort = data.getBuffer().readShort();
        nextState = data.readVarInt();
        if(nextState == 2) {
            int i1 = data.readVarInt(); // Unknown values
            int i2 = data.readVarInt(); // What are these ?
            username = data.readString();
        }
        return this;
    }

    @Override
    public EncodedPacket encode() {
        return null;
    }

}
