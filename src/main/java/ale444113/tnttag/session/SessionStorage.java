package ale444113.tnttag.session;

import java.util.HashMap;
import java.util.UUID;

public class SessionStorage {
    private static HashMap<UUID, Session> sessionStorage  = new HashMap<>();;

    public static void addSession(Session session){
        sessionStorage.put(session.getUuid(), session);
    }

    public static void removeSession(UUID uuid){
        sessionStorage.remove(uuid);
    }

    public static Session getSession(UUID uuid){
        return sessionStorage.get(uuid);
    }
}
