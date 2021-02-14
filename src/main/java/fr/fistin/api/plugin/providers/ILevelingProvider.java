package fr.fistin.api.plugin.providers;

import fr.fistin.api.plugin.PluginType;
import org.bukkit.entity.Player;

public interface ILevelingProvider extends IPluginProvider
{
    void addExp(final Player player, final int amount, final float boost) throws Exception;
    void removeExp(final Player player, final int amount) throws Exception;
    void addCoins(final Player player, final int amount, final float boost) throws Exception;
    void removeCoins(final Player player, final int amount) throws Exception;

    int getExp(final Player player) throws Exception;
    int getCoins(final Player player) throws Exception;

    @Override
    default PluginType getPluginType()
    {
        return PluginType.UTILITY;
    }
}
