package fr.fistin.api;

import fr.fistin.api.plugin.PluginType;
import fr.fistin.api.plugin.providers.IPluginProvider;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public interface ILevelingProvider extends IPluginProvider
{
    void addExp(final OfflinePlayer player, final int amount, final float boost);
    void removeExp(final OfflinePlayer player, final int amount);
    void addCoins(final OfflinePlayer player, final int amount, final float boost);
    void removeCoins(final OfflinePlayer player, final int amount);
    void setExp(final OfflinePlayer player, final int amount);
    void setCoins(final OfflinePlayer player, final int amount);

    int getExp(final OfflinePlayer player);
    int getCoins(final OfflinePlayer player);

    @Override
    default @NotNull PluginType pluginType()
    {
        return PluginType.UTILITY;
    }
}
