package nl.zandervdm.minecraftstatistics.Tasks;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class CreatePlayerOnlineTask extends BukkitRunnable {

    protected Player player;

    protected MySQL mysql;

    public CreatePlayerOnlineTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        MySQL mysql = new MySQL();
        mysql.openConnection();

        this.mysql = mysql;

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

        mysql.updateAsync(query);
        mysql.closeConnection();
    }

    protected void createUser(Player player) {
        String query = "INSERT INTO " + MySQL.table + " (uuid, name, server) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + MySQL.servername + "');";
        this.mysql.update(query);
    }

}
