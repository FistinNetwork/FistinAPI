package fr.fistin.api.database;

import java.sql.SQLException;

public class DatabaseManager
{
    private final DBConnection levelingConnection;

    public DatabaseManager(DatabaseConfiguration configuration)
    {
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
