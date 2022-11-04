package fr.fistin.api.packet.model;

import fr.fistin.api.packet.FistinPacket;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 04/11/2022 at 16:06.<br>
 *
 * Packet used to send a player to a given server.
 */
public class PlayerSendPacket implements FistinPacket {

    /** The {@link UUID} of the player to send */
    private final UUID playerId;
    /** The name of the server where the player will be sent */
    private final String serverName;

    /**
     * Default constructor of a {@link PlayerSendPacket}
     *
     * @param playerId The {@link UUID} of the player
     * @param serverName The name of the server
     */
    public PlayerSendPacket(UUID playerId, String serverName) {
        this.playerId = playerId;
        this.serverName = serverName;
    }

    /**
     * Get the {@link UUID} of the player to send
     *
     * @return A {@link UUID}
     */
    public UUID getPlayerId() {
        return this.playerId;
    }

    /**
     * Get the name of the server where the player will be sent
     *
     * @return A name. E.g lobby-c2q45
     */
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public String toString() {
        return "PlayerSendPacket{" +
                "playerId=" + playerId +
                ", serverName='" + serverName + '\'' +
                '}';
    }

}
