package nl.zandervdm.minecraftstatistics.Listeners;

import nl.zandervdm.minecraftstatistics.Classes.MySQL;
import nl.zandervdm.minecraftstatistics.Main;
import nl.zandervdm.minecraftstatistics.Tasks.CreatePlayerOnlineTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetPlayerOnlineListener implements Listener {

    protected Main plugin;

    public SetPlayerOnlineListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        new CreatePlayerOnlineTask(player).runTaskAsynchronously(this.plugin);
    }

}
