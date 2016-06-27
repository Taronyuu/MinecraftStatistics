package nl.zandervdm.minecraftstatistics;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Tasks.CollectPlayerDataTask;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable()
    {
        this.config = getConfig();
        this.config.options().copyDefaults(true);

        MySQL.establishMySQL();
        if(MySQL.connection != null){
            createDatabase();
        }

        new CollectPlayerDataTask(this).runTaskLater(this, 100);
    }

    public void onDisable()
    {

    }

    protected void createDatabase()
    {
        String table = "create table if not exists stats (" +
                        "id int KEY NOT NULL AUTO_INCREMENT, " +
                        "uuid varchar(255)," +
                        "name varchar(255),"; // Add ')' to close the statement

        for(Statistic statistic : Statistic.values()){
            try{
                table = table + "`" + statistic + "`" + " varchar(255),";
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        table = table + " is_online int);";

        System.out.println(table);
        MySQL.update(table);
    }

}
