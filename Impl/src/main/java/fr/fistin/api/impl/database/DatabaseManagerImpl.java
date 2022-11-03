package fr.fistin.api.impl.database;

import fr.fistin.api.configuration.ConfigurationProviders;
import fr.fistin.api.configuration.FistinAPIConfiguration;
import fr.fistin.api.database.DBConnection;
import fr.fistin.api.database.DBCredentials;
import fr.fistin.api.database.DatabaseManager;
import org.jetbrains.annotations.ApiStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ApiStatus.Internal
public class DatabaseManagerImpl implements DatabaseManager
{
    private final Map<String, DBConnection> connectionByName = new HashMap<>();

    public DatabaseManagerImpl()
    {
        final FistinAPIConfiguration configuration = ConfigurationProviders.getConfig(FistinAPIConfiguration.class);
        this.addConnection("LevelingConnection", new DBConnection(new DBCredentials(configuration.getLevelingUser(), configuration.getLevelingPass()), configuration.getLevelingHost(), configuration.getLevelingDbName(), configuration.getLevelingPort()));
    }

    @Override
    public void addConnection(String name, DBConnection connection)
    {
        this.connectionByName.putIfAbsent(name, connection);
    }

    @Override
    public DBConnection getConnection(String name)
    {
        return this.connectionByName.get(name);
    }

    @Override
    public void clear()
    {
        this.connectionByName.forEach((s, dbConnection) -> {
            try
            {
                dbConnection.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        });
        this.connectionByName.clear();
    }
}
