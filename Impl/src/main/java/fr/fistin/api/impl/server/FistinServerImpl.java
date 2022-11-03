package fr.fistin.api.impl.server;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.server.FistinServer;
import fr.fistin.hydra.api.HydraAPI;
import fr.fistin.hydra.api.server.HydraServer;
import fr.fistin.hydra.api.server.event.HydraServerUpdatedEvent;

/**
 * Created by AstFaster
 * on 03/11/2022 at 20:08
 */
public class FistinServerImpl implements FistinServer {

    private HydraServer handle;

    public FistinServerImpl(String name) {
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
        IFistinAPIProvider.fistinAPI()
                .hydra()
                .getServersService()
                .updateServer(this.handle);
    }

}
