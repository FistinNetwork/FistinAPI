package fr.fistin.api.database;

public interface DatabaseManager
{
    void addConnection(String name, DBConnection connection);
    DBConnection getConnection(String name);
    void clear();
}
