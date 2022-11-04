package fr.fistin.api.impl.server.listener;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.server.FistinServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by AstFaster
 * on 03/11/2022 at 21:44
 */
public class JoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final FistinServer server = IFistinAPIProvider.fistinAPI().server();

        server.handle().addPlayer(player.getUniqueId());
        server.update();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final FistinServer server = IFistinAPIProvider.fistinAPI().server();

        server.handle().removePlayer(player.getUniqueId());
        server.update();
    }

}
