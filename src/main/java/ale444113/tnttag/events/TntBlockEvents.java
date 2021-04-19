package ale444113.tnttag.events;

import ale444113.tnttag.GameManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class TntBlockEvents implements Listener {
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) {
        ItemStack block = e.getItemInHand();
        if(block == null || block.getType() == Material.AIR || !block.hasItemMeta()){ return; }
        ItemStack tnt = GameManager.getTNT();
        if(block.equals(tnt)) { e.setCancelled(true); }
    }

    @EventHandler
    public void onThrow(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if(item.equals(GameManager.getTNT())){ e.setCancelled(true); }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        ItemStack item = event.getCurrentItem();

        if(slot == 39 && item.equals(GameManager.getTNT())){
            event.setCancelled(true);
        }
    }
}
