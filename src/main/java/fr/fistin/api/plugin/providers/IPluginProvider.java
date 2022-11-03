package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.jetbrains.annotations.NotNull;

/**
 * Basic PluginProvider
 */
public interface IPluginProvider
{

    /**
     * Get the type of the plugin
     *
     * @return A {@link PluginType}
     */
    @NotNull PluginType pluginType();

}
