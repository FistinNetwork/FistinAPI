package fr.fistin.api.impl.common.player;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.player.PlayerSession;
import fr.fistin.api.player.PlayersService;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/11/2022 at 11:26
 */
public class PlayersServiceImpl implements PlayersService {

    private static final String HASH = "players-sessions:";

    @Override
    public boolean isPlayerOnline(UUID playerId) {
        return IFistinAPIProvider.fistinAPI().redis().get(jedis -> jedis.exists(HASH + playerId.toString()));
    }

    @Override
    public PlayerSession session(UUID playerId) {
        return IFistinAPIProvider.fistinAPI().redis().get(jedis -> {
            final String json = jedis.get(HASH + playerId.toString());

            return json == null ? null : IFistinAPIProvider.GSON.fromJson(json, PlayerSessionImpl.class);
        });
    }

    @Override
    public PlayerSession createSession(UUID playerId) {
        final PlayerSession session = new PlayerSessionImpl(playerId);

        this.saveSession(session);

        return session;
    }

    @Override
    public void saveSession(PlayerSession session) {
        IFistinAPIProvider.fistinAPI().redis().process(jedis -> jedis.set(HASH + session.playerId().toString(), IFistinAPIProvider.GSON.toJson(session)));
    }

    @Override
    public void deleteSession(UUID playerId) {
        IFistinAPIProvider.fistinAPI().redis().process(jedis -> jedis.del(HASH + playerId.toString()));
    }

}