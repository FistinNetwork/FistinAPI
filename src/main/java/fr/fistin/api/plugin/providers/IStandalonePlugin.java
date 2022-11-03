package fr.fistin.api.plugin.providers;

import java.util.logging.Logger;

public interface IStandalonePlugin extends IPluginProvider
{

    /**
     * Get the logger used by the plugin
     *
     * @return A {@link Logger} instance
     */
    Logger getLogger();

}
