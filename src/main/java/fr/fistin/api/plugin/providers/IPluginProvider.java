package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.jetbrains.annotations.NotNull;

public interface IPluginProvider
{
    @NotNull PluginType getPluginType();
}
