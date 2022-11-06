package fr.fistin.api.impl.common.lobby;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.lobby.LobbyBalancer;

import java.util.List;

/**
 * Created by AstFaster
 * on 05/11/2022 at 20:56
 */
public class LobbyBalancerImpl implements LobbyBalancer {

    @Override
    public String bestLobby() {
        return IFistinAPIProvider.fistinAPI().redis().get(jedis -> {
            final List<String> lobbies = jedis.zrange(REDIS_KEY, 0, 0);

            if (lobbies != null && lobbies.size() > 0) {
                return lobbies.get(0);
            }
            return null;
        });
    }

}
