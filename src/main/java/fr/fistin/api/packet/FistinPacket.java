package fr.fistin.api.packet;

/**
 * Indicate to FistinAPI if the processing packet is a FistinPacket.<br>
 * "Fistin Packets" system is a simply "action - reaction" system with BungeeCord communications.<br>
 * A FistinPacket must be serializable in JSON format.
 */
public interface FistinPacket
{
    @Override
    String toString();
}
