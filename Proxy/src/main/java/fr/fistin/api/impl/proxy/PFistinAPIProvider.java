package fr.fistin.api.impl.proxy;

import fr.fistin.api.impl.common.FistinAPIProvider;
import fr.fistin.api.impl.proxy.configuration.FistinAPIConfigurationImpl;
import fr.fistin.api.impl.proxy.listener.JoinListener;
import fr.fistin.api.impl.proxy.listener.ServersListener;
import fr.fistin.api.impl.proxy.listener.SessionListener;
import fr.fistin.api.impl.proxy.listener.SetupListener;
import fr.fistin.api.impl.proxy.receiver.PlayersReceiver;
import fr.fistin.api.proxy.FistinProxy;
import fr.fistin.hydra.api.event.HydraEvent;
import fr.fistin.hydra.api.server.HydraServer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Listener;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.function.Consumer;
import java.util.logging.Logger;

@ApiStatus.Internal
public final class PFistinAPIProvider extends FistinAPIProvider
{

    private FistinProxy proxy;

    private final FistinAPI plugin;

    public PFistinAPIProvider(FistinAPI plugin) {
        this.plugin = plugin;

        try {
            this.enable(new FistinAPIConfigurationImpl(plugin), this.plugin.getLogger());
        } catch (IOException e) {
            e.printStackTrace();

            ProxyServer.getInstance().stop();
        }
    }

    @Override
    protected void preInit()
    {
        super.preInit();
    }

    @Override
    protected void init()
    {
        super.init();

        this.proxy = new FistinProxyImpl(this.hydraEnv == null ? "dev-proxy" : this.hydraEnv.getName());
    }

    @Override
    protected void postInit()
    {
        if (this.configuration.isHydraEnabled()) {
            // Register existing servers
            for (HydraServer server : this.hydraAPI.getServersService().getServers()) {
                final String serverName = server.getName();
                final ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, new InetSocketAddress(serverName, 25565), "", false);

                ProxyServer.getInstance().getServers().put(serverName, serverInfo);
            }

            this.hydraAPI.getEventBus().subscribe(HydraEvent.class, new ServersListener());
        }

        this.packetManager.registerReceiver("players", new PlayersReceiver());

        final Consumer<Listener> registerer = listener -> ProxyServer.getInstance().getPluginManager().registerListener(this.plugin, listener);

        registerer.accept(new JoinListener());
        registerer.accept(new SetupListener());
        registerer.accept(new SessionListener());

        super.postInit();
    }

    @Override
    public void disable()
    {

    }

    @Override
    public FistinProxy proxy() {
        return this.proxy;
    }

}
