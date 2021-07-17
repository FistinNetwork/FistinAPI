package fr.fistin.api.impl;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.hydra.ServerLauncher;
import fr.fistin.api.packets.HStartServerPacket;
import fr.fistin.api.plugin.providers.PluginProviders;

public class ServerLauncherImpl implements ServerLauncher
{
    @Override
    public void launchServer(String template)
    {
        PluginProviders.getProvider(IFistinAPIProvider.class).packetManager().sendPacket(new HStartServerPacket(template));
    }
}
