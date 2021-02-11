package fr.fistin.api.configuration;

import fr.flowarg.sch.ConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ConfigurationProvidersTest
{
    @Test
    public void testOneConfigurationProvider()
    {
        final TestConfig provider = new TestConfig();

        ConfigurationProviders.setConfig(TestConfig.class, provider);

        final TestConfig fromPluginProviders = ConfigurationProviders.getConfig(TestConfig.class);
        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        ConfigurationProviders.clear();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnInterfaceProvider()
    {
        ConfigurationProviders.setConfig(TestBadConfigInterface.class, new TestBadConfig());
        ConfigurationProviders.clear();
    }

    @Test
    public void testTwoConfigurationProviders()
    {
        final TestConfig provider = new TestConfig();
        final AnotherTestConfig provider1 = new AnotherTestConfig();

        ConfigurationProviders.setConfig(TestConfig.class, provider);
        ConfigurationProviders.setConfig(AnotherTestConfig.class, provider1);

        final TestConfig fromPluginProviders = ConfigurationProviders.getConfig(TestConfig.class);
        final AnotherTestConfig fromPluginProviders1 = ConfigurationProviders.getConfig(AnotherTestConfig.class);

        assertSame(provider, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        assertSame(provider1, fromPluginProviders1);
        assertSame("test ok2", fromPluginProviders1.toString());

        ConfigurationProviders.clear();
    }

    @Test
    public void testImmutableValuesOfMap()
    {
        final TestConfig provider = new TestConfig();
        final TestConfig provider1 = new TestConfig();

        ConfigurationProviders.setConfig(TestConfig.class, provider);
        ConfigurationProviders.setConfig(TestConfig.class, provider1);

        final TestConfig fromPluginProviders = ConfigurationProviders.getConfig(TestConfig.class);

        assertSame(provider, fromPluginProviders);
        assertNotSame(provider1, fromPluginProviders);

        ConfigurationProviders.clear();
    }

    private static class TestConfig implements ConfigurationManager
    {
        @Override
        public void saveConfig() {}

        @Override
        public void loadConfig() {}

        @Override
        public FileConfiguration getConfig()
        {
            return null;
        }

        @Override
        public String toString()
        {
            return "test ok";
        }
    }

    private static class AnotherTestConfig implements ConfigurationManager
    {
        @Override
        public void saveConfig() {}

        @Override
        public void loadConfig() {}

        @Override
        public FileConfiguration getConfig()
        {
            return null;
        }

        @Override
        public String toString()
        {
            return "test ok2";
        }
    }

    interface TestBadConfigInterface extends ConfigurationManager {}

    private static class TestBadConfig implements TestBadConfigInterface
    {
        @Override
        public void saveConfig() {}

        @Override
        public void loadConfig() {}

        @Override
        public FileConfiguration getConfig()
        {
            return null;
        }
    }
}
