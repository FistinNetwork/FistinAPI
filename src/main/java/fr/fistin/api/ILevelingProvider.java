package fr.fistin.api;

import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IPluginProvider;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface ILevelingProvider extends IPluginProvider
{
    void addExp(@NotNull UUID player, final int amount, final float boost);
    void removeExp(@NotNull UUID player, final int amount);
    void addCoins(@NotNull UUID player, final int amount, final float boost);
    void removeCoins(@NotNull UUID player, final int amount);
    void setExp(@NotNull UUID player, final int amount);
    void setCoins(@NotNull UUID player, final int amount);

    int getExp(@NotNull UUID player);
    int getCoins(@NotNull UUID player);

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }
}
