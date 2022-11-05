package fr.fistin.api.impl.common.player;

import fr.fistin.api.player.PlayerSession;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/11/2022 at 11:26
 */
public class PlayerSessionImpl implements PlayerSession {

    private final UUID playerId;

    private String currentServer;
    private String oldServer;

    private String currentProxy;

    public PlayerSessionImpl(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public UUID playerId() {
        return this.playerId;
    }

    @Override
    public String currentServer() {
        return this.currentServer;
    }

    @Override
    public void setCurrentServer(String currentServer) {
        this.currentServer = currentServer;
    }

    @Override
    public String oldServer() {
        return this.oldServer;
    }

    @Override
    public void setOldServer(String oldServer) {
        this.oldServer = oldServer;
    }

    @Override
    public String currentProxy() {
        return this.currentProxy;
    }

    @Override
    public void setCurrentProxy(String currentProxy) {
        this.currentProxy = currentProxy;
    }

}
