package net.gameinbox.voidserver.server.packet.status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.buffer.BufferWriter;
import net.gameinbox.voidserver.server.packet.EncodedPacket;
import net.gameinbox.voidserver.server.packet.PacketBound;
import net.gameinbox.voidserver.server.protocol.ProtocolVersion;

public class PacketClientServerInfo extends PacketStatus<PacketClientServerInfo> {

    public String versionName;
    public int protocol;
    public int onlinePlayers;
    public int maxPlayers;
    public String descriptionText;

    @Override
    public int id() {
        return 0x00;
    }

    @Override
    public PacketBound packetBound() {
        return PacketBound.SERVERBOUND;
    }

    @Override
    public ProtocolVersion[] supportedProtocols() {
        return new ProtocolVersion[0];
    }

    @Override
    public PacketClientServerInfo decode(BufferReader data) {
        return null;
    }

    @Override
    public EncodedPacket encode() {
        Gson gson = new Gson();

        JsonObject versionObject = new JsonObject();
        versionObject.addProperty("name", versionName);
        versionObject.addProperty("protocol", protocol);

        JsonObject playersObject = new JsonObject();
        playersObject.addProperty("max", maxPlayers);
        playersObject.addProperty("online", onlinePlayers);
        playersObject.add("sample", new JsonArray());

        JsonObject descriptionObject = new JsonObject();
        descriptionObject.addProperty("text", descriptionText);

        JsonObject mainObject = new JsonObject();
        mainObject.add("version", versionObject);
        mainObject.add("players", playersObject);
        mainObject.add("description", descriptionObject);

        BufferWriter bufferWriter = BufferWriter.newWriter();

        bufferWriter.writeVarInt(id());
        bufferWriter.writeString(true, mainObject.toString());

        EncodedPacket encodedPacket = new EncodedPacket(bufferWriter.getBuffer());

        bufferWriter.release();

        return encodedPacket;
    }

}
