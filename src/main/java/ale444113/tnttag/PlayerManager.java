package ale444113.tnttag;

import ale444113.tnttag.DB.MongoDB;
import ale444113.tnttag.arena.Arena;
import ale444113.tnttag.session.Session;
import ale444113.tnttag.session.SessionStorage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class PlayerManager {

    public enum playerState {
        WithTnT,
        Playing,
        Spectating
    }
    public static Random rand = new Random();

    public PlayerManager(){}
    public static void killPlayer(String arenaName, Player player){
        if(!Arena.gameArenas.get(arenaName).containsKey(player) || !Arena.gameArenas.get(arenaName).get(player).equals(playerState.WithTnT)){ return; }
        GameManager.removeTNT(player, arenaName);
        Arena.gameArenas.get(arenaName).replace(player, playerState.Spectating);
        player.setGameMode(GameMode.SPECTATOR);
        try {
            player.playSound(player.getLocation(), Sound.valueOf("ENTITY_TNT_PRIMED"), 1.0f, 1.0f);
        } catch (Exception e) {
            player.playSound(player.getLocation(), Sound.valueOf("EXPLODE"), 1.0f, 1.0f);
        }
        player.sendMessage(TNTTag.getInstance().name+ChatColor.DARK_AQUA+"You died... "+ChatColor.BOLD+"Good luck next time!");
    }
    public static ArrayList<Player> getAlivePlayers(String arenaName){
        ArrayList<Player> alivePlayers = new ArrayList<>();
        for (Map.Entry<Player, playerState> entry : Arena.gameArenas.get(arenaName).entrySet()) {
            if(entry.getValue() == playerState.Playing){
                alivePlayers.add(entry.getKey());
            }
        }
        return alivePlayers;
    }
    public static void getNextPlayers(String arenaName,int n){
        ArrayList<Player> alivePlayers = getAlivePlayers(arenaName);
        while(n > 0){
            int playerNumber = rand.nextInt(alivePlayers.size());
            Player nextTNTPlayer = alivePlayers.get(playerNumber);
            Arena.gameArenas.get(arenaName).replace(nextTNTPlayer,playerState.WithTnT);

            GameManager.giveTNT(nextTNTPlayer,arenaName);
            n--;
        }
        if(MongoDB.usingMongoDB) {
            for (Player p : alivePlayers) {
                Session playerSession = SessionStorage.getSession(p.getUniqueId());
                playerSession.change("rounds", playerSession.getRounds() + 1);
                playerSession.change("points", playerSession.getPoints() + 1);
            }
        }
    }
    public static String getPlayerArena(Player player){
        for(String arenaName: Arena.gameArenas.keySet()) {
            for (Map.Entry<Player, playerState> entry : Arena.gameArenas.get(arenaName).entrySet()) {
                if(entry.getKey().equals(player)) return arenaName;
            }
        }
        return null;
    }
    public static boolean hasTNT(Player player, String arenaName){
        HashMap<Player, playerState> arenaPlayers = Arena.gameArenas.get(arenaName);
        return arenaPlayers.get(player).equals(playerState.WithTnT);
    }
    public static void killTntPlayers(String arenaName){
        for (Map.Entry<Player, playerState> entry : Arena.gameArenas.get(arenaName).entrySet()) {
            if(entry.getValue() == playerState.WithTnT){
                killPlayer(arenaName,entry.getKey());
                Arena.sendArenaMessage(arenaName,TNTTag.getInstance().name+ChatColor.YELLOW+entry.getKey().getName()+" died");}
        }
    }
}
