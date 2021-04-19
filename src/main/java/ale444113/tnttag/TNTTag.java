package ale444113.tnttag;

import ale444113.tnttag.DB.MongoDB;
import ale444113.tnttag.arena.Arena;
import ale444113.tnttag.arena.commands.admin.arenalocations.SetEndLocation;
import ale444113.tnttag.arena.commands.admin.arenalocations.SetFirstLocation;
import ale444113.tnttag.arena.commands.admin.arenamodifications.CreateArenaCommand;
import ale444113.tnttag.arena.commands.admin.arenamodifications.DeleteArenaCommand;
import ale444113.tnttag.arena.commands.admin.DeletePlayerCommand;
import ale444113.tnttag.arena.commands.admin.ListArenaCommand;
import ale444113.tnttag.arena.commands.user.JoinArenaCommand;
import ale444113.tnttag.arena.commands.user.LeaveArenaCommand;
import ale444113.tnttag.events.DamageListener;
import ale444113.tnttag.events.PlayerJoin;
import ale444113.tnttag.events.TntBlockEvents;
import ale444113.tnttag.events.PlayerLeave;
import ale444113.tnttag.session.Session;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

//                        PLUGIN CREATED WITH LOVE FROM ALENUMEROS
//                       PLEASE DO NOT DISTRIBUTE IT WITHOUT CONSENT
//                       uwu             I <3 U                  owo


public final class TNTTag extends JavaPlugin {

    private static TNTTag instace;
    public static TNTTag getInstance(){
        return instace;
    }

    public String configRute;

    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();
    public String name = ChatColor.GOLD+"["+pdffile.getName()+"]";

    public ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();


    @Override
    public void onEnable() {
        instace = this;
        new MongoDB();
        registerCommands();
        registerEvents();
        registerConfig();
        init();
        playerSessions();

        console.sendMessage(name+"Enabled correctly V"+version);
    }

    @Override
    public void onDisable() {
        console.sendMessage(name+"Unloaded correctly V"+version);
    }

    public void registerCommands() {
        //User commands
        this.getCommand("joinarena").setExecutor(new JoinArenaCommand(this));
        this.getCommand("leavearena").setExecutor(new LeaveArenaCommand(this));
        //Staff commands
        this.getCommand("listarenas").setExecutor(new ListArenaCommand(this));
        //Owner commands
        this.getCommand("deleteplayerdata").setExecutor(new DeletePlayerCommand(this));

        this.getCommand("createarena").setExecutor(new CreateArenaCommand(this));
        this.getCommand("deletearena").setExecutor(new DeleteArenaCommand(this));

        this.getCommand("setarenastart").setExecutor(new SetFirstLocation(this));
        this.getCommand("setarenaend").setExecutor(new SetEndLocation(this));
    }
    public void registerEvents() {
        PluginManager pm = 	getServer().getPluginManager();
        pm.registerEvents(new PlayerLeave(), this);
        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new TntBlockEvents(), this);
        pm.registerEvents(new DamageListener(), this);
    }

    public void registerConfig() {
        File config = new File(this.getDataFolder(),"config.yml");
        configRute = config.getPath();
        getConfig().getString("");

        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();

        }
    }

    public void init(){
        ArrayList<String> defaultKeys = new ArrayList<>(Arrays.asList("change-every", "server-ip", "mongoUri"));
        FileConfiguration config = this.getConfig();
        for(String key: config.getKeys(false)){
            if(defaultKeys.contains(key)){ continue; }
            new Arena(key, this);
            Arena.toJoinArenas.add(key);
        }
    }

    public void playerSessions(){
        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            new Session(player.getUniqueId());
            Scoreboards.setLobbyScoreboard(player);
        }
    }

}