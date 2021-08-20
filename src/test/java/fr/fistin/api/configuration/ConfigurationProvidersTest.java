package fr.fistin.api.configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ConfigurationProvidersTest
{
    private interface AnotherFistinConfig extends FistinConfiguration {}

    @Mock
    private FistinConfiguration testConfig;

    @Mock
    private AnotherFistinConfig anotherTestConfig;

    @BeforeEach
    public void setup()
    {
        lenient().when(this.testConfig.toString()).thenReturn("test ok");
        lenient().when(this.anotherTestConfig.toString()).thenReturn("test ok2");
    }

    @AfterEach
    public void clean()
    {
        ConfigurationProviders.clear();
    }

    @Test
    public void testOneConfigurationProvider()
    {
        ConfigurationProviders.setConfig(FistinConfiguration.class, this.testConfig);

        final FistinConfiguration fromPluginProviders = ConfigurationProviders.getConfig(FistinConfiguration.class);
        assertSame(testConfig, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());
    }

    @Test
    public void testTwoConfigurationProviders()
    {
        ConfigurationProviders.setConfig(FistinConfiguration.class, this.testConfig);
        ConfigurationProviders.setConfig(AnotherFistinConfig.class, this.anotherTestConfig);

        final FistinConfiguration fromPluginProviders = ConfigurationProviders.getConfig(FistinConfiguration.class);
        final AnotherFistinConfig fromPluginProviders1 = ConfigurationProviders.getConfig(AnotherFistinConfig.class);

        assertSame(this.testConfig, fromPluginProviders);
        assertSame("test ok", fromPluginProviders.toString());

        assertSame(this.anotherTestConfig, fromPluginProviders1);
        assertSame("test ok2", fromPluginProviders1.toString());
    }

    @Test
    public void testImmutableValuesOfMap()
    {
        ConfigurationProviders.setConfig(FistinConfiguration.class, this.testConfig);
        ConfigurationProviders.setConfig(FistinConfiguration.class, this.anotherTestConfig);

        final FistinConfiguration fromPluginProviders = ConfigurationProviders.getConfig(FistinConfiguration.class);

        assertSame(this.testConfig, fromPluginProviders);
        assertNotSame(this.anotherTestConfig, fromPluginProviders);
    }
}
