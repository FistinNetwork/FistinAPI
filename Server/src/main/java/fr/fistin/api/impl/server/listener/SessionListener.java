package fr.fistin.api.impl.server.listener;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.player.PlayerSession;
import fr.fistin.api.player.PlayersService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

/**
 * Created by AstFaster
 * on 05/11/2022 at 12:52
 */
public class SessionListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerId = player.getUniqueId();
        final PlayersService playersService = IFistinAPIProvider.fistinAPI().playersService();
        final PlayerSession session = FistinAPIConfiguration.get().isLocal() ? playersService.createSession(playerId) : playersService.session(playerId);

        session.setOldServer(session.currentServer());
        session.setCurrentServer(IFistinAPIProvider.fistinAPI().server().handle().getName());
        session.save();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final PlayersService playersService = IFistinAPIProvider.fistinAPI().playersService();
        final PlayerSession session = playersService.session(player.getUniqueId());

        if (FistinAPIConfiguration.get().isLocal()) {
            playersService.deleteSession(session);
        }
    }

}
