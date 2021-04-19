package ale444113.tnttag.arena.commands.user;

import ale444113.tnttag.PlayerManager;
import ale444113.tnttag.TNTTag;
import ale444113.tnttag.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class LeaveArenaCommand implements CommandExecutor {
    private final TNTTag plugin;
    public LeaveArenaCommand(TNTTag plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender == null) return false;

        Player player = (Player) sender;
        String playerArena = PlayerManager.getPlayerArena(player);
        if(playerArena == null){
            player.sendMessage(plugin.name+ ChatColor.RED+"You aren't in an arena");
            return false;
        }

        Arena.leaveArena(playerArena, player);
        player.sendMessage(plugin.name+ChatColor.GRAY+"You left "+playerArena);
        return true;
    }
}
