package fr.fistin.api.impl;

import com.google.gson.GsonBuilder;
import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.database.IDatabaseManager;
import fr.fistin.api.hydra.ServerLauncher;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.packets.HStartServerPacket;
import fr.fistin.api.packets.PacketManager;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.FistinAPIException;
import fr.fistin.api.utils.PluginLocation;
import fr.fistin.hydraconnector.HydraConnector;
import fr.fistin.hydraconnector.protocol.channel.HydraChannel;
import fr.fistin.hydraconnector.protocol.packet.server.StartServerPacket;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Entry point of the FistinAPI, need to be public to be recognized by PluginClassLoader.
 */
@ApiStatus.Internal
public final class FistinAPIProvider extends JavaPlugin implements IFistinAPIProvider
{
    private PacketManager packetManager;
    private IDatabaseManager databaseManager;
    private HydraConnector hydraConnector;
    private ServerLauncher serverLauncher;

    @Override
    public void onEnable()
    {
        this.getLogger().info("==========================");
        this.getLogger().info("Hello, Starting Fistin API");

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
        ConfigurationProviders.setConfig(FistinAPIConfiguration.class, new FistinAPIConfigurationImpl());
    }

    private void init()
    {
        this.databaseManager = new DatabaseManager();
        this.packetManager = new PacketManagerImpl();
        this.serverLauncher = new ServerLauncherImpl();
        final FistinAPIConfiguration config = ConfigurationProviders.getConfig(FistinAPIConfiguration.class);
        this.hydraConnector = new HydraConnector(
                new GsonBuilder()
                        .disableHtmlEscaping()
                        .serializeNulls()
                        .create(),
                config.getHydraHost(),
                config.getHydraPort(),
                config.getHydraPass()
        );
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

        this.packetManager.registerPacket(HStartServerPacket.class, packet -> this.hydraConnector.getConnectionManager().sendPacket(HydraChannel.SERVERS, new StartServerPacket(packet.getTemplate())));

        this.getServer().getPluginManager().registerEvents(new SetupListener(), this);

        if(ConfigurationProviders.getConfig(FistinAPIConfiguration.class).getHydraEnable())
        {
            if(this.hydraConnector.connectToRedis())
                this.hydraConnector.startPacketHandler();
        }
    }
    
    @Override
    public void onDisable()
    {
        this.databaseManager.clear();
        this.packetManager.clear();

        if(ConfigurationProviders.getConfig(FistinAPIConfiguration.class).getHydraEnable())
            this.hydraConnector.stop();

        PluginProviders.clear();
        ConfigurationProviders.clear();
        PluginLocation.clear();

        this.getLogger().info("Stopped Fistin API, bye !");
        this.getLogger().info("=========================");
    }

    @Override
    public @NotNull PacketManager packetManager()
    {
        return this.packetManager;
    }

    @Override
    public @NotNull IDatabaseManager databaseManager()
    {
        return this.databaseManager;
    }

    @Override
    public @NotNull ServerLauncher serverLauncher()
    {
        return this.serverLauncher;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unsafeGet(String parameter, TypeGet typeGet)
    {
        switch (typeGet)
        {
            case FIELD:
            {
                try
                {
                    final Field field = JavaPlugin.class.getDeclaredField(parameter);
                    field.setAccessible(true);
                    return (T)field.get(this);
                } catch (IllegalAccessException | NoSuchFieldException e)
                {
                    this.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    return null;
                }
            }
            case METHOD:
            {
                try {
                    final Method method = JavaPlugin.class.getDeclaredMethod(parameter);
                    method.setAccessible(true);
                    return (T)method.invoke(this);
                }
                catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
                {
                    this.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    return null;
                }
            }
        }
        throw new FistinAPIException("Unknown TypeGet: " + typeGet);
    }
}
