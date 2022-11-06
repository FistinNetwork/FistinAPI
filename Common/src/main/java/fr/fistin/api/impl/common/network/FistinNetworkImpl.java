package fr.fistin.api.impl.common.network;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.eventbus.FistinEvent;
import fr.fistin.api.eventbus.FistinEventBus;
import fr.fistin.api.impl.common.network.counter.NetworkCounterImpl;
import fr.fistin.api.network.FistinNetwork;
import fr.fistin.api.network.NetworkMaintenance;
import fr.fistin.api.network.counter.NetworkCounter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Created by AstFaster
 * on 06/11/2022 at 09:09
 */
public class FistinNetworkImpl implements FistinNetwork {

    private final NetworkEventBus eventBus;
    private final NetworkCounter counter;

    public FistinNetworkImpl() {
        this.eventBus = new NetworkEventBus();
        this.counter = new NetworkCounterImpl();
    }

    @Override
    public @NotNull FistinEventBus<Supplier<? extends FistinEvent>> eventBus() {
        return this.eventBus;
    }

    @Override
    public @NotNull NetworkCounter counter() {
        return this.counter;
    }

    @Override
    public @NotNull NetworkMaintenance maintenance() {
        return IFistinAPIProvider.fistinAPI().redis().get(jedis -> {
            final String json = jedis.get(NetworkMaintenanceImpl.REDIS_KEY);

            if (json == null) {
                final NetworkMaintenanceImpl maintenance = new NetworkMaintenanceImpl();

                maintenance.save();

                return maintenance;
            }
            return IFistinAPIProvider.GSON.fromJson(json, NetworkMaintenanceImpl.class);
        });
    }

}