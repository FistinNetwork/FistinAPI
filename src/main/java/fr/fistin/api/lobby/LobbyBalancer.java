package fr.fistin.api.lobby;

/**
 * Created by AstFaster
 * on 05/11/2022 at 20:50
 */
public interface LobbyBalancer {

    /** The hash used by the balancer */
    String HASH = "lobby-balancer";

    /**
     * Get the current best lobby name.
     *
     * @return A server name. E.g. lobby-4sq5b
     */
    String bestLobby();

}
