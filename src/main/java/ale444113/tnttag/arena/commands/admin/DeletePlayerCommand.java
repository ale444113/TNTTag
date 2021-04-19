package ale444113.tnttag.arena.commands.admin;

import ale444113.tnttag.DB.MongoDB;
import ale444113.tnttag.TNTTag;
import ale444113.tnttag.session.Session;
import ale444113.tnttag.session.SessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeletePlayerCommand implements CommandExecutor {
    private TNTTag plugin;

    public DeletePlayerCommand(TNTTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("tnttag.admin.deletedata"))
        if(args.length == 0){
            sender.sendMessage(plugin.name+ ChatColor.RED+"Please provide a valid user name");
            return false;
        }

        Player p = Bukkit.getServer().getPlayer(args[0]);
        if(p == null){
            sender.sendMessage(plugin.name+ChatColor.RED+"The player "+args[0]+"doesn't exists");
            return false;
        }

        MongoDB.deletePlayerData(p.getUniqueId());
        SessionStorage.removeSession(p.getUniqueId());

        new Session(p.getUniqueId());
        return true;
    }
}
