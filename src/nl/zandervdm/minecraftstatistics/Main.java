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

        MySQL.establishMySQL();
        if(MySQL.connection != null){
            createDatabase();
        }

        String query = "UPDATE " + MySQL.table + " SET is_online=0";
        MySQL.update(query);

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
        MySQL.update(table);
        updateDatabase();
    }

    protected void updateDatabase() {
        String query = "alter table " + MySQL.table + " " +
                "ADD `server` varchar(100) NULL DEFAULT 'default' AFTER `name`";
        MySQL.update(query, true);
        query = "alter table " + MySQL.table + " " +
                "ADD `last_join` int AFTER `server`";
        MySQL.update(query, true);
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
        List<Statistic> stats = new ArrayList<Statistic>();
        stats.add(Statistic.DAMAGE_DEALT);
        stats.add(Statistic.DAMAGE_TAKEN);
        stats.add(Statistic.DEATHS);
        stats.add(Statistic.MOB_KILLS);
        stats.add(Statistic.PLAYER_KILLS);
        stats.add(Statistic.FISH_CAUGHT);
        stats.add(Statistic.ANIMALS_BRED);
        stats.add(Statistic.TREASURE_FISHED);
        stats.add(Statistic.JUNK_FISHED);
        stats.add(Statistic.LEAVE_GAME);
        stats.add(Statistic.JUMP);
        stats.add(Statistic.PLAY_ONE_TICK);
        stats.add(Statistic.WALK_ONE_CM);
        stats.add(Statistic.SWIM_ONE_CM);
        stats.add(Statistic.FALL_ONE_CM);
        stats.add(Statistic.SNEAK_TIME);
        stats.add(Statistic.CLIMB_ONE_CM);
        stats.add(Statistic.FLY_ONE_CM);
        stats.add(Statistic.MINECART_ONE_CM);
        stats.add(Statistic.BOAT_ONE_CM);
        stats.add(Statistic.PIG_ONE_CM);
        stats.add(Statistic.HORSE_ONE_CM);
        stats.add(Statistic.SPRINT_ONE_CM);
        stats.add(Statistic.CROUCH_ONE_CM);
        stats.add(Statistic.AVIATE_ONE_CM);
        stats.add(Statistic.TIME_SINCE_DEATH);
        stats.add(Statistic.TALKED_TO_VILLAGER);
        stats.add(Statistic.TRADED_WITH_VILLAGER);
        stats.add(Statistic.CAKE_SLICES_EATEN);
        stats.add(Statistic.CAULDRON_FILLED);
        stats.add(Statistic.CAULDRON_USED);
        stats.add(Statistic.ARMOR_CLEANED);
        stats.add(Statistic.BANNER_CLEANED);
        stats.add(Statistic.BREWINGSTAND_INTERACTION);
        stats.add(Statistic.BEACON_INTERACTION);
        stats.add(Statistic.DROPPER_INSPECTED);
        stats.add(Statistic.HOPPER_INSPECTED);
        stats.add(Statistic.DISPENSER_INSPECTED);
        stats.add(Statistic.NOTEBLOCK_PLAYED);
        stats.add(Statistic.NOTEBLOCK_TUNED);
        stats.add(Statistic.FLOWER_POTTED);
        stats.add(Statistic.TRAPPED_CHEST_TRIGGERED);
        stats.add(Statistic.ENDERCHEST_OPENED);
        stats.add(Statistic.ITEM_ENCHANTED);
        stats.add(Statistic.RECORD_PLAYED);
        stats.add(Statistic.FURNACE_INTERACTION);
        stats.add(Statistic.CRAFTING_TABLE_INTERACTION);
        stats.add(Statistic.CHEST_OPENED);
        stats.add(Statistic.SLEEP_IN_BED);
        return stats;
    }

}
