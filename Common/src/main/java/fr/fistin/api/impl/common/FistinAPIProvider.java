package fr.fistin.api.impl.common;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.eventbus.DefaultEventBus;
import fr.fistin.api.eventbus.FistinEvent;
import fr.fistin.api.eventbus.FistinEventBus;
import fr.fistin.api.impl.common.database.DatabaseManagerImpl;
import fr.fistin.api.impl.common.lobby.LobbyBalancerImpl;
import fr.fistin.api.impl.common.network.FistinNetworkImpl;
import fr.fistin.api.impl.common.packet.PacketManagerImpl;
import fr.fistin.api.impl.common.player.PlayersServiceImpl;
import fr.fistin.api.impl.common.redis.RedisImpl;
import fr.fistin.api.lobby.LobbyBalancer;
import fr.fistin.api.network.FistinNetwork;
import fr.fistin.api.packet.PacketManager;
import fr.fistin.api.player.PlayersService;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.redis.Redis;
import fr.fistin.api.utils.PluginLocation;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.data.HydraEnv;
import fr.fistin.hydra.api.protocol.data.RedisData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by AstFaster
 * on 04/11/2022 at 14:06
 */
public class FistinAPIProvider implements IFistinAPIProvider {

    protected FistinAPIConfiguration configuration;
    protected Logger logger;

    protected DatabaseManager databaseManager;

    protected RedisImpl redis;

    protected HydraEnv hydraEnv;
    protected HydraAPI hydraAPI;

    protected PacketManager packetManager;
    protected FistinEventBus<Supplier<? extends FistinEvent>> eventBus;
    protected FistinNetwork network;
    protected PlayersService playersService;
    protected LobbyBalancer lobbyBalancer;

    public void enable(FistinAPIConfiguration configuration, Logger logger) {
        this.logger = logger;

        this.logger.log(Level.INFO, "==========================");
        this.logger.log(Level.INFO, "Hello, Starting Fistin API");

        this.configuration = configuration;

        this.preInit();
        this.init();
        this.postInit();
    }

    protected void preInit()
    {
        PluginProviders.setProvider(IFistinAPIProvider.class, this);
        PluginProviders.setProvider(ILevelingProvider.class, new LevelingProvider());
        ConfigurationProviders.setConfig(FistinAPIConfiguration.class, this.configuration);
    }

    protected void init()
    {
        final boolean hydraEnabled = this.configuration.isHydraEnabled();

        if (hydraEnabled) {
            this.hydraEnv = HydraEnv.load();
        }

        final RedisData redisConfig = hydraEnabled ? this.hydraEnv.getRedis() : this.configuration.getRedis();

        this.redis = new RedisImpl(redisConfig);

        if (!this.redis.connect()) {
            System.exit(-1);
            return;
        }

        this.databaseManager = new DatabaseManagerImpl();

        if (hydraEnabled) {
            final String appName = hydraEnv.getName();

            this.hydraAPI = new HydraAPI.Builder(HydraAPI.Type.SERVER, appName)
                    .withRedis(this.redis)
                    .withLogger(this.getLogger())
                    .withLogHeader("Hydra")
                    .build();
            this.hydraAPI.start();
        }

        this.packetManager = new PacketManagerImpl();
        this.eventBus = new DefaultEventBus();
        this.network = new FistinNetworkImpl();
        this.playersService = new PlayersServiceImpl();
        this.lobbyBalancer = new LobbyBalancerImpl();
    }

    protected void postInit()
    {

    }

    public void disable() {
        this.databaseManager.clear();
        this.packetManager.clear();

        if (this.configuration.isHydraEnabled()) {
            this.hydraAPI.stop("Fistin API normal shutdown");
        }

        this.redis.disconnect();

        PluginProviders.clear();
        ConfigurationProviders.clear();
        PluginLocation.clear();

        this.logger.log(Level.INFO, "Stopped Fistin API, bye!");
        this.logger.log(Level.INFO, "=========================");
    }

    @Override
    public @NotNull DatabaseManager databaseManager() {
        return this.databaseManager;
    }

    @Override
    public @NotNull Redis redis() {
        return this.redis;
    }

    @Override
    public HydraAPI hydra() {
        return this.hydraAPI;
    }

    @Override
    public @NotNull PacketManager packetManager() {
        return this.packetManager;
    }

    @Override
    public @NotNull FistinEventBus<Supplier<? extends FistinEvent>> eventBus() {
        return this.eventBus;
    }

    @Override
    public @NotNull FistinNetwork network() {
        return this.network;
    }

    @Override
    public @NotNull PlayersService playersService() {
        return this.playersService;
    }

    @Override
    public @NotNull LobbyBalancer lobbyBalancer() {
        return this.lobbyBalancer;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

}
