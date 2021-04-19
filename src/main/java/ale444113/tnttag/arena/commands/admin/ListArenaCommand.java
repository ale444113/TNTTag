package ale444113.tnttag.arena.commands.admin;

import ale444113.tnttag.TNTTag;
import ale444113.tnttag.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class ListArenaCommand implements CommandExecutor {
    private final TNTTag plugin;
    public ListArenaCommand(TNTTag plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("tnttag.arenas.listarena")){
            sender.sendMessage(plugin.name+ ChatColor.RED+"You don't have access to this command");
            return false;
        }

        sender.sendMessage(plugin.name+ChatColor.GOLD+"Here is a list of all the arenas: ");
        for(String arenaName : Arena.gameArenas.keySet()){
            int arenaPlayers = Arena.getArenaPlayers(arenaName);

            ConfigurationSection arenaSection =  plugin.getConfig().getConfigurationSection(arenaName);
            int arenaMaxPlayers = arenaSection.getInt("max");
            int arenaMinPlayers = arenaSection.getInt("min");

            if(arenaPlayers == 0){sender.sendMessage(ChatColor.GRAY+"- "+arenaName+ChatColor.GRAY+" ("+arenaPlayers+"/"+arenaMaxPlayers+")");}
            else if(arenaPlayers == arenaMaxPlayers){sender.sendMessage(ChatColor.GRAY+"- "+arenaName+ChatColor.RED+" ("+arenaPlayers+"/"+arenaMaxPlayers+")");}
            else if(arenaPlayers >= arenaMinPlayers){sender.sendMessage(ChatColor.GRAY+"- "+arenaName+ChatColor.YELLOW+" ("+arenaPlayers+"/"+arenaMaxPlayers+")");}
            else{sender.sendMessage(ChatColor.GRAY+"- "+arenaName+ChatColor.GREEN+" ("+arenaPlayers+"/"+arenaMaxPlayers+")");}
        }

        return true;
    }
}
