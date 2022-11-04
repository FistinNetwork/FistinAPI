package fr.fistin.api.impl.proxy.listener;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.proxy.FistinProxy;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * Created by AstFaster
 * on 03/11/2022 at 21:44
 */
public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            final FistinProxy proxy = IFistinAPIProvider.fistinAPI().proxy();

            proxy.handle().addPlayer(player.getUniqueId());
            proxy.update();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        final FistinProxy proxy = IFistinAPIProvider.fistinAPI().proxy();

        proxy.handle().removePlayer(player.getUniqueId());
        proxy.update();
    }

}
