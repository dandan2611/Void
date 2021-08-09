package net.gameinbox.voidserver.server.packet;

import io.netty.buffer.ByteBuf;
import net.gameinbox.voidserver.buffer.BufferReader;
import net.gameinbox.voidserver.server.packet.status.PacketClientPing;
import net.gameinbox.voidserver.server.packet.status.PacketServerListPing;
import net.gameinbox.voidserver.server.packet.status.PacketServerPing;
import net.gameinbox.voidserver.server.packet.status.PacketStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class PacketRegistry {

    private HashMap<CommunicationState, PacketBox<?>> packetRegistry;

    public PacketRegistry() {
        packetRegistry = new HashMap<>();
        packetRegistry.put(
                CommunicationState.STATUS,
                new PacketBox<PacketStatus<?>>()
                        .addPacket(new PacketServerListPing(), PacketServerListPing.class)
                        .addPacket(new PacketServerPing(), PacketServerPing.class)
                        .addPacket(new PacketClientPing(), PacketClientPing.class)
        );
    }

    public Packet<?> decodePacket(CommunicationState state, int id, PacketBound bound, BufferReader data) {
        Class<? extends Packet<?>> packetClass = getPacketById(state, id, bound);

        Constructor<?>[] constructors = packetClass.getDeclaredConstructors();

        Constructor<?> constructor = null;

        // Search for empty constructor
        for(Constructor<?> c : constructors)
            if(c.getGenericParameterTypes().length == 0)
                constructor = c;

        if(constructor == null)
            throw new NullPointerException("Unable to instantiate new packet " + packetClass.getSimpleName()
                    + " : no empty constructor found");

        try {
            constructor.setAccessible(true);

            // Create new instance of the packet and decode the data
            Packet<?> decodedPacket = (Packet<?>) constructor.newInstance();
            decodedPacket.decode(data);

            return decodedPacket;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Class<? extends Packet<?>> getPacketById(CommunicationState state, int id, PacketBound bound) {
        PacketBox<? extends Packet<?>> box = packetRegistry.get(state);

        if(box == null)
            throw new IllegalArgumentException("State PacketBox not found in registry");

        Class<? extends Packet<?>> packetClass = box.getPacketById(id, bound);

        if(packetClass == null)
            throw new IllegalArgumentException("Packet id " + id + " not found");

        return packetClass;
    }

    public static class PacketBox<T extends Packet<?>> {

        public HashMap<Integer, HashMap<PacketBound, Class<? extends T>>> packets;

        protected PacketBox() {
            packets = new HashMap<>();
        }

        protected PacketBox<T> addPacket(Packet<? extends T> packetInstance, Class<? extends T> packetClass) {
            int packetId = packetInstance.id();
            packets.putIfAbsent(packetId, new HashMap<>());
            packets.get(packetId).put(packetInstance.packetBound(), packetClass);
            return this;
        }

        protected Class<? extends T> getPacketById(int id, PacketBound bound) {
            return packets.get(id).get(bound);
        }

    }

}
