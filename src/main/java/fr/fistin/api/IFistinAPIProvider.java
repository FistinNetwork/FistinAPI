package fr.fistin.api;

import fr.fistin.api.database.IDatabaseManager;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IBukkitPluginProvider;
import org.jetbrains.annotations.NotNull;

public interface IFistinAPIProvider extends IBukkitPluginProvider
{
    String NAMESPACE = "fistinapi";
    String BUNGEE_CORD_CHANNEL = "BungeeCord";

    @NotNull PacketManager packetManager();
    @NotNull IDatabaseManager databaseManager();

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }
}
