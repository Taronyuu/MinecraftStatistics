package nl.zandervdm.minecraftstatistics.Tasks;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Main;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class CollectPlayerDataTask extends BukkitRunnable {

    protected Main plugin;

    public CollectPlayerDataTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
        Statistic[] statistics = Statistic.values();

        for(Player player : players){
            try {
                boolean result = MySQL.get("SELECT * FROM stats WHERE uuid = '" + player.getUniqueId() + "'").last();
                if(!result){
                    createUser(player);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String query = "UPDATE stats SET name='" + player.getName() + "', ";

            for(Statistic statistic : statistics){
                try{
                    query = query + statistic + "='" + player.getStatistic(statistic) + "', ";
                }catch (Exception e){
                    //Ignore
                }
            }
            query = query + "is_online=1 WHERE uuid='" + player.getUniqueId() + "'";
            MySQL.update(query);
        }

        new CollectPlayerDataTask(this.plugin).runTaskLater(this.plugin, 100);
    }

    protected void createUser(Player player){
        String query = "INSERT INTO stats (uuid, name) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "');";
        MySQL.update(query);
    }

}
