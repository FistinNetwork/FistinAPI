package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.bukkit.entity.Player;

public interface ILevelingProvider extends IPluginProvider
{
    void addExp(final Player player, final int amount, final float boost);
    void removeExp(final Player player, final int amount);
    void addCoins(final Player player, final int amount, final float boost);
    void removeCoins(final Player player, final int amount);

    int getExp(final Player player);
    int getCoins(final Player player);

    @Override
    default PluginType getPluginType()
    {
        return PluginType.UTILITY;
    }
}
