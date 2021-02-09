package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.IPluginType;
import fr.fistin.api.plugin.impl.PluginTypes;
import org.bukkit.entity.Player;

public interface ILevelingProvider extends IPluginProvider
{
    void addExp(final Player player, final double amount);
    void removeExp(final Player player, final double amount);
    void addCoins(final Player player, final double amount);
    void removeCoins(final Player player, final double amount);

    double getExp(final Player player);
    double getCoins(final Player player);

    @Override
    default IPluginType getPluginType()
    {
        return PluginTypes.UTILITY;
    }
}
