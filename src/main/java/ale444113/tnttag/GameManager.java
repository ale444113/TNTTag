package ale444113.tnttag;

import ale444113.tnttag.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class GameManager {

    public static ItemStack getTNT(){
        ItemStack tnt = new ItemStack(Material.TNT);
        ItemMeta tntMeta = tnt.getItemMeta();
        tntMeta.setDisplayName(ChatColor.RED+"You have the bomb!");
        tntMeta.addEnchant(Enchantment.DURABILITY, 100, true);
        tntMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        tnt.setItemMeta(tntMeta);
        return tnt;
    }

    public static void giveTNT(Player player, String arenaName){
        player.getInventory().clear();

        player.getInventory().addItem(getTNT());
        player.getInventory ().setHelmet(getTNT());

        HashMap<Player, PlayerManager.playerState> arenaPlayers = Arena.gameArenas.get(arenaName);
        arenaPlayers.replace(player, PlayerManager.playerState.WithTnT);

        try {
            player.playSound(player.getLocation(),Sound.valueOf("ENTITY_CAT_HURT"), 1.0f, 1.0f);
        } catch (Exception e) {
            player.playSound(player.getLocation(),Sound.valueOf("CAT_HIT"), 1.0f, 1.0f);
        }

        player.sendMessage(TNTTag.getInstance().name+ChatColor.RED+"You have the tnt!");
        Arena.sendArenaMessage(arenaName,TNTTag.getInstance().name+"&f"+player.getName()+" have the tnt");
    }
    public static void removeTNT(Player player, String arenaName){
        HashMap<Player, PlayerManager.playerState> arenaPlayers = Arena.gameArenas.get(arenaName);
        if(!arenaPlayers.get(player).equals(PlayerManager.playerState.WithTnT)) return;
        player.getInventory().remove(GameManager.getTNT());
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        arenaPlayers.replace(player, PlayerManager.playerState.Playing);
    }
    public static void changeTNTOwner(Player from, Player to, String arenaName){
        removeTNT(from, arenaName);
        giveTNT(to, arenaName);
    }
}
