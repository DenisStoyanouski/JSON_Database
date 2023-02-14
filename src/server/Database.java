package server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

class Database {
    private volatile Map<String, String> database;

    private final Path path = Paths.get("." + File.separator + "JSON Database" + File.separator + "task" +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator + "db.json");

    private final File file = new File(path.toString());

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

    public synchronized Message setCell(String key, String value) {
        Message response = new Message();
        database.put(key, value);
        response.setType("OK");
        serializeDB();
        return response;

    }

    public synchronized Message deleteCell(String key) {
        Message response = new Message();
        if (database.get(key) != null) {
            database.remove(key);
            response.setType("OK");
        } else {
        response.setType("ERROR");
        response.setKey("No such key");
        }
        serializeDB();
        return response;
    }

    private synchronized void serializeDB() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = null;
        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(database);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(jsonResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
