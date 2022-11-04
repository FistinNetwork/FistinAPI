package fr.fistin.api.packet;

/**
 * Created by AstFaster
 * on 04/11/2022 at 15:54.<br>
 *
 * A {@link FistinPacketReceiver} is used to listen for incoming packets.<br>
 * To register one see {@link PacketManager#registerReceiver(String, FistinPacketReceiver)}
 */
public interface FistinPacketReceiver {

    /**
     * Receive an incoming packet
     *
     * @param packet The received {@linkplain FistinPacket packet}
     */
    void receive(FistinPacket packet);

}
