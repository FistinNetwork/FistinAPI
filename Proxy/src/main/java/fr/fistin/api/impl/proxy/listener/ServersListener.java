package fr.fistin.api.impl.proxy.listener;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.event.HydraEventListener;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.event.HydraServerStartedEvent;
import fr.fistin.hydra.api.server.event.HydraServerStoppedEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.logging.Level;

/**
 * Created by AstFaster
 * on 04/11/2022 at 16:13
 */
public class ServersListener implements HydraEventListener<HydraEvent> {

    @Override
    public void onEvent(HydraEvent event) {
        if (event instanceof HydraServerStartedEvent) {
            final HydraServerStartedEvent serverEvent = (HydraServerStartedEvent) event;
            final HydraServer server = serverEvent.getServer();
            final String serverName = server.getName();
            final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, new InetSocketAddress(serverName, 25565), "", false);

            ProxyServer.getInstance().getServers().put(serverName, serverInfo);

            IFistinAPIProvider.fistinAPI().getLogger().log(Level.INFO, "Added '" + serverName + "' server.");
        } else if (event instanceof HydraServerStoppedEvent) {
            final HydraServerStoppedEvent serverEvent = (HydraServerStoppedEvent) event;
            final HydraServer server = serverEvent.getServer();

            ProxyServer.getInstance().getServers().remove(server.getName());
        }
    }

}
