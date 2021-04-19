package ale444113.tnttag.events;

import ale444113.tnttag.Scoreboards;
import ale444113.tnttag.session.Session;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onLeave(PlayerJoinEvent event){
        Player p = event.getPlayer();
        new Session(p.getUniqueId());

        Scoreboards.setLobbyScoreboard(p);
    }

}
