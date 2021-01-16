package fr.fistin.api.plugin.providers;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class PluginProvidersTest
{
    @Test
    public void testOnePluginProvider()
    {
        final ITNTTagProvider provider = new ITNTTagProvider() {
            @Override
            public String toString()
            {
                return "test ok";
            }
        };

        PluginProviders.setProvider(ITNTTagProvider.class, provider);

        final ITNTTagProvider fromPluginProviders = PluginProviders.getProvider(ITNTTagProvider.class);
        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        PluginProviders.clear();
    }

    @Test
    public void testTwoPluginProviders()
    {
        final ITNTTagProvider provider = new ITNTTagProvider() {
            @Override
            public String toString()
            {
                return "test ok";
            }
        };

        final IRTFProvider provider1 = new IRTFProvider() {
            @Override
            public String toString()
            {
                return "test ok2";
            }
        };

        PluginProviders.setProvider(ITNTTagProvider.class, provider);
        PluginProviders.setProvider(IRTFProvider.class, provider1);

        final ITNTTagProvider fromPluginProviders = PluginProviders.getProvider(ITNTTagProvider.class);
        final IRTFProvider fromPluginProviders1 = PluginProviders.getProvider(IRTFProvider.class);

        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        assertSame(provider1, fromPluginProviders1);
        assertSame("test ok2", fromPluginProviders1.toString());

        PluginProviders.clear();
    }

    @Test
    public void testImmutableValuesOfMap()
    {
        final ITNTTagProvider provider = new ITNTTagProvider() {
            @Override
            public String toString()
            {
                return "test ok";
            }
        };

        final ITNTTagProvider provider1 = new ITNTTagProvider() {
            @Override
            public String toString()
            {
                return "test not ok";
            }
        };

        PluginProviders.setProvider(ITNTTagProvider.class, provider);
        PluginProviders.setProvider(ITNTTagProvider.class, provider1);

        final ITNTTagProvider fromPluginProviders = PluginProviders.getProvider(ITNTTagProvider.class);

        assertSame(provider, fromPluginProviders);
        assertNotSame(provider1, fromPluginProviders);

        PluginProviders.clear();
    }
}
