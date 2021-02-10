package fr.fistin.api.database;

public class DbCredentials {
    private String host;
    private String user;
    private String pass;
    private String dbName;
    private int port;

    public DbCredentials(String host, String user, String pass, String dbName, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.dbName = dbName;
        this.port = port;
    }

    public String toURI(){
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("jdbc:mysql://")
                .append(host)
                .append(":")
                .append(port)
                .append("/")
                .append(dbName);

        return stringBuilder.toString();
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
