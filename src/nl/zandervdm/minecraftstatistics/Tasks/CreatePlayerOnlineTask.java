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
        try {
            boolean result = MySQL.get("SELECT * FROM " + MySQL.table + " WHERE uuid = '" + player.getUniqueId() + "'").last();
            if (!result) {
                createUser(player);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        String query = "UPDATE " + MySQL.table + " SET is_online=1 WHERE uuid='" + player.getUniqueId() + "'";

        MySQL.updateAsync(query);
    }

    protected void createUser(Player player) {
        String query = "INSERT INTO " + MySQL.table + " (uuid, name, server) VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', '" + MySQL.servername + "');";
        MySQL.update(query);
    }

}
