package fr.fistin.api.packets;

import java.util.function.Consumer;

public interface PacketManager
{
    <P extends FistinPacket> void registerPacket(Class<P> packet, Consumer<P> packetAction);
    <P extends FistinPacket> void sendPacket(P packet);
    void clear();
}
