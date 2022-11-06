package fr.fistin.api.packet;

import org.jetbrains.annotations.NotNull;

/**
 * Packet manager is a system used to communicate information through the network.<br>
 * E.g. Sending a packet to a given server or proxy to perform an action.
 */
public interface PacketManager
{

    /** The prefix of each channel */
    @NotNull String CHANNELS_PREFIX = "fistinapi@";

    /**
     * Send a {@linkplain FistinPacket packet} on a given network
     *
     * @param channel The channel where the packet will be sent
     * @param packet The pocket to send through the network
     * @param <P> The type of the packet to send
     */
    <P extends FistinPacket> void sendPacket(@NotNull String channel, @NotNull P packet);

    /**
     * Register a {@linkplain FistinPacketReceiver packet receiver} on a given channel
     *
     * @param channel The channel where the receiver will listen for incoming packets
     * @param receiver The receiver to register
     */
    void registerReceiver(@NotNull String channel, @NotNull FistinPacketReceiver receiver);

    <P extends FistinPacket> void registerSerializer(@NotNull Class<P> packetClass, @NotNull FistinPacket.Serializer<P> serializer);

    /**
     * Clear the packet manager.<br>
     * No more packets will be received and sent.
     */
    void clear();

}
