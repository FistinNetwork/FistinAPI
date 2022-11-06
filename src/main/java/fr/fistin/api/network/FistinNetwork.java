package fr.fistin.api.network;

import fr.fistin.api.eventbus.FistinEvent;
import fr.fistin.api.eventbus.FistinEventBus;
import fr.fistin.api.network.counter.NetworkCounter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Created by AstFaster
 * on 06/11/2022 at 08:48.<br>
 *
 * Represents the information of the network: players, slots, maintenance, etc.
 */
public interface FistinNetwork {

    /**
     * Get the {@linkplain FistinEventBus event bus} used to trigger events through the network.<br>
     * All application running (on the same network) with Fistin API will trigger listeners for a triggered event.<br>
     * All triggered {@link FistinEvent} must be serializable in JSON format!
     *
     * @return A {@link FistinEventBus} instance
     */
    @NotNull FistinEventBus<Supplier<? extends FistinEvent>> eventBus();

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
