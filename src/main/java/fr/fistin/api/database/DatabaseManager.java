package fr.fistin.api.database;

import java.sql.SQLException;

public class DatabaseManager {
    private DbConnection dbConnection;

    public DatabaseManager() {
        this.dbConnection = new DbConnection(new DbCredentials("localhost", "root", "", "minecraft", 3306));
    }

    public DbConnection getDbConnection() {
        return dbConnection;
    }

    public void close(){
        try {
            this.dbConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
