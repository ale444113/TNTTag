package ale444113.tnttag.arena;

import ale444113.tnttag.DB.MongoDB;
import ale444113.tnttag.PlayerManager;
import ale444113.tnttag.Scoreboards;
import ale444113.tnttag.TNTTag;
import ale444113.tnttag.session.Session;
import ale444113.tnttag.session.SessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;


//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


import java.util.AbstractMap;

public class ArenaTimer {
    private final String arenaName;
    private final int requiredPlayers;
    private final TNTTag plugin;
    private int timeToStart = 60;
    private int taskId;

    private int seconds;
    private int round = 1;


    public ArenaTimer(String arenaName, int requiredPlayers){
        this.arenaName = arenaName;
        this.requiredPlayers = requiredPlayers;
        this.plugin = TNTTag.getInstance();
        startTimer();
    }
    private void startTimer(){
        this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(Arena.getArenaPlayers(arenaName) >= requiredPlayers){
                    timeToStart-=1;
                    Arena.setPlayersXP(arenaName, timeToStart);
                    switch (timeToStart){
                        case 59:
                            Arena.sendArenaMessage(arenaName, plugin.name+ChatColor.BLUE+"Arena starting soon... 60 seconds left");
                            break;
                        case 30:
                            Arena.sendArenaMessage(arenaName, plugin.name+ChatColor.BLUE+"Arena starting soon... 30 seconds left");
                            break;
                        case 10:
                            Arena.sendArenaMessage(arenaName, plugin.name+ChatColor.BLUE+"Arena starting in 10 seconds!");
                            break;
                        case 3:
                            Arena.sendArenaMessage(arenaName, plugin.name+ChatColor.BLUE+"Arena starting 3...");
                            break;
                        case 2:
                            Arena.sendArenaMessage(arenaName, plugin.name+ChatColor.BLUE+"Arena starting 2...");
                            break;
                        case 1:
                            Arena.sendArenaMessage(arenaName, plugin.name+ChatColor.BLUE+"Arena starting 1...");
                            break;
                        case 0:
                            Bukkit.getScheduler().cancelTask(taskId);
                            Arena.arenaStart(arenaName);
                            gameTimer();
                            break;
                    }
                }else{timeToStart=60;
                    Arena.setPlayersXP(arenaName,0);}
            }
        }, 0, 20);
    }
    private void gameTimer(){
        int tntChange = TNTTag.getInstance().getConfig().getInt("change-every");

        this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                if(seconds == 0) {
                    PlayerManager.killTntPlayers(arenaName);
                    AbstractMap.SimpleEntry<Integer, Player> aliveData = Arena.getArenaAlivePlayers(arenaName);
                    if (aliveData.getKey() == 1) {
                        Bukkit.getScheduler().cancelTask(taskId);
                        Arena.sendArenaMessage(arenaName, plugin.name + ChatColor.AQUA + aliveData.getValue().getName() + " is the winner!!");

                        Session playerSession = SessionStorage.getSession(aliveData.getValue().getUniqueId());
                        playerSession.change("points", playerSession.getPoints() + 5);
                        playerSession.change("wins", playerSession.getWins() + 1);

                        Location endLocation = Arena.arenaEndLocations.get(arenaName);
                        for (Player p : Arena.gameArenas.get(arenaName).keySet()) {
                            if(Arena.isValidArena(endLocation)){ p.teleport(endLocation); }
                            p.setGameMode(GameMode.SURVIVAL);
                            Scoreboards.setLobbyScoreboard(p);
                            Arena.showAllPlayers(p);
                        }
                        Arena.sendArenaMessage(arenaName, plugin.name + ChatColor.GRAY + "You left the arena");
                        Arena.gameArenas.get(arenaName).clear();
                        Arena.toJoinArenas.add(arenaName);
                        round = 0;
                        startTimer();
                        return;
                    }
                    int nextPlayers = (int) Arena.getArenaPlayers(arenaName) / 4;
                    if (nextPlayers == 0) {
                        nextPlayers = 1;
                    }
                    PlayerManager.getNextPlayers(arenaName, nextPlayers);
                    round++;
                }
                seconds++;
                if(seconds == tntChange){
                    System.out.println("A");
                    seconds = 0;
                }
                Scoreboards.setScoreboardToArenaPlayers(arenaName, round, tntChange-seconds);
            }
        }, 0, 20);
    }
}
