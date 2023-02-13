package server;

import java.util.HashMap;
import java.util.Map;

class Database {
    private Map<String, String> database;

    public Database() {
        database = new HashMap<>();
    }

    public Message getCell(String key) {
        Message response = new Message();
        if (database.get(key) != null) {
            response.setType("OK");
            response.setValue(database.get(key));
        } else {
        response.setType("ERROR");
        response.setKey("No such key");
        }
        return response;
    }

    public Message setCell(String key, String value) {
        Message response = new Message();
        database.put(key, value);
        response.setType("OK");

        return response;
    }

    public Message deleteCell(String key) {
        Message response = new Message();
        if (database.get(key) != null) {
            database.remove(key);
            response.setType("OK");
        } else {
        response.setType("ERROR");
        response.setKey("No such key");
        }
        return response;
    }

}
