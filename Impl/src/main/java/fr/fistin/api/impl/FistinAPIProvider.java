package fr.fistin.api.impl;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.database.DatabaseManager;
import fr.fistin.api.impl.configuration.FistinAPIConfigurationImpl;
import fr.fistin.api.impl.database.DatabaseManagerImpl;
import fr.fistin.api.impl.listener.JoinListener;
import fr.fistin.api.impl.listener.SetupListener;
import fr.fistin.api.impl.packets.PacketManagerImpl;
import fr.fistin.api.impl.redis.RedisImpl;
import fr.fistin.api.impl.server.FistinServerImpl;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.redis.Redis;
import fr.fistin.api.server.FistinServer;
import fr.fistin.api.utils.FistinAPIException;
import fr.fistin.api.utils.PluginLocation;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.data.HydraEnv;
import fr.fistin.hydra.api.protocol.data.RedisData;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * Entry point of the FistinAPI, need to be public to be recognized by PluginClassLoader.
 */
@ApiStatus.Internal
public final class FistinAPIProvider extends JavaPlugin implements IFistinAPIProvider
{

    private FistinAPIConfiguration configuration;

    private PacketManager packetManager;
    private DatabaseManager databaseManager;

    private RedisImpl redis;
    private RedisImpl communicationRedis;

    private HydraEnv hydraEnv;
    private HydraAPI hydraAPI;

    private FistinServer server;

    public static void log(Level level, String message) {
        IFistinAPIProvider.fistinAPI().getLogger().log(level, message);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    @Override
    public void onEnable()
    {
        log("==========================");
        log("Hello, Starting Fistin API");

        this.preInit();
        this.init();
        this.postInit();
    }

    private void preInit()
    {
        this.saveDefaultConfig();
        this.reloadConfig();

        PluginProviders.setProvider(IFistinAPIProvider.class, this);
        PluginProviders.setProvider(ILevelingProvider.class, new LevelingProvider());
        ConfigurationProviders.setConfig(FistinAPIConfiguration.class, this.configuration = new FistinAPIConfigurationImpl());
    }

    private void init()
    {
        this.databaseManager = new DatabaseManagerImpl();
        this.packetManager = new PacketManagerImpl();

        final boolean hydraEnabled = this.configuration.isHydraEnabled();

        if (hydraEnabled) {
            this.hydraEnv = HydraEnv.load();
        }

        final RedisData redisConfig = hydraEnabled ? this.hydraEnv.getRedis() : this.configuration.getRedis();

        this.redis = new RedisImpl(redisConfig);
        this.communicationRedis = new RedisImpl(redisConfig);

        if (!this.redis.connect() || !this.communicationRedis.connect()) {
            Bukkit.shutdown();
            return;
        }

        if (hydraEnabled) {
            final String appName = hydraEnv.getName();

            this.hydraAPI = new HydraAPI.Builder(HydraAPI.Type.SERVER, appName)
                    .withRedis(this.communicationRedis)
                    .withLogger(this.getLogger())
                    .withLogHeader("Hydra")
                    .build();
            this.hydraAPI.start();
            this.server = new FistinServerImpl(appName);
        }
    }

    private void postInit()
    {
        this.packetManager.registerPacket(FReturnToBungeePacket.class, packet -> {
            try
            {
                final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(byteArray);

                out.writeUTF("Connect");
                out.writeUTF(packet.getServerName());

                Bukkit.getPlayer(packet.getToSend()).sendPluginMessage((Plugin)packet.getPlugin(), packet.getBungeeCordChannel(), byteArray.toByteArray());
            }
            catch (IOException e)
            {
                this.getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        });

        final Consumer<Listener> listenerRegisterer = listener -> this.getServer().getPluginManager().registerEvents(listener, this);

        listenerRegisterer.accept(new SetupListener());
        listenerRegisterer.accept(new JoinListener());
    }

    @Override
    public void onDisable()
    {
        this.databaseManager.clear();
        this.packetManager.clear();

        if (this.configuration.isHydraEnabled()) {
            this.hydraAPI.stop("Bukkit normal shutdown");
        }

        this.redis.disconnect();
        this.communicationRedis.disconnect();

        PluginProviders.clear();
        ConfigurationProviders.clear();
        PluginLocation.clear();

        log("Stopped Fistin API, bye !");
        log("=========================");
    }

    @Override
    public @NotNull PacketManager packetManager()
    {
        return this.packetManager;
    }

    @Override
    public @NotNull DatabaseManager databaseManager()
    {
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
    public FistinServer server() {
        if (this.server == null) {
            throw new FistinAPIException("FistinServer cannot be used (Fistin API is not using Hydra systems)!");
        }
        return this.server;
    }

}
