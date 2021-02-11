package fr.fistin.api.database;

import fr.fistin.api.configuration.ConfigurationProviders;

import java.sql.SQLException;

public class DatabaseManager
{
    private final DBConnection levelingConnection;

    public DatabaseManager()
    {
        final DatabaseConfiguration configuration = ConfigurationProviders.getConfig(DatabaseConfiguration.class);
        this.levelingConnection = new DBConnection(new DBCredentials(configuration.getLevelingUser(), configuration.getLevelingPass()), configuration.getLevelingHost(), configuration.getLevelingDbName(), configuration.getLevelingPort());
    }

    public DBConnection getLevelingConnection()
    {
        return this.levelingConnection;
    }

    public void close()
    {
        try
        {
            this.levelingConnection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
