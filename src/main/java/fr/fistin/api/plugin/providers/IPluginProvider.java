package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.bukkit.plugin.Plugin;

public interface IPluginProvider
{
    PluginType getPluginType();
}
