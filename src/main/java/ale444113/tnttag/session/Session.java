package ale444113.tnttag.session;

import ale444113.tnttag.DB.MongoDB;
import org.bson.Document;

import java.util.UUID;

public class Session {
    private final UUID uuid;

    private Document playerData;

    public Session(UUID uuid){
        this.uuid = uuid;
        reloadData();
        SessionStorage.addSession(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public int getPoints(){
        return this.playerData.getInteger("points");
    }

    public int getWins(){
        return this.playerData.getInteger("wins");
    }

    public int getRounds(){
        return this.playerData.getInteger("rounds");
    }

    public void reloadData(){
        Document playerData = MongoDB.getPlayerData(uuid);
        if(this.playerData == null) playerData = MongoDB.createPlayerData(uuid);
        this.playerData = playerData;
    }

    public void change(String data, int n){
        if(!this.playerData.containsKey(data)) return;
        this.playerData.replace(data, n);

        MongoDB.changeData(this.uuid, data, n);
    }
}
