package fr.fistin.api.packets;

import fr.fistin.api.FistinAPI;

import java.util.function.Consumer;

public class PacketManager
{
    private final PacketProcessor packetProcessor = new PacketProcessor();

    public <P extends FistinPacket> void registerPacket(Class<P> packet, Consumer<P> packetAction)
    {
        this.packetProcessor.registerPacket(packet, packetAction);
    }

    public <P extends FistinPacket> void sendPacket(P packet)
    {
        this.packetProcessor.processPacket(packet);
    }

    public void stop()
    {
        FistinAPI.getFistinAPI().getLogger().info("Stopping FistinAPI packet system.");
        this.packetProcessor.free();
    }
}
