package fr.fistin.api.impl.proxy;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.proxy.FistinProxy;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.data.HydraData;
import fr.fistin.hydra.api.proxy.HydraProxy;
import fr.fistin.hydra.api.proxy.event.HydraProxyUpdatedEvent;

/**
 * Created by AstFaster
 * on 04/11/2022 at 14:59
 */
public class FistinProxyImpl implements FistinProxy {

    private HydraProxy handle;

    public FistinProxyImpl(String name) {
        if (!ConfigurationProviders.getConfig(FistinAPIConfiguration.class).isHydraEnabled()) {
            this.handle = new HydraProxy(new HydraData(), 25577);
            return;
        }

        final HydraAPI hydraAPI = IFistinAPIProvider.fistinAPI().hydra();

        this.handle = hydraAPI.getProxiesService().getProxy(name);

        hydraAPI.getEventBus().subscribe(HydraProxyUpdatedEvent.class, event -> {
            final HydraProxy proxy = event.getProxy();

            if (proxy.getName().equals(name)) {
                this.handle = proxy;
            }
        });
    }

    @Override
    public HydraProxy handle() {
        return this.handle;
    }

    @Override
    public void update() {
        if (!ConfigurationProviders.getConfig(FistinAPIConfiguration.class).isHydraEnabled()) {
            return;
        }

        IFistinAPIProvider.fistinAPI()
                .hydra()
                .getProxiesService()
                .updateProxy(this.handle);
    }

}
