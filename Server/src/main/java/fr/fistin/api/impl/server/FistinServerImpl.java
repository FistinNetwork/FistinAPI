package fr.fistin.api.impl.server;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.server.FistinServer;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.protocol.data.HydraData;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.HydraServerOptions;
import fr.fistin.hydra.api.server.event.HydraServerUpdatedEvent;

/**
 * Created by AstFaster
 * on 03/11/2022 at 20:08
 */
public class FistinServerImpl implements FistinServer {

    private HydraServer handle;

    public FistinServerImpl(String name) {
        if (!ConfigurationProviders.getConfig(FistinAPIConfiguration.class).isHydraEnabled()) {
            this.handle = new HydraServer("dev", "normal", "", HydraServer.Accessibility.PUBLIC, HydraServer.Process.PERMANENT, new HydraServerOptions(), new HydraData(), 1000);
            return;
        }

        final HydraAPI hydraAPI = IFistinAPIProvider.fistinAPI().hydra();

        this.handle = hydraAPI.getServersService().getServer(name);

        hydraAPI.getEventBus().subscribe(HydraServerUpdatedEvent.class, event -> {
            final HydraServer server = event.getServer();

            if (server.getName().equals(name)) {
                this.handle = server;
            }
        });
    }

    @Override
    public HydraServer handle() {
        return this.handle;
    }

    @Override
    public void update() {
        if (!ConfigurationProviders.getConfig(FistinAPIConfiguration.class).isHydraEnabled()) {
            return;
        }

        IFistinAPIProvider.fistinAPI()
                .hydra()
                .getServersService()
                .updateServer(this.handle);
    }

}
