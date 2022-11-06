package fr.fistin.api.impl.common.network.counter;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.network.counter.NetworkCategoryCounter;
import fr.fistin.hydra.api.server.HydraServer;

/**
 * Created by AstFaster
 * on 06/11/2022 at 09:19
 */
public class NetworkCategoryCounterImpl implements NetworkCategoryCounter {

    private final String category;

    public NetworkCategoryCounterImpl(String category) {
        this.category = category;
    }

    @Override
    public int getPlayers() {
        if (FistinAPIConfiguration.get().isLocal()) {
            return 0;
        }

        int players = 0;

        for (HydraServer server : IFistinAPIProvider.fistinAPI().hydra().getServersService().getServers(this.category)) {
            players += server.getPlayers().size();
        }
        return players;
    }

    @Override
    public int getPlayers(String type) {
        if (FistinAPIConfiguration.get().isLocal()) {
            return 0;
        }

        int players = 0;

        for (HydraServer server : IFistinAPIProvider.fistinAPI().hydra().getServersService().getServers(this.category)) {
            if (!server.getGameType().equals(type)) {
                continue;
            }

            players += server.getPlayers().size();
        }
        return players;
    }

}
