package fr.fistin.api.impl.proxy;

import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * Entry point of the FistinAPI, need to be public to be recognized by PluginClassLoader.
 */
@ApiStatus.Internal
public final class FistinAPI extends Plugin {

    private PFistinAPIProvider provider;

    @Override
    public void onEnable() {
        this.provider = new PFistinAPIProvider(this);
    }

    @Override
    public void onDisable() {
        this.provider.disable();
    }

    public PFistinAPIProvider getProvider() {
        return this.provider;
    }


}
