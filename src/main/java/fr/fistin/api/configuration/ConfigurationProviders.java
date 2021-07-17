package fr.fistin.api.configuration;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConfigurationProviders
{
    private static final Map<Class<? extends FistinConfiguration>, Supplier<? extends FistinConfiguration>> CONFIG_PROVIDERS = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <P extends FistinConfiguration> P getConfig(Class<P> configClass)
    {
        return (P)CONFIG_PROVIDERS.get(configClass).get();
    }

    public static <P extends FistinConfiguration> void setConfig(Class<P> impl, P provider)
    {
        CONFIG_PROVIDERS.computeIfAbsent(impl, k -> () -> provider);
    }

    public static void clear()
    {
        CONFIG_PROVIDERS.clear();
    }
}
