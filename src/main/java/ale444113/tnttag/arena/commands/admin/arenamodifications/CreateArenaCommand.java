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

public class CreateArenaCommand implements CommandExecutor {
    private final TNTTag plugin;

    public CreateArenaCommand(TNTTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!sender.hasPermission("tnttag.arenas.createarena")){
            sender.sendMessage(plugin.name+ ChatColor.RED+"You don't have permissions to create arenas");
            return true;
        }

        if(args.length != 3){
            sender.sendMessage(plugin.name+ ChatColor.RED+"You must follow the example.");
            sender.sendMessage(plugin.name+ ChatColor.GRAY+"/createarena [Arena name] [Required Players] [Max players]");
            return false;
        }

        FileConfiguration config = plugin.getConfig();
        String name = args[0];
        int requieredPlayers = 0;
        int maxPlayers = 0;

        if(config.contains(name)){
            sender.sendMessage(plugin.name+ChatColor.RED+"The arena "+name+" alredy exist");
            return true;
        }

        String error = null;
        try{
            requieredPlayers = Integer.parseInt(args[1]);
            maxPlayers = Integer.parseInt(args[2]);
        }catch (NumberFormatException e){
            error = e.getLocalizedMessage().replace("For input string: ","");
        }
        if(error != null){
            sender.sendMessage(plugin.name+ChatColor.RED+error+" is not a valid number");
            return true;
        }

        Arena.createArena(sender.getName(),requieredPlayers,maxPlayers,name,plugin,config);
        sender.sendMessage(plugin.name+ ChatColor.GREEN+"Arena "+ChatColor.YELLOW+name+ChatColor.GREEN+" created successfully");

        return true;
    }
}