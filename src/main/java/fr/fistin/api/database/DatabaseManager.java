package fr.fistin.api.database;

import fr.fistin.api.configuration.ConfigurationProviders;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager
{
    private final Map<String, DBConnection> connectionByName = new HashMap<>();

    public DatabaseManager()
    {
        final DatabaseConfiguration configuration = ConfigurationProviders.getConfig(DatabaseConfiguration.class);
        this.addConnection("LevelingConnection", new DBConnection(new DBCredentials(configuration.getLevelingUser(), configuration.getLevelingPass()), configuration.getLevelingHost(), configuration.getLevelingDbName(), configuration.getLevelingPort()));
    }

    public void addConnection(String name, DBConnection connection)
    {
        this.connectionByName.putIfAbsent(name, connection);
    }

    public DBConnection getConnection(String name)
    {
        return this.connectionByName.get(name);
    }

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
