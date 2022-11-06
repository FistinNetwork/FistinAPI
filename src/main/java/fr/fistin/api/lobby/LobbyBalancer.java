package fr.fistin.api.lobby;

/**
 * Created by AstFaster
 * on 05/11/2022 at 20:50.<br>
 *
 * The {@link LobbyBalancer} is used to interact with the current best lobby where players will be connected.
 */
public interface LobbyBalancer {

    /** The Redis key used by the balancer */
    String REDIS_KEY = "lobby-balancer";

    /**
     * Get the current best lobby name.
     *
     * @return A server name. E.g. lobby-4sq5b
     */
    String bestLobby();

}
