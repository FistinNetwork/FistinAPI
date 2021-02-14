package fr.fistin.api.plugin.impl;

import fr.fistin.api.database.DBConnection;
import fr.fistin.api.plugin.providers.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.ILevelingProvider;
import fr.fistin.api.plugin.providers.PluginProviders;
import fr.fistin.api.utils.Internal;
import org.bukkit.entity.Player;

import java.sql.*;

@Internal
final class LevelingProvider implements ILevelingProvider
{
    @Override
    public void addExp(Player player, int amount) throws Exception
    {
        PreparedStatement statement = this.createConnection("UPDATE player_levels SET exp = exp+? WHERE uuid = ?");

        statement.setDouble(1, amount);
        statement.setString(2, String.valueOf(player.getUniqueId()));
        statement.executeUpdate();
    }

    @Override
    public void removeExp(Player player, int amount) throws Exception
    {
        PreparedStatement statement = this.createConnection("UPDATE player_levels SET exp = exp-? WHERE uuid = ?");

        statement.setDouble(1, amount);
        statement.setString(2, String.valueOf(player.getUniqueId()));
        statement.executeUpdate();
    }

    @Override
    public void addCoins(Player player, int amount) throws Exception
    {
        // TODO Access to database and put coins.
    }

    @Override
    public void removeCoins(Player player, int amount) throws Exception
    {
        // TODO Access to database and remove coins.
    }

    @Override
    public int getExp(Player player) throws Exception
    {
        PreparedStatement statement = this.createConnection("SELECT exp FROM player_levels WHERE uuid = ?");
        statement.setString(1, String.valueOf(player.getUniqueId()));
        statement.executeQuery();
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        int expe = resultSet.getInt("exp");
        return expe;
    }

    @Override
    public int getCoins(Player player) throws Exception
    {
        // TODO Access to database and get coins.
        return 0;
    }

    private PreparedStatement createConnection(String cmd) throws Exception {
        DBConnection dbConnection = PluginProviders.getProvider(IFistinAPIProvider.class).getDatabaseManager().getLevelingConnection();
        Connection connection = dbConnection.getConnection();
        return connection.prepareStatement(cmd);
    }
}
