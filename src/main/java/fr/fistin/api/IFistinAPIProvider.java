package fr.fistin.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.FistinEvent;
import fr.fistin.api.eventbus.FistinEventBus;
import fr.fistin.api.lobby.LobbyBalancer;
import fr.fistin.api.network.FistinNetwork;
import fr.fistin.api.packet.PacketManager;
import fr.fistin.api.player.PlayersService;
import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IStandalonePlugin;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.proxy.FistinProxy;
import fr.fistin.api.redis.Redis;
import fr.fistin.api.server.FistinServer;
import fr.fistin.api.utils.FistinAPIException;
import fr.fistin.hydra.api.HydraAPI;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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
    /** The {@link  Gson} instance used by Fistin API */
    @NotNull Gson GSON = new GsonBuilder().create();

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

    /**
     * Get the packet manager instance.<br>
     * {@linkplain PacketManager Packet manager} is used to send packets through the network.
     *
     * @return The {@link PacketManager} instance
     */
    @NotNull PacketManager packetManager();

    /**
     * Get Fistin API event bus instance.
     *
     * @return The {@link FistinEventBus} instance
     */
    @NotNull FistinEventBus<Supplier<? extends FistinEvent>> eventBus();

    /**
     * Get the network instance.<br>
     * {@linkplain FistinNetwork Network} is used to fetch network information, interact with maintenance, etc.
     *
     * @return The {@link FistinNetwork} instance
     */
    @NotNull FistinNetwork network();

    /**
     * Get the players service instance.<br>
     * {@linkplain PlayersService Players service} is used to interact with players data.
     *
     * @return The {@link PlayersService} instance
     */
    @NotNull PlayersService playersService();

    /**
     * Get the lobby balancer instance.<br>
     * {@linkplain LobbyBalancer Lobby balancer} is used to get the current best lobby on the network.
     *
     * @return The {@link LobbyBalancer} instance
     */
    @NotNull LobbyBalancer lobbyBalancer();

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }

}
