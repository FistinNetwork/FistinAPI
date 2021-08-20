package fr.fistin.api.impl;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.hydra.ServerLauncher;
import fr.fistin.api.packets.HStartServerPacket;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.FistinAPIException;

public class ServerLauncherImpl implements ServerLauncher
{
    @Override
    public void launchServer(String template)
    {
        if (!ConfigurationProviders.getConfig(FistinAPIConfiguration.class).getHydraEnable()) throw new FistinAPIException("Calling `launchServer` but hydra.enable=false");
        PluginProviders.getProvider(IFistinAPIProvider.class).packetManager().sendPacket(new HStartServerPacket(template));
    }
}
