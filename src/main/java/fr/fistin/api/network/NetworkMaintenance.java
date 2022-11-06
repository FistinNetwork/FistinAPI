package fr.fistin.api.network;

/**
 * Created by AstFaster
 * on 06/11/2022 at 08:51.<br>
 *
 * The {@link NetworkMaintenance} is used to trigger a new maintenance, check if one is currently running, etc.
 */
public interface NetworkMaintenance {

    /**
     * Enable the maintenance on the network.
     *
     * @param message The message to display to players
     */
    void enable(String message);

    /**
     * Disable the maintenance on the network.
     */
    void disable();

    /**
     * Get the message of the maintenance to display to players.
     *
     * @return A message
     */
    String message();

    /**
     * Check whether the maintenance is enabled or not.
     *
     * @return <code>true</code> if the maintenance is enabled
     */
    boolean isEnabled();

}
