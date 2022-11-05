package fr.fistin.api.impl.proxy.listener;

import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.plugin.providers.PluginProviders;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SetupListener implements Listener
{

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            PluginProviders.getProvider(ILevelingProvider.class).initialize(event.getPlayer().getUniqueId());
        }
    }

}
