package fr.fistin.api.impl.common;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

@ApiStatus.Internal
class LevelingProvider implements ILevelingProvider
{
    @Override
    public void initialize(UUID player) {
        if (!this.isPlayerExisting(player)) {
            this.createConnectionAndRequest("INSERT INTO player_levels (uuid, exp, coins) VALUES (?, 0, 0)", statement -> {
                try {
                    statement.setString(1, player.toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    catchException(e);
                }
            });
        }
    }

    private boolean isPlayerExisting(UUID player) {
        return this.createConnectionAndRequest("SELECT * FROM player_levels", statement -> {
            try {
                final ResultSet resultSet =  statement.executeQuery();

                if (resultSet.getMetaData().getColumnName(1).equalsIgnoreCase("uuid")) {
                    while (resultSet.next()) {
                        if (resultSet.getString("uuid").equalsIgnoreCase(player.toString())) {
                            return true;
                        }
                    }
                }
                return false;
            } catch (SQLException e) {
                catchException(e);
            }
            return false;
        }, false);
    }

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

            if(connection != null) {
                return request.apply(connection.prepareStatement(cmd));
            }
        }
        catch (SQLException e)
        {
            catchException(e);
        }
        return nullValue;
    }

    private void createConnectionAndRequest(String cmd, Consumer<PreparedStatement> request)
    {
        try
        {
            final Connection connection = this.createConnection();

            if(connection != null) {
                request.accept(connection.prepareStatement(cmd));
            }
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
