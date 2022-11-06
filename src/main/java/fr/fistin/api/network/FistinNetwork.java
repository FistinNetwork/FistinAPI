package fr.fistin.api.network;

import fr.fistin.api.network.counter.NetworkCounter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by AstFaster
 * on 06/11/2022 at 08:48.<br>
 *
 * Represents the information of the network: players, slots, maintenance, etc.
 */
public interface FistinNetwork {

    /**
     * Get the counter of players of the network.
     *
     * @return The {@link NetworkCounter} instance
     */
    @NotNull NetworkCounter counter();

    /**
     * Get the maintenance of the network.<br>
     * It can be used to trigger a new one or to stop the current one.
     *
     * @return The {@link NetworkMaintenance} instance
     */
    @NotNull NetworkMaintenance maintenance();

}
