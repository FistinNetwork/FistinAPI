package fr.fistin.api;

import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IStandalonePlugin;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.proxy.FistinProxy;
import fr.fistin.api.redis.Redis;
import fr.fistin.api.server.FistinServer;
import fr.fistin.api.utils.FistinAPIException;
import fr.fistin.hydra.api.HydraAPI;
import org.jetbrains.annotations.NotNull;

public interface IFistinAPIProvider extends IStandalonePlugin
{

    /**
     * Get the Fistin API instance from plugin providers.
     *
     * @return The {@linkplain IFistinAPIProvider Fistin API} instance
     */
    @NotNull static IFistinAPIProvider fistinAPI()
    {
        return PluginProviders.getProvider(IFistinAPIProvider.class);
    }

    /** The namespace of Fistin API */
    @NotNull String NAMESPACE = "fistinapi";

    /**
     * Get the packet manager instance.<br>
     * {@linkplain PacketManager Packet manager} is used to send packets through the network.
     *
     * @return The {@link PacketManager} instance
     */
    @NotNull PacketManager packetManager();

    /**
     * Get the database manager instance.<br>
     * {@linkplain DatabaseManager Database manager} is used to interact with SQL database.
     *
     * @return The {@link DatabaseManager} instance
     */
    @NotNull DatabaseManager databaseManager();

    /**
     * Get the Redis connection instance
     *
     * @return The {@link Redis} instance
     */
    @NotNull Redis redis();

    /**
     * Get the {@link HydraAPI} instance.<br>
     * This API is used to interact with Hydra (standalone application) by creating/stopping servers or proxies for example.
     *
     * @return The {@link HydraAPI} instance
     */
    HydraAPI hydra();

    /**
     * Get the server instance if Fistin API is running on a server.
     *
     * @return THe {@link FistinServer} instance; or <code>null</code> if Fistin API is not on a server
     */
    default FistinServer server() {
        throw new FistinAPIException("Fistin API is not running in server mode.");
    }

    /**
     * Get the proxy instance if Fistin API is running on a proxy.
     *
     * @return THe {@link FistinProxy} instance; or <code>null</code> if Fistin API is not on a proxy
     */
    default FistinProxy proxy() {
        throw new FistinAPIException("Fistin API is not running in proxy mode.");
    }

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }

}
