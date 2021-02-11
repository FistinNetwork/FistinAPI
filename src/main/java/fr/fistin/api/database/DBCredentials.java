package fr.fistin.api.database;

public class DBCredentials
{
    private final String user;
    private final String pass;

    public DBCredentials(String user, String pass)
    {
        this.user = user;
        this.pass = pass;
    }

    public String getUser()
    {
        return this.user;
    }

    public String getPass()
    {
        return this.pass;
    }
}
