package fr.fistin.api.impl;

import fr.fistin.api.packets.FistinPacket;
import fr.fistin.api.packets.PacketException;
import fr.fistin.api.packets.PacketManager;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PacketManagerImpl implements PacketManager
{
    private final PacketProcessorImpl packetProcessor = new PacketProcessorImpl();
    private boolean started;

    public PacketManagerImpl()
    {
        this.started = true;
    }

    @Override
    public <P extends FistinPacket> void registerPacket(Class<P> packet, Consumer<P> packetAction)
    {
        if(this.assertStarted()) this.packetProcessor.registerPacket(packet, packetAction);
    }

    @Override
    public <P extends FistinPacket> void sendPacket(P packet)
    {
        if(this.assertStarted()) this.packetProcessor.processPacket(packet);
    }

    private boolean assertStarted()
    {
        if(this.started) return true;
        else throw new PacketException("PacketManager isn't started !");
    }

    @Override
    public void clear()
    {
        this.packetProcessor.clear();
        this.started = false;
    }

    static final class PacketProcessorImpl
    {
        private final Map<Class<? extends FistinPacket>, Consumer<? extends FistinPacket>> packets = new IdentityHashMap<>();

        void registerPacket(Class<? extends FistinPacket> packet, Consumer<? extends FistinPacket> packetAction)
        {
            this.packets.putIfAbsent(packet, packetAction);
        }

        @SuppressWarnings("unchecked")
        <P extends FistinPacket> void processPacket(@NotNull P packet)
        {
            if(this.packets.containsKey(packet.getClass()))
            {
                final Consumer<P> action = (Consumer<P>)this.packets.get(packet.getClass());
                action.accept(packet);
            }
            else throw new PacketException("Unknown packet: %s (%s).%n", packet.toString(), packet.getClass().getName());
        }

        void clear()
        {
            this.packets.clear();
        }
    }
}
