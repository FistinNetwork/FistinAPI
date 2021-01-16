package fr.fistin.api.packets;

import java.util.function.Consumer;

public class PacketManager
{
    private final PacketProcessor packetProcessor = new PacketProcessor();
    private boolean started;

    public PacketManager()
    {
        this.started = true;
    }

    public <P extends FistinPacket> void registerPacket(Class<P> packet, Consumer<P> packetAction)
    {
        if(this.assertStarted()) this.packetProcessor.registerPacket(packet, packetAction);
    }

    public <P extends FistinPacket> void sendPacket(P packet)
    {
        if(this.assertStarted()) this.packetProcessor.processPacket(packet);
    }

    private boolean assertStarted()
    {
        if(this.started)
            return true;
        System.err.println("PacketManager isn't started !");
        return false;
    }

    public void stop()
    {
        this.packetProcessor.free();
        this.started = false;
    }
}
