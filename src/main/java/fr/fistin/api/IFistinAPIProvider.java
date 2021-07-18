package fr.fistin.api;

import fr.fistin.api.database.IDatabaseManager;
import fr.fistin.api.hydra.ServerLauncher;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IStandalonePlugin;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.jetbrains.annotations.NotNull;

public interface IFistinAPIProvider extends IStandalonePlugin
{
    @NotNull static IFistinAPIProvider fistinAPI()
    {
        return PluginProviders.getProvider(IFistinAPIProvider.class);
    }

    @NotNull String NAMESPACE = "fistinapi";
    @NotNull String BUNGEE_CORD_CHANNEL = "BungeeCord";

    @NotNull PacketManager packetManager();
    @NotNull IDatabaseManager databaseManager();
    @NotNull ServerLauncher serverLauncher();

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }
}
