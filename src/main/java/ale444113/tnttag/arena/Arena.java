package ale444113.tnttag.arena;

import ale444113.tnttag.PlayerManager;
import ale444113.tnttag.Scoreboards;
import ale444113.tnttag.TNTTag;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public class Arena {

    public static HashMap<String, HashMap<Player, PlayerManager.playerState>> gameArenas = new HashMap<>();
    public static HashMap<String, Location> arenaStartLocations = new HashMap<>();
    public static HashMap<String, Location> arenaEndLocations = new HashMap<>();
    public static ArrayList<String> toJoinArenas= new ArrayList<>(); //This will store the arenas which allows the user to join, if it isn't here is because it is full or alredy started


    public Arena(String arenaName, TNTTag plugin){
        gameArenas.put(arenaName, new HashMap<>());
        int requiredPlayers = plugin.getConfig().getConfigurationSection(arenaName).getInt("min");
        new ArenaTimer(arenaName,requiredPlayers);

        FileConfiguration config = plugin.getConfig();

        ConfigurationSection startLocation = config.getConfigurationSection(arenaName).getConfigurationSection("startLocation");
        World world =  Bukkit.getServer().getWorld(startLocation.getString("world"));
        double x = startLocation.getDouble("x");
        double y = startLocation.getDouble("y");
        double z = startLocation.getDouble("z");
        arenaStartLocations.put(arenaName,new Location(world, x, y, z, 0, 0));

        ConfigurationSection endLocation = config.getConfigurationSection(arenaName).getConfigurationSection("endLocation");
        world =  Bukkit.getServer().getWorld(endLocation.getString("world"));
         x = endLocation.getDouble("x");
         y = endLocation.getDouble("y");
         z = endLocation.getDouble("z");
        arenaEndLocations.put(arenaName,new Location(world, x, y, z, 0, 0));
    }

    public static void joinArena(String arenaName, Player player){
        HashMap<Player, PlayerManager.playerState> arenaPlayers = gameArenas.get(arenaName);
        arenaPlayers.put(player, PlayerManager.playerState.Playing);
        gameArenas.replace(arenaName, arenaPlayers);

        player.setGameMode(GameMode.SURVIVAL);

        sendArenaMessage(arenaName,TNTTag.getInstance().name+"&f"+player.getName()+"&9 just joined the arena!");
    }

    public static void leaveArena(String arenaName, Player player){
        HashMap<Player, PlayerManager.playerState> arenaPlayers = gameArenas.get(arenaName);
        arenaPlayers.remove(player);
        gameArenas.replace(arenaName, arenaPlayers);

        Scoreboards.setLobbyScoreboard(player);
        player.setGameMode(GameMode.SURVIVAL);
        showAllPlayers(player);

        Location endLocation = Arena.arenaEndLocations.get(arenaName);
        if(Arena.isValidArena(endLocation)){ player.teleport(endLocation); }

        sendArenaMessage(arenaName,TNTTag.getInstance().name+"&f"+player.getName()+"&7 left the arena");
    }

    public static void arenaStart(String arenaName){
        if(!toJoinArenas.contains(arenaName)) return;
        toJoinArenas.remove(arenaName);
    }

    public static void sendArenaMessage(String arenaName, String message){
        for (Map.Entry<Player, PlayerManager.playerState> entry : Arena.gameArenas.get(arenaName).entrySet()) {
            entry.getKey().sendMessage(ChatColor.translateAlternateColorCodes('&',message));
        }
    }

    public static void createArena(String playerName, int requieredPlayers, int maxPlayers, String arenaName, TNTTag plugin, FileConfiguration config){
        Map<String,Object> addToConfig = new HashMap<>();
        addToConfig.put("max",maxPlayers);
        addToConfig.put("min",requieredPlayers);
        addToConfig.put("creator",playerName);
        Map<String,Object> location = new HashMap<>();
        location.put("world","world");
        location.put("x",0.0);
        location.put("y",0.0);
        location.put("z",0.0);

        toJoinArenas.add(arenaName);

        config.createSection(arenaName, addToConfig);
        config.getConfigurationSection(arenaName).createSection("startLocation",location);
        config.getConfigurationSection(arenaName).createSection("endLocation",location);

        plugin.saveConfig();

        new Arena(arenaName, plugin);
    }

    public static void deleteArena(String arenaName, TNTTag plugin, FileConfiguration config){
        config.set(arenaName, null);
        plugin.saveConfig();

        toJoinArenas.remove(arenaName);

        Arena.gameArenas.remove(arenaName);
    }

    public static int getArenaPlayers(String arenaName){
        return gameArenas.get(arenaName).size();
    }

    public static AbstractMap.SimpleEntry<Integer, Player> getArenaAlivePlayers(String arenaName){
        int size = 0;
        Player p = null;
        for(Map.Entry<Player, PlayerManager.playerState> entry : Arena.gameArenas.get(arenaName).entrySet()) {
            if(entry.getValue().equals(PlayerManager.playerState.Playing) ||entry.getValue().equals(PlayerManager.playerState.WithTnT)){
                p = entry.getKey();
                ++size;
            }
        }
        return new AbstractMap.SimpleEntry(size, p);
    }

    public static void setPlayersXP(String arenaName, int level){
        for(Player player : gameArenas.get(arenaName).keySet()){
            player.setLevel(level);
        }
    }

    public static boolean isValidArena(Location location){
        return location.getX() != 0 && location.getY() != 0 && location.getZ() != 0;
    }

    public static void showOnlyArenaPlayers(Player p){
        TNTTag plugin = TNTTag.getInstance();
        for(Player toHide : Bukkit.getServer().getOnlinePlayers()){
            try{ p.hidePlayer(plugin, toHide); }catch (Exception ignore){ p.hidePlayer(toHide); }
        }
        for(Player toShow : gameArenas.get(PlayerManager.getPlayerArena(p)).keySet()){
            try{ p.showPlayer(plugin, toShow); }catch (Exception ignore){ p.showPlayer(toShow); }
        }
    }
    public static void showAllPlayers(Player p){
        TNTTag plugin = TNTTag.getInstance();
        for(Player toShow : Bukkit.getServer().getOnlinePlayers()){
            try{ p.showPlayer(plugin, toShow); }catch (Exception ignore){ p.showPlayer(toShow); }
        }
    }
}