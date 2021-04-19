package ale444113.tnttag.arena.commands.admin.arenamodifications;

import ale444113.tnttag.TNTTag;
import ale444113.tnttag.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class DeleteArenaCommand implements CommandExecutor {
    private final TNTTag plugin;

    public DeleteArenaCommand(TNTTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("tnttag.arenas.deletearena")){
            sender.sendMessage(plugin.name+ ChatColor.RED+"You don't have permissions to delete arenas");
            return false;
        }

        if(args.length != 1){
            sender.sendMessage(plugin.name+ ChatColor.RED+"You must follow the example.");
            sender.sendMessage(plugin.name+ ChatColor.GRAY+"/deletearena [Arena name]");
            return false;
        }

        FileConfiguration config = plugin.getConfig();
        String name = args[0];

        if(!config.contains(name)){
            sender.sendMessage(plugin.name+ChatColor.RED+"The arena "+name+" doesn't exists...");
            return true;
        }

        Arena.deleteArena(name, plugin, config);
        sender.sendMessage(plugin.name+ ChatColor.GREEN+"Arena "+ChatColor.YELLOW+name+ChatColor.GREEN+" deleted successfully");

        return true;
    }
}
