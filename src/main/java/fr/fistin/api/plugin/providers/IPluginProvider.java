package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Basic PluginProvider
 */
public interface IPluginProvider
{
    @NotNull PluginType pluginType();
    @NotNull default IFistinAPIProvider fistinAPI()
    {
        return Cache.cache();
    }

    @ApiStatus.Internal
    class Cache
    {
        private static IFistinAPIProvider cache;

        private static IFistinAPIProvider cache()
        {
            return cache != null ? cache : (cache = PluginProviders.getProvider(IFistinAPIProvider.class));
        }
    }
}
