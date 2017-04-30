package nl.zandervdm.minecraftstatistics.Tasks;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class CreatePlayerOnlineTask extends BukkitRunnable {

    protected Player player;

    public CreatePlayerOnlineTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        (new MySQL()).validateDatabase();

        try {
            boolean result = (new MySQL()).get("SELECT * FROM " + MySQL.table + " WHERE uuid = '" + player.getUniqueId() + "' AND server = '" + MySQL.servername + "'").last();
            if (!result) {
                createUser(player);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        int lastJoin = (int)(System.currentTimeMillis()/1000L);
        String query = "UPDATE " + MySQL.table + " SET is_online=1,last_join=" + lastJoin + " WHERE uuid='" + player.getUniqueId() + "' AND server = '" + MySQL.servername + "'";

        (new MySQL()).updateAsync(query);
    }

    protected void createUser(Player player) {
        String query = "INSERT INTO " + MySQL.table + " (uuid, name, server) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + MySQL.servername + "');";
        (new MySQL()).update(query);
    }

}
