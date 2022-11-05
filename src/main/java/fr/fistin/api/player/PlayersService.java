package fr.fistin.api.player;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/11/2022 at 10:28.<br>
 *
 * This service is the abstract layer of players information fetching.<br>
 * It can be used to get any information about a given player.
 */
public interface PlayersService {

    /**
     * Check whether a player is online on the network or not.
     *
     * @param playerId The {@link UUID} of the player
     * @return <code>true</code> if he is online and <code>false</code> otherwise
     */
    boolean isPlayerOnline(UUID playerId);

    /**
     * Get the network session of a connected player.<br>
     * It might return <code>null</code> if the given player doesn't exist or is not connected on the network.
     *
     * @param playerId The {@link UUID} of the player
     * @return The {@linkplain PlayerSession session} of the given player or <code>null</code>
     */
    PlayerSession session(UUID playerId);

    /**
     * Create a {@link PlayerSession} for a given player.
     *
     * @param playerId The {@link UUID} of the player
     * @return The created {@linkplain PlayerSession session}
     */
    PlayerSession createSession(UUID playerId);

    /**
     * Save a given session in cache.
     *
     * @param session The {@linkplain PlayerSession session} to save
     */
    void saveSession(PlayerSession session);

    /**
     * Delete the session of a given player
     *
     * @param playerId The {@link UUID} of the player
     */
    void deleteSession(UUID playerId);

    /**
     * Delete a given session from cache.
     *
     * @param session The session to remove
     */
    default void deleteSession(PlayerSession session) {
        this.deleteSession(session.playerId());
    }

}
