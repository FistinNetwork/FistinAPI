package fr.fistin.api.impl.server;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * Entry point of the FistinAPI, need to be public to be recognized by PluginClassLoader.
 */
@ApiStatus.Internal
public final class FistinAPI extends JavaPlugin {

    private SFistinAPIProvider provider;

    @Override
    public void onEnable() {
        this.provider = new SFistinAPIProvider(this);
    }

    @Override
    public void onDisable() {
        this.provider.disable();
    }

    public SFistinAPIProvider getProvider() {
        return this.provider;
    }

}
