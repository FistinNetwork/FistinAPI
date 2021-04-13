package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class PluginProvidersTest
{
    private interface CustomProvider extends IPluginProvider
    {
        void action();

        @Override
        default @NotNull PluginType pluginType()
        {
            return PluginType.UTILITY;
        }
    }

    @Test
    public void testOnePluginProvider()
    {
        class X implements IPluginProvider
        {
            @Override
            public String toString()
            {
                return "test ok";
            }

            @Override
            public @NotNull PluginType pluginType()
            {
                return PluginType.UTILITY;
            }
        }
        final IPluginProvider provider = new X();

        PluginProviders.setProvider(IPluginProvider.class, provider);

        final IPluginProvider fromPluginProviders = PluginProviders.getProvider(IPluginProvider.class);
        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        PluginProviders.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnObjectProviderInterface()
    {
        class X implements IPluginProvider
        {
            @Override
            public @NotNull PluginType pluginType()
            {
                return PluginType.UTILITY;
            }
        }

        PluginProviders.setProvider(X.class, new X());
        PluginProviders.clear();
    }

    @Test
    public void testTwoPluginProviders()
    {
        class X implements IPluginProvider
        {
            @Override
            public String toString()
            {
                return "test ok";
            }

            @Override
            public @NotNull PluginType pluginType()
            {
                return PluginType.UTILITY;
            }
        }
        
        class Y implements CustomProvider
        {
            @Override
            public String toString()
            {
                return "test ok2";
            }

            @Override
            public void action()
            {
                System.out.println(this);
            }
        }
        
        final IPluginProvider provider = new X();
        final CustomProvider provider1 = new Y();

        PluginProviders.setProvider(IPluginProvider.class, provider);
        PluginProviders.setProvider(CustomProvider.class, provider1);

        final IPluginProvider fromPluginProviders = PluginProviders.getProvider(IPluginProvider.class);
        final CustomProvider fromPluginProviders1 = PluginProviders.getProvider(CustomProvider.class);

        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        assertSame(provider1, fromPluginProviders1);
        assertSame("test ok2", fromPluginProviders1.toString());

        PluginProviders.clear();
    }

    @Test
    public void testImmutableValuesOfMap()
    {
        class X implements IPluginProvider
        {
            @Override
            public String toString()
            {
                return "test ok";
            }

            @Override
            public @NotNull PluginType pluginType()
            {
                return PluginType.UTILITY;
            }
        }
        
        class Y implements IPluginProvider
        {
            @Override
            public String toString()
            {
                return "test ok2";
            }

            @Override
            public @NotNull PluginType pluginType()
            {
                return PluginType.UTILITY;
            }
        }
        final IPluginProvider provider = new X();
        final IPluginProvider provider1 = new Y();

        PluginProviders.setProvider(IPluginProvider.class, provider);
        PluginProviders.setProvider(IPluginProvider.class, provider1);

        final IPluginProvider fromPluginProviders = PluginProviders.getProvider(IPluginProvider.class);

        assertSame(provider, fromPluginProviders);
        assertNotSame(provider1, fromPluginProviders);

        PluginProviders.clear();
    }
}
