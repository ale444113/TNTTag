package ale444113.tnttag.arena.commands.admin.arenalocations;

import ale444113.tnttag.TNTTag;
import ale444113.tnttag.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetFirstLocation implements CommandExecutor {
    private TNTTag plugin;
    public SetFirstLocation(TNTTag plugin){ this.plugin = plugin; }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender == null) return false;
        Player player = (Player) sender;

        if(!player.hasPermission("tnttag.arenas.setlocations")){
            player.sendMessage(plugin.name+ ChatColor.RED+"You don't have permission to do that...");
            return true;
        }

        if(args.length == 0){
            player.sendMessage(plugin.name+ ChatColor.RED+"You must specify a arena name");
            return false;
        }
        if(!Arena.gameArenas.containsKey(args[0])){
            player.sendMessage(plugin.name+ChatColor.RED+"There is no arena with name "+args[0]);
            return true;
        }
        Location playerLocation = player.getLocation();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection startLocation = config.getConfigurationSection(args[0]).getConfigurationSection("startLocation");
        startLocation.set("world", playerLocation.getWorld().getName());
        startLocation.set("x", playerLocation.getX());
        startLocation.set("y",playerLocation.getY());
        startLocation.set("z", playerLocation.getZ());
        plugin.saveConfig();

        Arena.arenaStartLocations.put(args[0], playerLocation);

        player.sendMessage(plugin.name+ChatColor.AQUA+args[0]+ChatColor.GREEN+" start location updated");
        return true;
    }
}
