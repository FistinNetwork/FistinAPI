package fr.fistin.api.impl.proxy;

import fr.fistin.api.impl.common.FistinAPIProvider;
import fr.fistin.api.impl.proxy.configuration.FistinAPIConfigurationImpl;
import fr.fistin.api.proxy.FistinProxy;
import fr.fistin.api.server.FistinServer;
import fr.fistin.api.utils.FistinAPIException;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.logging.Logger;

@ApiStatus.Internal
public final class PFistinAPIProvider extends FistinAPIProvider
{

    private FistinProxy proxy;

    private final FistinAPI plugin;

    public PFistinAPIProvider(FistinAPI plugin) {
        this.plugin = plugin;

        try {
            this.enable(new FistinAPIConfigurationImpl(plugin));
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

        if (this.configuration.isHydraEnabled()) {
            this.proxy = new FistinProxyImpl(this.hydraEnv.getName());
        }
    }

    @Override
    protected void postInit()
    {

    }

    @Override
    public void disable()
    {

    }

    @Override
    public FistinProxy proxy() {
        return this.proxy;
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

}
