package fr.fistin.api.server;

import fr.fistin.hydra.api.server.HydraServer;

/**
 * Created by AstFaster
 * on 03/11/2022 at 20:05.<br>
 *
 * Represents the abstraction of the server running Fistin API.<br>
 * It will not work if Fistin API is not using Hydra systems or not running on a server.
 */
public interface FistinServer {

    /**
     * Get the handle of the server.<br>
     * Handle is used to get name, type, change state, etc.
     *
     * @return A {@link HydraServer} object
     */
    HydraServer handle();

    /**
     * Update the server information to Hydra.
     */
    void update();

}
