package fr.fistin.api.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

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
        this.connect();
    }

    private void connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.toURI(), this.dbCredentials.getUser(), this.dbCredentials.getPass());

            Logger.getLogger("Minecraft").info("Connected to the DB !");
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
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

    public Connection getConnection() throws SQLException
    {
        if(this.connection != null) if(!this.connection.isClosed()) return this.connection;

        this.connect();
        return this.connection;
    }
}
