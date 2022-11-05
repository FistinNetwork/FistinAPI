package fr.fistin.api.impl.server.listener;

import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SetupListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (FistinAPIConfiguration.get().isLocal()) {
            PluginProviders.getProvider(ILevelingProvider.class).initialize(event.getPlayer().getUniqueId());
        }
    }

}
