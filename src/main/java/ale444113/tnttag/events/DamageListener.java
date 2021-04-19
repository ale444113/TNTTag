package ale444113.tnttag.events;

import ale444113.tnttag.GameManager;
import ale444113.tnttag.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class DamageListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player damagedPlayer = (Player) event.getEntity();
        String damagedPlayerArena = PlayerManager.getPlayerArena(damagedPlayer);
        if(damagedPlayerArena == null) return;
        event.setDamage(0);

        if(!(event.getDamager() instanceof Player)) return;
        Player damagerPlayer = (Player) event.getDamager();
        String damagerPlayerArena = PlayerManager.getPlayerArena(damagerPlayer);

        if(damagerPlayerArena == null ||!damagerPlayerArena.equals(damagedPlayerArena)) return;
        if(!PlayerManager.hasTNT(damagerPlayer,damagedPlayerArena)) return;

        GameManager.changeTNTOwner(damagerPlayer ,damagedPlayer, damagedPlayerArena);
    }
}
