package fr.fistin.api.plugin.impl;

import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.utils.Internal;
import org.bukkit.entity.Player;

@Internal
public class LevelingProvider implements ILevelingProvider
{
    @Override
    public void addExp(Player player, double amount)
    {
        // TODO Access to database and put xp.
    }

    @Override
    public void removeExp(Player player, double amount)
    {
        // TODO Access to database and remove xp.
    }

    @Override
    public void addCoins(Player player, double amount)
    {
        // TODO Access to database and put coins.
    }

    @Override
    public void removeCoins(Player player, double amount)
    {
        // TODO Access to database and remove coins.
    }

    @Override
    public double getExp(Player player)
    {
        // TODO Access to database and get xp.
        return 0D;
    }

    @Override
    public double getCoins(Player player)
    {
        // TODO Access to database and get coins.
        return 0D;
    }
}
