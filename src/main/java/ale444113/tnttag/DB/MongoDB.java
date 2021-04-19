package ale444113.tnttag.DB;
import ale444113.tnttag.TNTTag;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;


import java.util.UUID;

public class MongoDB {
    private final TNTTag plugin = TNTTag.getInstance();
    private static MongoDatabase database;
    private static MongoCollection playersData;
    public static boolean usingMongoDB = false;
    private static final ConsoleCommandSender console =  Bukkit.getConsoleSender();

    public MongoDB() {
        init();
    }

    private void init() {
        String mongoUrl = plugin.getConfig().getString("mongoUri");
        MongoClient mongoClient = null;
        if (mongoUrl == null || mongoUrl.equals("") ){
            console.sendMessage(plugin.name+ChatColor.RED+"No DB provided...");
            console.sendMessage(plugin.name+ChatColor.YELLOW+"Creating local DB...");
            mongoClient = new MongoClient();
            console.sendMessage(plugin.name+ChatColor.GREEN+"Local DB created");
        }
        if(mongoClient == null) mongoClient = new MongoClient(new MongoClientURI(mongoUrl));
        database = mongoClient.getDatabase("MinecraftTNTTag");
        try {
            playersData = database.getCollection("PlayersData");
        } catch (Exception e) {
            console.sendMessage(plugin.name + ChatColor.RED + "ERROR CONNECTING TO THE DATABASE");
            console.sendMessage(ChatColor.YELLOW + "Did you provide a valid mongouri?");
            console.sendMessage(ChatColor.GRAY + "Error: ");
            System.out.println(e);
        }
        usingMongoDB = true;
    }
    public static Document createPlayerData(UUID playerUUID) {
        Document playerDocumentData = new Document("_id", playerUUID)
                .append("points", 0)
                .append("wins", 0)
                .append("rounds", 0);
        Document playerData = getPlayerData(playerUUID);
        if(playerData != null){ return playerData; }
        playersData.insertOne(playerDocumentData);
        return playerDocumentData;
    }

    public static Document getPlayerData(UUID playerUUID) {
        BasicDBObject query = new BasicDBObject("_id", playerUUID);
        MongoCursor cursor = playersData.find(query).cursor();
        if(cursor.hasNext()){ return (Document) cursor.next(); }
        return null;

    }

    public static void changeData(UUID uuid, String dataToChange, int value){
        BasicDBObject query = new BasicDBObject("_id", uuid);

        Document playerData = (Document) playersData.find(query).first();
        if(playerData == null){ return; }
        playerData.replace(dataToChange, value);

        playersData.findOneAndReplace(query, playerData);
    }

    public static void deletePlayerData(UUID uuid){
        BasicDBObject query = new BasicDBObject("_id", uuid);
        if(!playersData.find(query).cursor().hasNext()){ return; }
        playersData.deleteOne(query);
    }

}
