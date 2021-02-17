package fr.fistin.api.plugin.impl;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.Internal;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Internal
final class LevelingProvider implements ILevelingProvider
{
    @Override
    public void addExp(Player player, int amount, float boost)
    {
        try {
            PreparedStatement statement = this.createConnection("UPDATE player_levels SET exp = exp+? WHERE uuid = ?");

            statement.setInt(1, Math.round(amount * boost));
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        } catch(Exception e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void removeExp(Player player, int amount)
    {
        try {
            PreparedStatement statement = this.createConnection("UPDATE player_levels SET exp = exp-? WHERE uuid = ?");

            statement.setInt(1, amount);
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        } catch(Exception e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void addCoins(Player player, int amount, float boost)
    {
        try {
            PreparedStatement statement = this.createConnection("UPDATE player_levels SET coins = coins+? WHERE uuid = ?");

            statement.setInt(1, Math.round(amount * boost));
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        } catch(Exception e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public void removeCoins(Player player, int amount)
    {
        try {
            PreparedStatement statement = this.createConnection("UPDATE player_levels SET coins = coins-? WHERE uuid = ?");

            statement.setInt(1, amount);
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        } catch(Exception e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public int getExp(Player player)
    {
        try {
            PreparedStatement statement = this.createConnection("SELECT exp FROM player_levels WHERE uuid = ?");
            statement.setString(1, String.valueOf(player.getUniqueId()));
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt("exp");
        } catch(Exception e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public int getCoins(Player player)
    {
        try {
            PreparedStatement statement = this.createConnection("SELECT coins FROM player_levels WHERE uuid = ?");
            statement.setString(1, String.valueOf(player.getUniqueId()));
            statement.executeQuery();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt("coins");
        } catch(Exception e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
            return 0;
        }
    }

    private PreparedStatement createConnection(String cmd) throws Exception {
        return PluginProviders
                .getProvider(IFistinAPIProvider.class)
                .getDatabaseManager()
                .getConnection("LevelingConnection")
                .getConnection()
                .prepareStatement(cmd);
    }
}
