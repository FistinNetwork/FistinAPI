package fr.fistin.api.database;

import fr.fistin.api.IFistinAPIProvider;
import fr.fistin.api.plugin.providers.PluginProviders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class DBConnection
{
    private final DBCredentials dbCredentials;
    private Connection connection;
    private final String host;
    private final String dbName;
    private final int port;

    public DBConnection(DBCredentials dbCredentials, String host, String dbName, int port)
    {
        this.dbCredentials = dbCredentials;
        this.host = host;
        this.dbName = dbName;
        this.port = port;
    }

    private void connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.toURI(), this.dbCredentials.getUser(), this.dbCredentials.getPass());
        }
        catch (SQLException | ClassNotFoundException e)
        {
            PluginProviders.getProvider(IFistinAPIProvider.class).getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public String toURI()
    {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbName;
    }

    public void close() throws SQLException
    {
        if(this.connection != null) if(!this.connection.isClosed()) this.connection.close();
    }

    public Connection connection() throws SQLException
    {
        if(this.connection != null) if(!this.connection.isClosed()) return this.connection;

        this.connect();
        return this.connection;
    }
}
