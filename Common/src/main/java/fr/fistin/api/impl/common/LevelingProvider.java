package fr.fistin.api.impl.common;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

@ApiStatus.Internal
class LevelingProvider implements ILevelingProvider
{
    @Override
    public void addExp(@NotNull UUID player, int amount, float boost)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = exp+? WHERE uuid = ?", addByPlayerRequest(amount, boost, player));
    }

    @Override
    public void removeExp(@NotNull UUID player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = exp-? WHERE uuid = ?", setByPlayerRequest(amount, player));
    }

    @Override
    public void setExp(@NotNull UUID player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET exp = ? WHERE uuid = ?", setByPlayerRequest(amount, player));
    }

    @Override
    public void addCoins(@NotNull UUID player, int amount, float boost)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = coins+? WHERE uuid = ?", addByPlayerRequest(amount, boost, player));
    }

    @Override
    public void removeCoins(@NotNull UUID player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = coins-? WHERE uuid = ?", setByPlayerRequest(amount, player));
    }

    @Override
    public void setCoins(@NotNull UUID player, int amount)
    {
        this.createConnectionAndRequest("UPDATE player_levels SET coins = ? WHERE uuid = ?", setByPlayerRequest(amount, player));
    }

    @Override
    public int getExp(@NotNull UUID player)
    {
        return this.createConnectionAndRequest("SELECT exp FROM player_levels WHERE uuid = ?", statement -> {
            try
            {
                statement.setString(1, String.valueOf(player));
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
    public int getCoins(@NotNull UUID player)
    {
        return this.createConnectionAndRequest("SELECT coins FROM player_levels WHERE uuid = ?", statement -> {
            try
            {
                statement.setString(1, String.valueOf(player));
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

    private <R> R createConnectionAndRequest(String cmd, Function<PreparedStatement, R> request, @SuppressWarnings("SameParameterValue") R nullValue)
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
        return IFistinAPIProvider.fistinAPI()
                .databaseManager()
                .getConnection("LevelingConnection")
                .connection();
    }

    private static Consumer<PreparedStatement> addByPlayerRequest(int amount, float boost, UUID player)
    {
        return statement -> {
            try
            {
                statement.setInt(1, Math.round(amount * boost));
                statement.setString(2, String.valueOf(player));
                statement.executeUpdate();
            }
            catch(Exception e)
            {
                catchException(e);
            }
        };
    }

    private static Consumer<PreparedStatement> setByPlayerRequest(int amount, UUID player)
    {
        return statement -> {
            try
            {
                statement.setInt(1, amount);
                statement.setString(2, String.valueOf(player));
                statement.executeUpdate();
            }
            catch(Exception e)
            {
                catchException(e);
            }
        };
    }

    private static void catchException(Throwable e)
    {
        PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
    }
}
