package fr.fistin.api.impl.proxy.listener;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.player.PlayerSession;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * Created by AstFaster
 * on 05/11/2022 at 12:52
 */
public class SessionListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(ServerConnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            // Initializing a session for the player that just connected on the network
            final PlayerSession session = IFistinAPIProvider.fistinAPI()
                    .playersService()
                    .createSession(player.getUniqueId());

            session.setCurrentProxy(IFistinAPIProvider.fistinAPI().proxy().handle().getName());
            session.save();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();

        // Remove session from cache because player is no longer connected on the network
        IFistinAPIProvider.fistinAPI()
                .playersService()
                .deleteSession(player.getUniqueId());
    }

}
