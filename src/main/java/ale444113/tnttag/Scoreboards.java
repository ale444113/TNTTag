package ale444113.tnttag;

import ale444113.tnttag.arena.Arena;
import ale444113.tnttag.session.Session;
import ale444113.tnttag.session.SessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class Scoreboards {
    public static void gameScoreboard(Player p, String arenaName, int alivePlayers, int totalPlayers, int round, int seconds){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Game","dummy");
        objective.setDisplayName(ChatColor.AQUA+arenaName);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.GRAY+"〰〰〰〰〰〰〰〰〰〰 ").setScore(7);
        objective.getScore(ChatColor.GOLD+"Alive players: "+ChatColor.YELLOW+alivePlayers+"/"+totalPlayers).setScore(6);
        objective.getScore(ChatColor.GOLD+"Round: "+ChatColor.LIGHT_PURPLE+round).setScore(5);
        switch(Arena.gameArenas.get(arenaName).get(p)){
            case Playing:
                objective.getScore(ChatColor.GOLD+"Your state: "+ChatColor.GREEN+"Playing").setScore(4);
                break;
            case WithTnT:
                objective.getScore(ChatColor.GOLD+"Your state: "+ChatColor.RED+"TNT").setScore(4);
                break;
            case Spectating:
                objective.getScore(ChatColor.GOLD+"Your state: "+ChatColor.GRAY+"Spectating").setScore(4);
                break;
        }
        objective.getScore(ChatColor.GOLD+"Explosion in: "+ChatColor.GREEN+seconds).setScore(5);

        String serverIp = TNTTag.getInstance().getConfig().getString("server-ip");
        objective.getScore(ChatColor.translateAlternateColorCodes('&',"&6Ip: "+serverIp)).setScore(2);
        objective.getScore(ChatColor.GRAY+"〰〰〰〰〰〰〰〰〰〰").setScore(1);
        p.setScoreboard(scoreboard);
        Arena.showOnlyArenaPlayers(p);
    }
    public static void setScoreboardToArenaPlayers(String arenaName, int round, int seconds){
        int alivePlayers = Arena.getArenaAlivePlayers(arenaName).getKey();
        int totalPlayers = Arena.getArenaPlayers(arenaName);
        for(Player p : Arena.gameArenas.get(arenaName).keySet()){
            gameScoreboard(p, arenaName, alivePlayers, totalPlayers, round, seconds);
        }
    }

    public static void setLobbyScoreboard(Player p){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Lobby","dummy");
        objective.setDisplayName(ChatColor.AQUA+"TNTTag");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore(ChatColor.GRAY+"〰〰〰〰〰〰〰〰〰〰 ").setScore(6);

        Session playerSession = SessionStorage.getSession(p.getUniqueId());
        int wins = playerSession.getWins();
        int rounds = playerSession.getRounds();
        int points = playerSession.getPoints();

        objective.getScore(ChatColor.GOLD+"Wins: "+ ChatColor.DARK_BLUE+wins).setScore(5);
        objective.getScore(ChatColor.GOLD+"Round: "+ChatColor.LIGHT_PURPLE+ rounds).setScore(4);
        objective.getScore(ChatColor.GOLD+"Points : "+ChatColor.GREEN+points).setScore(3);

        String serverIp = TNTTag.getInstance().getConfig().getString("server-ip");
        objective.getScore(ChatColor.translateAlternateColorCodes('&',"&6Ip: "+serverIp)).setScore(2);
        objective.getScore(ChatColor.GRAY+"〰〰〰〰〰〰〰〰〰〰").setScore(1);
        p.setScoreboard(scoreboard);
    }

}
