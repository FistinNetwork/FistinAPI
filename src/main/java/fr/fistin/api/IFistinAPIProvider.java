package fr.fistin.api;

import fr.fistin.api.database.IDatabaseManager;
import fr.fistin.api.hydra.ServerLauncher;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IStandalonePlugin;
import org.jetbrains.annotations.NotNull;

public interface IFistinAPIProvider extends IStandalonePlugin
{
    String NAMESPACE = "fistinapi";
    String BUNGEE_CORD_CHANNEL = "BungeeCord";

    @NotNull PacketManager packetManager();
    @NotNull IDatabaseManager databaseManager();
    @NotNull ServerLauncher serverLauncher();

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }
}
