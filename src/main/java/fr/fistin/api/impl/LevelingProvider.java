package fr.fistin.api.impl;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.TriFunction;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

@ApiStatus.Internal
final class LevelingProvider implements ILevelingProvider
{
    private static final TriFunction<Integer, Float, OfflinePlayer, Consumer<PreparedStatement>> ADD_BY_PLAYER_REQUEST = (amount, boost, player) -> statement -> {
        try
        {
            statement.setInt(1, Math.round(amount * boost));
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        }
        catch(Exception e)
        {
            catchException(e);
        }
    };

    private static final BiFunction<Integer, OfflinePlayer, Consumer<PreparedStatement>> REMOVE_BY_PLAYER_REQUEST = (amount, player) -> statement -> {
        try
        {
            statement.setInt(1, amount);
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        }
        catch(Exception e)
        {
            catchException(e);
        }
    };

    private static final BiFunction<Integer, OfflinePlayer, Consumer<PreparedStatement>> SET_BY_PLAYER_REQUEST = (amount, player) -> statement -> {
        try
        {
            statement.setInt(1, amount);
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        }
        catch(Exception e)
        {
            catchException(e);
        }
    };

    @Override
    public void addExp(OfflinePlayer player, int amount, float boost)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = exp+? WHERE uuid = ?", ADD_BY_PLAYER_REQUEST.apply(amount, boost, player));
    }

    @Override
    public void removeExp(OfflinePlayer player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = exp-? WHERE uuid = ?", REMOVE_BY_PLAYER_REQUEST.apply(amount, player));
    }

    @Override
    public void setExp(OfflinePlayer player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = ? WHERE uuid = ?", SET_BY_PLAYER_REQUEST.apply(amount, player));
    }

    @Override
    public void addCoins(OfflinePlayer player, int amount, float boost)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = coins+? WHERE uuid = ?", ADD_BY_PLAYER_REQUEST.apply(amount, boost, player));
    }

    @Override
    public void removeCoins(OfflinePlayer player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = coins-? WHERE uuid = ?", REMOVE_BY_PLAYER_REQUEST.apply(amount, player));
    }

    @Override
    public void setCoins(OfflinePlayer player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = ? WHERE uuid = ?", SET_BY_PLAYER_REQUEST.apply(amount, player));
    }


    @Override
    public int getExp(OfflinePlayer player)
    {
        return this.createConnectionAndRequest("SELECT exp FROM player_levels WHERE uuid = ?", statement -> {
            try
            {
                statement.setString(1, String.valueOf(player.getUniqueId()));
                statement.executeQuery();

                final ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return resultSet.getInt("exp");
            }
            catch(Exception e)
            {
                catchException(e);
                return 0;
            }
        }, 0);
    }

    @Override
    public int getCoins(OfflinePlayer player)
    {
        return this.createConnectionAndRequest("SELECT coins FROM player_levels WHERE uuid = ?", statement -> {
            try
            {
                statement.setString(1, String.valueOf(player.getUniqueId()));
                statement.executeQuery();

                final ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                return resultSet.getInt("coins");
            }
            catch(Exception e)
            {
                catchException(e);
                return 0;
            }
        }, 0);
    }

    private <R> R createConnectionAndRequest(String cmd, Function<PreparedStatement, R> request, R nullValue)
    {
        try
        {
            final Connection connection = this.createConnection();
            if(connection != null) return request.apply(connection.prepareStatement(cmd));
            else return nullValue;
        }
        catch (SQLException e)
        {
            catchException(e);
            return nullValue;
        }
    }

    private void createConnectionAndRequest(String cmd, Consumer<PreparedStatement> request)
    {
        try
        {
            final Connection connection = this.createConnection();
            if(connection != null) request.accept(connection.prepareStatement(cmd));
        }
        catch (SQLException e)
        {
            catchException(e);
        }
    }

    @Nullable
    private Connection createConnection() throws SQLException
    {
        return this.fistinAPI()
                .databaseManager()
                .getConnection("LevelingConnection")
                .connection();
    }

    private static void catchException(Throwable e)
    {
        PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
    }
}
