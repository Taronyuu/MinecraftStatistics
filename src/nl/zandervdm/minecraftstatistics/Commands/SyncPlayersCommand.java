package nl.zandervdm.minecraftstatistics.Commands;

import nl.zandervdm.minecraftstatistics.Main;
import nl.zandervdm.minecraftstatistics.Tasks.CollectPlayerDataTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class SyncPlayersCommand implements CommandExecutor {

    protected Main plugin;

    public SyncPlayersCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("minecraftstatistics.sync")){
            commandSender.sendMessage(ChatColor.RED + "You don't have access to this command");
            return true;
        }

        commandSender.sendMessage(ChatColor.LIGHT_PURPLE + "Player stats are now being synced!");

        new CollectPlayerDataTask(this.plugin, true).runTaskLater(this.plugin, 1);

        return true;
    }

}
