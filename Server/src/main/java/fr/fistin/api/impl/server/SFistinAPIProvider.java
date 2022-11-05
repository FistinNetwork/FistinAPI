package fr.fistin.api.impl.server;

import fr.fistin.api.impl.common.FistinAPIProvider;
import fr.fistin.api.impl.server.configuration.FistinAPIConfigurationImpl;
import fr.fistin.api.impl.server.listener.JoinListener;
import fr.fistin.api.impl.server.listener.SessionListener;
import fr.fistin.api.impl.server.listener.SetupListener;
import fr.fistin.api.server.FistinServer;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
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

        this.server = new FistinServerImpl(this.hydraEnv == null ? "dev-server" : this.hydraEnv.getName());
    }

    @Override
    protected void postInit()
    {
        final Consumer<Listener> listenerRegisterer = listener -> this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);

        listenerRegisterer.accept(new SetupListener());
        listenerRegisterer.accept(new JoinListener());
        listenerRegisterer.accept(new SessionListener());

        super.postInit();
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
