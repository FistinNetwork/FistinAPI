package fr.fistin.api.impl.server;

import fr.fistin.api.impl.server.configuration.FistinAPIConfigurationImpl;
import fr.fistin.api.impl.common.FistinAPIProvider;
import fr.fistin.api.impl.server.listener.JoinListener;
import fr.fistin.api.impl.server.listener.SetupListener;
import fr.fistin.api.packets.FReturnToBungeePacket;
import fr.fistin.api.server.FistinServer;
import fr.fistin.api.utils.FistinAPIException;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApiStatus.Internal
public final class SFistinAPIProvider extends FistinAPIProvider
{

    private FistinServer server;

    private final FistinAPI plugin;

    public SFistinAPIProvider(FistinAPI plugin) {
        this.plugin = plugin;

        this.enable(new FistinAPIConfigurationImpl());
    }

    @Override
    protected void preInit()
    {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();

        super.preInit();
    }

    @Override
    protected void init()
    {
        super.init();

        if (this.configuration.isHydraEnabled()) {
            this.server = new FistinServerImpl(this.hydraEnv.getName());
        }
    }

    @Override
    protected void postInit()
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

        final Consumer<Listener> listenerRegisterer = listener -> this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);

        listenerRegisterer.accept(new SetupListener());
        listenerRegisterer.accept(new JoinListener());
    }

    @Override
    public void disable()
    {

    }

    @Override
    public FistinServer server() {
        return this.server;
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

}
