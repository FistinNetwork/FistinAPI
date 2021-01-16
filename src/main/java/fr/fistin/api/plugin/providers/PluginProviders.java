package fr.fistin.api.plugin.providers;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PluginProviders
{
    private static final Map<Class<? extends IPluginProvider>, Supplier<? extends IPluginProvider>> PLUGIN_PROVIDERS = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <P extends IPluginProvider> P getProvider(Class<P> providerClass)
    {
        return (P)PLUGIN_PROVIDERS.get(providerClass).get();
    }

    public static <P extends IPluginProvider> void setProvider(Class<? extends IPluginProvider> providerInterface, P provider)
    {
        if(providerInterface.isInterface())
            PLUGIN_PROVIDERS.computeIfAbsent(providerInterface, k -> () -> provider);
        else throw new IllegalArgumentException("providerInterface must be an interface !");
    }

    public static void clear()
    {
        PLUGIN_PROVIDERS.clear();
    }
}
