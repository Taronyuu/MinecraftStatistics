package nl.zandervdm.minecraftstatistics;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Commands.SyncPlayersCommand;
import nl.zandervdm.minecraftstatistics.Listeners.SetPlayerOfflineListener;
import nl.zandervdm.minecraftstatistics.Listeners.SetPlayerOnlineListener;
import nl.zandervdm.minecraftstatistics.Tasks.CollectPlayerDataTask;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static FileConfiguration config;
    public static Integer updateFrequency;
    public static List<Statistic> statistics;
    public static Main plugin;

    @Override
    public void onEnable()
    {
        createConfig();
        config = getConfig();

        updateFrequency = config.getInt("frequency");
        statistics = this.getStatistics();

        (new MySQL()).openConnection();
        createDatabase();

        String query = "UPDATE " + MySQL.table + " SET is_online=0";
        (new MySQL()).update(query);

        plugin = this;

        getServer().getPluginManager().registerEvents(new SetPlayerOfflineListener(), this);
        getServer().getPluginManager().registerEvents(new SetPlayerOnlineListener(this), this);

        new CollectPlayerDataTask(this).runTaskLater(this, updateFrequency*20);

        this.getCommand("statsync").setExecutor(new SyncPlayersCommand(this));
    }

    public void onDisable() {

    }

    public List<Statistic> getStats(){
        return statistics;
    }

    protected void createDatabase() {
        String table = "create table if not exists " + MySQL.table + " (" +
                        "id int KEY NOT NULL AUTO_INCREMENT, " +
                        "uuid varchar(255)," +
                        "name varchar(255),"; // Add ')' to close the statement

        for(Statistic statistic : statistics){
            table = table + "`" + statistic + "`" + " int(30),";
        }

        table = table + " is_online int);";
        (new MySQL()).update(table);
        updateDatabase();
    }

    protected void updateDatabase() {
        String query = "alter table " + MySQL.table + " " +
                "ADD `server` varchar(100) NULL DEFAULT 'default' AFTER `name`";
        (new MySQL()).update(query, true);
        query = "alter table " + MySQL.table + " " +
                "ADD `last_join` int AFTER `server`";
        (new MySQL()).update(query, true);
        // This might a hacky solutions, but migrations are a bitch.
        // Besides that, we want to support all future versions as well without keeping this up-to-date
        for(Statistic statistic : statistics){
            query = "alter table " + MySQL.table + " " +
                    "ADD `" + statistic + "` int(30) AFTER `last_join`";
            (new MySQL()).update(query, true);
        }

        // What do we say to removing unused columns? Not today.
        // (No really, we aren't doing that because of backwards compatibility.)
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

    protected List<Statistic> getStatistics(){
        List<Statistic> ignore = new ArrayList<Statistic>();
        ignore.add(Statistic.DROP);
        ignore.add(Statistic.PICKUP);
        ignore.add(Statistic.MINE_BLOCK);
        ignore.add(Statistic.USE_ITEM);
        ignore.add(Statistic.BREAK_ITEM);
        ignore.add(Statistic.CRAFT_ITEM);
        ignore.add(Statistic.KILL_ENTITY);
        ignore.add(Statistic.ENTITY_KILLED_BY);

        List<Statistic> stats = new ArrayList<Statistic>();
        for(Statistic statistic : Statistic.values()){
            // If the statistic is inside the ignore arraylist we should ignore it because it *will* throw an exception.
            if(ignore.contains(statistic)) continue;

            // Instead of adding them all by hand, just add which one are available.
            stats.add(statistic);
        }
        return stats;
    }

}
