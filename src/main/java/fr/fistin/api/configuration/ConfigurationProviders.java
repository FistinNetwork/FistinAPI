package fr.fistin.api.configuration;

import fr.flowarg.sch.ConfigurationManager;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ConfigurationProviders
{
    private static final Map<Class<? extends ConfigurationManager>, Supplier<? extends ConfigurationManager>> CONFIG_PROVIDERS = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <P extends ConfigurationManager> P getConfig(Class<P> configClass)
    {
        return (P)CONFIG_PROVIDERS.get(configClass).get();
    }

    public static <P extends ConfigurationManager> void setConfig(Class<P> impl, P provider)
    {
        if(!impl.isInterface())
            CONFIG_PROVIDERS.computeIfAbsent(impl, k -> () -> provider);
        else throw new IllegalArgumentException("impl must be a class !");
    }

    public static void clear()
    {
        CONFIG_PROVIDERS.clear();
    }
}
