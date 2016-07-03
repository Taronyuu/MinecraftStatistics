package nl.zandervdm.minecraftstatistics.Listeners;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetPlayerOnlineListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        String query = "UPDATE " + MySQL.table + " SET is_online=1 WHERE uuid='" + player.getUniqueId() + "'";

        MySQL.updateAsync(query);
    }

}
