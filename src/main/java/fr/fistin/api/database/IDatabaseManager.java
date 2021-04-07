package fr.fistin.api.database;

public interface IDatabaseManager
{
    void addConnection(String name, DBConnection connection);
    DBConnection getConnection(String name);
    void clear();
}
