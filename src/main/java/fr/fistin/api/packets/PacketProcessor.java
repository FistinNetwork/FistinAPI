package fr.fistin.api.packets;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Separate {@link PacketManager} & {@link PacketProcessor}. PacketProcessor is an internal class. Changes may not affect PacketManager class.
 * Immutable class.
 */
final class PacketProcessor
{
    private final Map<Class<? extends FistinPacket>, Consumer<? extends FistinPacket>> packets = new IdentityHashMap<>();

    final void registerPacket(Class<? extends FistinPacket> packet, Consumer<? extends FistinPacket> packetAction)
    {
        this.packets.putIfAbsent(packet, packetAction);
    }

    @SuppressWarnings("unchecked")
    final <P extends FistinPacket> void processPacket(P packet)
    {
        if(this.packets.containsKey(packet.getClass()))
        {
            final Consumer<P> action = (Consumer<P>)this.packets.get(packet.getClass());
            action.accept(packet);
        }
        else System.err.printf("Unknown packet: %s (%s).%n", packet.toString(), packet.getClass().getName());
    }

    final void free()
    {
        this.packets.clear();
    }
}
