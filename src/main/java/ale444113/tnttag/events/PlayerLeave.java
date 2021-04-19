package ale444113.tnttag.events;

import ale444113.tnttag.PlayerManager;
import ale444113.tnttag.arena.Arena;
import ale444113.tnttag.session.SessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class PlayerLeave implements Listener {
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player p = event.getPlayer();

        String playerArena = PlayerManager.getPlayerArena(p);
        if(playerArena == null) return;

        Arena.leaveArena(playerArena, p);
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); // It removes the scoreboard to the player

        SessionStorage.removeSession(p.getUniqueId());
    }
}
