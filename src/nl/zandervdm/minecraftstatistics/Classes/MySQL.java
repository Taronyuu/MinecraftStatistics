package nl.zandervdm.minecraftstatistics.Classes;

import nl.zandervdm.minecraftstatistics.Main;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    public Connection connection;

    protected static String host;
    protected static Integer port;
    protected static String database;
    protected static String username;
    protected static String password;
    public static String servername;
    public static String table;

    public void openConnection()
    {

        host        = Main.config.getString("mysql.host");
        port        = Main.config.getInt("mysql.port");
        database    = Main.config.getString("mysql.database");
        username    = Main.config.getString("mysql.username");
        password    = Main.config.getString("mysql.password");
        servername  = Main.config.getString("servername");
        table       = Main.config.getString("mysql.table");
        if(table == null){
            table = "stats";
        }

        try{
            this.connection = connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void validateDatabase(){
        try {
            if(connection == null || connection.isClosed() || connection.isReadOnly() || !connection.isValid(5)){
                this.openConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String query)
    {
        try {
            openConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void update(String query, boolean silence)
    {
        try {
            openConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            if(silence){
                //Ignore
            }else{
                e.printStackTrace();
            }
        }
        closeConnection();
    }

    public void updateAsync(final String query){
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
            @Override
            public void run() {
                update(query);
            }
        });
    }

    public ResultSet get(String query)
    {
        try {
            openConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
        return null;
    }

    private Connection connect() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
        return connection;
    }

    private void closeConnection()
    {
        try {
            this.connection.close();
        } catch (SQLException e) {
            // Most likely no connection
        }
    }

}
