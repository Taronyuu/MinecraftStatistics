package nl.zandervdm.minecraftstatistics;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Listeners.SetPlayerOfflineListener;
import nl.zandervdm.minecraftstatistics.Listeners.SetPlayerOnlineListener;
import nl.zandervdm.minecraftstatistics.Tasks.CollectPlayerDataTask;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static FileConfiguration config;
    public static Integer updateFrequency;

    @Override
    public void onEnable()
    {
        createConfig();
        config = getConfig();

        updateFrequency = config.getInt("frequency");

        MySQL.establishMySQL();
        if(MySQL.connection != null){
            createDatabase();
        }

        getServer().getPluginManager().registerEvents(new SetPlayerOfflineListener(), this);
        getServer().getPluginManager().registerEvents(new SetPlayerOnlineListener(), this);

        new CollectPlayerDataTask(this).runTaskLater(this, updateFrequency*20);
    }

    public void onDisable() {

    }

    protected void createDatabase() {
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
        MySQL.update(table);
    }

    protected void createConfig(){
        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }

        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()){
            getLogger().info("Config.yml not found, creating!");
            saveDefaultConfig();
        }
    }

}
