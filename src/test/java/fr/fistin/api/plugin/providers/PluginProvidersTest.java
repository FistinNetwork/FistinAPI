package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PlayerGrade;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class PluginProvidersTest
{
    @Test
    public void testOnePluginProvider()
    {
        final ITNTTagProvider provider = defaultTagProvider("test ok");

        PluginProviders.setProvider(ITNTTagProvider.class, provider);

        final ITNTTagProvider fromPluginProviders = PluginProviders.getProvider(ITNTTagProvider.class);
        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        PluginProviders.clear();
    }

    @Test
    public void testTwoPluginProviders()
    {
        final ITNTTagProvider provider = defaultTagProvider("test ok");
        final IRTFProvider provider1 = defaultRTFProvider();

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
        final ITNTTagProvider provider = defaultTagProvider("test ok");
        final ITNTTagProvider provider1 = defaultTagProvider("test ok2");

        PluginProviders.setProvider(ITNTTagProvider.class, provider);
        PluginProviders.setProvider(ITNTTagProvider.class, provider1);

        final ITNTTagProvider fromPluginProviders = PluginProviders.getProvider(ITNTTagProvider.class);

        assertSame(provider, fromPluginProviders);
        assertNotSame(provider1, fromPluginProviders);

        PluginProviders.clear();
    }

    private static ITNTTagProvider defaultTagProvider(String toString)
    {
        return new ITNTTagProvider() {
            @Override
            public int xpForWin()
            {
                return 0;
            }

            @Override
            public int xpForLoose()
            {
                return 0;
            }

            @Override
            public int coinsForWin()
            {
                return 0;
            }

            @Override
            public int coinsForLoose()
            {
                return 0;
            }

            @Override
            public boolean canWinPointsOnWin()
            {
                return false;
            }

            @Override
            public boolean canWinPointsOnLoose()
            {
                return false;
            }

            @Override
            public boolean canWinXpOnWin()
            {
                return false;
            }

            @Override
            public boolean canWinXpOnLoose()
            {
                return false;
            }

            @Override
            public int gradeMultiplier(PlayerGrade grade)
            {
                return 1;
            }

            @Override
            public String toString()
            {
                return toString;
            }
        };
    }

    private static IRTFProvider defaultRTFProvider()
    {
        return new IRTFProvider() {
            @Override
            public int xpForWin()
            {
                return 0;
            }

            @Override
            public int xpForLoose()
            {
                return 0;
            }

            @Override
            public int coinsForWin()
            {
                return 0;
            }

            @Override
            public int coinsForLoose()
            {
                return 0;
            }

            @Override
            public boolean canWinPointsOnWin()
            {
                return false;
            }

            @Override
            public boolean canWinPointsOnLoose()
            {
                return false;
            }

            @Override
            public boolean canWinXpOnWin()
            {
                return false;
            }

            @Override
            public boolean canWinXpOnLoose()
            {
                return false;
            }

            @Override
            public int gradeMultiplier(PlayerGrade grade)
            {
                return 1;
            }

            @Override
            public String toString()
            {
                return "test ok2";
            }
        };
    }
}
