package ale444113.tnttag.arena.commands.user;

import ale444113.tnttag.PlayerManager;
import ale444113.tnttag.TNTTag;
import ale444113.tnttag.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo

public class JoinArenaCommand implements CommandExecutor {
    private final TNTTag plugin;
    public JoinArenaCommand(TNTTag plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(!(sender instanceof Player)) return false;

        if(strings.length == 0){
            sender.sendMessage(plugin.name+ChatColor.RED+"Provide a valid arena name");
            return false;
        }

        Player player = (Player) sender;
        if(!Arena.gameArenas.containsKey(strings[0])){
            sender.sendMessage(plugin.name+ChatColor.RED+"There is no arena with name "+strings[0]);
            return false;
        }

        FileConfiguration config = plugin.getConfig();
        if(Arena.getArenaPlayers(strings[0]) >= config.getConfigurationSection(strings[0]).getInt("max")){
            player.sendMessage(plugin.name+ChatColor.YELLOW+"The arena "+strings[0]+" is full");
            return false;
        }

        if(!Arena.toJoinArenas.contains(strings[0])){
            player.sendMessage(plugin.name+ChatColor.YELLOW+"The arena "+strings[0]+" already started");
            return false;
        }

        String playerArena = PlayerManager.getPlayerArena(player);
        if(playerArena != null){
            player.sendMessage(plugin.name+ChatColor.RED+"You are in an arena! ("+ChatColor.GRAY+playerArena+ChatColor.RED+")");
            return false;
        }

        player.sendMessage(plugin.name+ ChatColor.GREEN+"You just joined "+strings[0]);
        Arena.joinArena(strings[0],player);

        Location startLocation = Arena.arenaStartLocations.get(strings[0]);
        if(Arena.isValidArena(startLocation)) player.teleport(startLocation);
        return true;
    }
}
