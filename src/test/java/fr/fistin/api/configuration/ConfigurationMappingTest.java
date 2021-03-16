package fr.fistin.api.configuration;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ConfigurationMappingTest
{
    @Test
    public void testMap()
    {
        final ConfigurationMapping<String, String> testMappings = new ConfigurationMapping<String, String>() {
            private final Map<String, Function<String, String>> mappings = new HashMap<>();

            @Override
            public @NotNull Map<String, Function<String, String>> mappings()
            {
                return this.mappings;
            }
        };
        testMappings.mappings().put("%TEST_KEY%", String::toUpperCase);
        testMappings.mappings().put("%OTHER_KEY%", String::toLowerCase);
        final String mapped = testMappings.map("It is an %TEST_KEY%, and another %OTHER_KEY%.", "AmaZIng KEy nOW mAppED");
        Assert.assertEquals("It is an AMAZING KEY NOW MAPPED, and another amazing key now mapped.", mapped);
    }
}