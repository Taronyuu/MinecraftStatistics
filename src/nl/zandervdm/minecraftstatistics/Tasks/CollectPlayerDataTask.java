package nl.zandervdm.minecraftstatistics.Tasks;

import com.sun.org.apache.xpath.internal.operations.Bool;
import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Main;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class CollectPlayerDataTask extends BukkitRunnable {

    protected Main plugin;
    protected Boolean singleRunOnly;

    public CollectPlayerDataTask(Main plugin, Boolean singleRunOnly) {
        this.plugin = plugin;
        this.singleRunOnly = singleRunOnly;
    }

    public CollectPlayerDataTask(Main plugin){
        this.plugin = plugin;
        this.singleRunOnly = false;
    }

    @Override
    public void run() {
        (new MySQL()).validateDatabase();

        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        Statistic[] statistics = Statistic.values();

        for(Player player : players){
            String query = "UPDATE " + MySQL.table + " SET name='" + player.getName() + "', ";

            for(Statistic statistic : this.plugin.getStats()){
                try {
                    query = query + statistic + "='" + player.getStatistic(statistic) + "', ";
                }catch (Exception e){
                    // Most likely not allowed here
                }
            }
            query = query + "is_online=1, WHERE uuid='" + player.getUniqueId() + "' AND server='" + MySQL.servername + "' ";
            (new MySQL()).updateAsync(query);
        }

        if(!singleRunOnly) {
            new CollectPlayerDataTask(this.plugin).runTaskLater(this.plugin, Main.updateFrequency * 20);
        }
    }

}
