package fr.fistin.api.impl;

import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.TriFunction;
import org.bukkit.entity.Player;
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
    private static final Consumer<Throwable> EXCEPTION_CATCHER = e -> PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);

    private static final TriFunction<Integer, Float, Player, Consumer<PreparedStatement>> ADD_BY_PLAYER_REQUEST = (amount, boost, player) -> statement -> {
        try
        {
            statement.setInt(1, Math.round(amount * boost));
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        }
        catch(Exception e)
        {
            EXCEPTION_CATCHER.accept(e);
        }
    };

    private static final BiFunction<Integer, Player, Consumer<PreparedStatement>> REMOVE_BY_PLAYER_REQUEST = (amount, player) -> statement -> {
        try
        {
            statement.setInt(1, amount);
            statement.setString(2, String.valueOf(player.getUniqueId()));
            statement.executeUpdate();
        }
        catch(Exception e)
        {
            EXCEPTION_CATCHER.accept(e);
        }
    };

    @Override
    public void addExp(Player player, int amount, float boost)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = exp+? WHERE uuid = ?", ADD_BY_PLAYER_REQUEST.apply(amount, boost, player));
    }

    @Override
    public void removeExp(Player player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = exp-? WHERE uuid = ?", REMOVE_BY_PLAYER_REQUEST.apply(amount, player));
    }

    @Override
    public void addCoins(Player player, int amount, float boost)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = coins+? WHERE uuid = ?", ADD_BY_PLAYER_REQUEST.apply(amount, boost, player));
    }

    @Override
    public void removeCoins(Player player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = coins-? WHERE uuid = ?", REMOVE_BY_PLAYER_REQUEST.apply(amount, player));
    }

    @Override
    public int getExp(Player player)
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
                EXCEPTION_CATCHER.accept(e);
                return 0;
            }
        }, 0);
    }

    @Override
    public int getCoins(Player player)
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
                EXCEPTION_CATCHER.accept(e);
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
            EXCEPTION_CATCHER.accept(e);
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
            EXCEPTION_CATCHER.accept(e);
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
}
