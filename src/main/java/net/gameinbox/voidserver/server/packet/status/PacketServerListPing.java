package net.gameinbox.voidserver.server.packet.status;

import io.netty.buffer.ByteBuf;
import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketServerListPing extends PacketStatus<PacketServerListPing> {

    public int protocolVersion;
    public String serverAddress;
    public short serverPort;
    public int nextState;

    @Override
    public int id() {
        return 0x00;
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

        return this;
    }

    @Override
    public ByteBuf encode() {
        return null;
    }

}
