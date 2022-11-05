package fr.fistin.api.player;

import fr.fistin.api.IFistinAPIProvider;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/11/2022 at 10:20.<br>
 *
 * A session is created each time a player joins the network.<br>
 * A {@linkplain PlayerSession session} stores a lot of information used while the player is connected on the network.
 */
public interface PlayerSession {

    /**
     * Get the {@link UUID} of the player linked to the session.
     *
     * @return A {@link UUID}
     */
    UUID playerId();

    /**
     * Get the current server the player is connected to.
     *
     * @return A server name. E.g. lobby-1qs5x
     */
    String currentServer();

    /**
     * Set the current server the player is connected to.
     *
     * @param currentServer The new current server
     */
    void setCurrentServer(String currentServer);

    /**
     * Get the old server the player was connected to.
     *
     * @return A server name. E.g. lobby-1qs5x
     */
    String oldServer();

    /**
     * Set the old server the player was connected to.
     *
     * @param oldServer The new old server
     */
    void setOldServer(String oldServer);

    /**
     * Get the current proxy the player is connected to.
     *
     * @return A proxy name. E.g. proxy-5f98a
     */
    String currentProxy();

    /**
     * Set the current proxy the player is connected to
     *
     * @param currentProxy The new current proxy
     */
    void setCurrentProxy(String currentProxy);

    /**
     * Save the {@linkplain PlayerSession session} in cache.
     */
    default void save() {
        IFistinAPIProvider.fistinAPI().playersService().saveSession(this);
    }

}
