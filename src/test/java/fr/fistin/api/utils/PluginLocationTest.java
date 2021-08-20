package fr.fistin.api.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PluginLocationTest
{
    @Test
    public void testConstructorsHaveSameResult()
    {
        final PluginLocation first = new PluginLocation("nea", "some_say");
        final PluginLocation second = new PluginLocation("nea:some_say");
        assertEquals(first, second);
    }

    @Test
    public void testLowerUpperCaseAndSpaces()
    {
        final PluginLocation first = new PluginLocation("Pink Floyd", "Comfortably Numb");
        final PluginLocation second = new PluginLocation("pinK_fLoyd:comfortaBly Numb");
        assertEquals(first, second);
    }

    @Test
    public void testGetFromMap()
    {
        final Map<PluginLocation, String> testMap = new HashMap<>();
        final PluginLocation loc = new PluginLocation("test", "junit", true);
        testMap.put(loc, "aValue");

        assertEquals(testMap.get(PluginLocation.getLocation("test", "junit")), testMap.get(loc));
    }
}
