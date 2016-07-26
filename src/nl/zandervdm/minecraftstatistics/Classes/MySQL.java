package nl.zandervdm.minecraftstatistics.Classes;

import nl.zandervdm.minecraftstatistics.Main;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {

    public static Connection connection;

    protected static String host;
    protected static Integer port;
    protected static String database;
    protected static String username;
    protected static String password;
    public static String servername;
    public static String table;

    public static void establishMySQL()
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
            openConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void update(String query)
    {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(String query, boolean silence)
    {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            if(silence){
                //Ignore
            }else{
                e.printStackTrace();
            }
        }
    }

    public static void updateAsync(final String query){
        Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable() {
            @Override
            public void run() {
                update(query);
            }
        });
    }

    public static ResultSet get(String query)
    {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Connection openConnection() throws ClassNotFoundException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
        return connection;
    }

}
