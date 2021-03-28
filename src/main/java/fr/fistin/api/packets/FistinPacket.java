package fr.fistin.api.packets;

/**
 * Indicate to FistinAPI if the processing packet is a FistinPacket.
 *
 * "Fistin Packets" system is a simply "action - reaction" system with BungeeCord communications.
 */
public interface FistinPacket
{
    @Override
    String toString();
}
