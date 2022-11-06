package fr.fistin.api.impl.common.network.counter;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.network.counter.NetworkCategoryCounter;
import fr.fistin.api.network.counter.NetworkCounter;
import fr.fistin.hydra.api.proxy.HydraProxy;

/**
 * Created by AstFaster
 * on 06/11/2022 at 09:19
 */
public class NetworkCounterImpl implements NetworkCounter {

    @Override
    public int getPlayers() {
        if (FistinAPIConfiguration.get().isLocal()) {
            return 0;
        }

        int players = 0;

        for (HydraProxy proxy : IFistinAPIProvider.fistinAPI().hydra().getProxiesService().getProxies()) {
            players += proxy.getPlayers().size();
        }
        return players;
    }

    @Override
    public NetworkCategoryCounter getCategory(String name) {
        return new NetworkCategoryCounterImpl(name);
    }

}
