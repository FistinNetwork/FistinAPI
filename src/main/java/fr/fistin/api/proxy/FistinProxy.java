package fr.fistin.api.proxy;

import fr.fistin.hydra.api.proxy.HydraProxy;

/**
 * Created by AstFaster
 * on 03/11/2022 at 20:05
 *
 * Represents the abstraction of the proxy running Fistin API.<br>
 * It will not work if Fistin API is not using Hydra systems or not running on a proxy.
 */
public interface FistinProxy {

    /**
     * Get the handle of the proxy.<br>
     * Handle is used to get name, type, change state, etc.
     *
     * @return A {@link HydraProxy} object
     */
    HydraProxy handle();

    /**
     * Update the proxy information to Hydra.
     */
    void update();

}
