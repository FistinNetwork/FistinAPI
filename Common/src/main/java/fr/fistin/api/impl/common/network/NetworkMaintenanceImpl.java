package fr.fistin.api.impl.common.network;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.network.NetworkMaintenance;
import fr.fistin.api.utils.FistinAPIException;

/**
 * Created by AstFaster
 * on 06/11/2022 at 09:09
 */
public class NetworkMaintenanceImpl implements NetworkMaintenance {

    public static final String REDIS_KEY = "network:maintenance";

    private boolean enabled = false;
    private String message = null;

    @Override
    public void enable(String message) {
        if (this.enabled) {
            throw new FistinAPIException("Maintenance is already enabled!");
        }

        this.enabled = true;
        this.message = message;

        // TODO Trigger an event on the network (to kick online players)

        this.save();
    }

    @Override
    public void disable() {
        if (!this.enabled) {
            throw new FistinAPIException("Maintenance is already disabled!");
        }

        this.enabled = false;
        this.message = null;

        // TODO Trigger an event on the network (to kick online players)

        this.save();
    }

    public void save() {
        IFistinAPIProvider.fistinAPI()
                .redis()
                .process(jedis -> jedis.set(REDIS_KEY, IFistinAPIProvider.GSON.toJson(this)));
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
