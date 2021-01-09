package fr.fistin.api.packets;

import fr.fistin.api.FistinAPI;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Separate {@link PacketManager} & {@link PacketProcessor}. PacketProcessor is an internal class.
 */
class PacketProcessor
{
    private final Map<Class<? extends FistinPacket>, Consumer<? extends FistinPacket>> packets = new IdentityHashMap<>();

    void registerPacket(Class<? extends FistinPacket> packet, Consumer<? extends FistinPacket> packetAction)
    {
        this.packets.putIfAbsent(packet, packetAction);
    }

    <P extends FistinPacket> void processPacket(P packet)
    {
        if(this.packets.containsKey(packet.getClass()))
        {
            final Consumer<P> action = (Consumer<P>)this.packets.get(packet.getClass());
            action.accept(packet);
        }
        else FistinAPI.getFistinAPI().getLogger().warning(String.format("Unknown packet: %s (%s).", packet.toString(), packet.getClass().getPackage().getName() + packet.getClass().getName()));
    }

    void free()
    {
        this.packets.clear();
    }
}
