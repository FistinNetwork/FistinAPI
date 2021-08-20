package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PluginProvidersTest
{
    private interface CustomProvider extends IPluginProvider
    {
        @Override
        default @NotNull PluginType pluginType()
        {
            return PluginType.UTILITY;
        }
    }

    @AfterEach
    public void clean()
    {
        PluginProviders.clear();
    }

    @Test
    public void testOnePluginProvider()
    {
        final IPluginProvider provider = mock(IPluginProvider.class);
        when(provider.toString()).thenReturn("test ok");

        PluginProviders.setProvider(IPluginProvider.class, provider);

        final IPluginProvider fromPluginProviders = PluginProviders.getProvider(IPluginProvider.class);
        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());
    }

    @Test
    public void testAnObjectProviderInterface()
    {
        final IPluginProvider mockedProvider = mock(IPluginProvider.class);
        assertThrows(IllegalArgumentException.class, () -> PluginProviders.setProvider(mockedProvider.getClass(), mockedProvider));
    }

    @Test
    public void testTwoPluginProviders()
    {
        final IPluginProvider provider = mock(IPluginProvider.class);
        final CustomProvider provider1 = mock(CustomProvider.class);
        when(provider.toString()).thenReturn("test ok");
        when(provider1.toString()).thenReturn("test ok2");

        PluginProviders.setProvider(IPluginProvider.class, provider);
        PluginProviders.setProvider(CustomProvider.class, provider1);

        final IPluginProvider fromPluginProviders = PluginProviders.getProvider(IPluginProvider.class);
        final CustomProvider fromPluginProviders1 = PluginProviders.getProvider(CustomProvider.class);

        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        assertSame(provider1, fromPluginProviders1);
        assertSame("test ok2", fromPluginProviders1.toString());
    }

    @Test
    public void testImmutableValuesOfMap()
    {
        final IPluginProvider provider = mock(IPluginProvider.class);
        final IPluginProvider provider1 = mock(IPluginProvider.class);

        PluginProviders.setProvider(IPluginProvider.class, provider);
        PluginProviders.setProvider(IPluginProvider.class, provider1);

        final IPluginProvider fromPluginProviders = PluginProviders.getProvider(IPluginProvider.class);

        assertSame(provider, fromPluginProviders);
        assertNotSame(provider1, fromPluginProviders);
    }
}
