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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Database {
    final private Map<String, String> database;

    final private static ReadWriteLock lock = new ReentrantReadWriteLock();

    final private static Lock writeLock = lock.writeLock();

    final private static Lock readLock = lock.readLock();


    /*private final Path path = Paths.get("." + File.separator + "JSON Database" + File.separator + "task" +
            File.separator + "src" + File.separator + "server" + File.separator + "data" + File.separator + "db.json");*/

    private final Path path = Paths.get("." + File.separator + "src" + File.separator + "server" + File.separator +
            "data" + File.separator + "db.json");

    private final File file = new File(path.toString());

    public Database() {
        database = new HashMap<>();

    }

    public Message getCell(String key) {
        Message response = new Message();
        try {
            readLock.lock();
            if (database.get(key) != null) {
                response.setType("OK");
                response.setValue(database.get(key));
            } else {
                response.setType("ERROR");
                response.setKey("No such key");
            }
        } finally {
            readLock.unlock();
        }
        return response;
    }

    public Message setCell(String key, String value) {
        Message response = new Message();
        try {
            writeLock.lock();
            database.put(key, value);
            response.setType("OK");
            serializeDB();
        } finally {
            writeLock.unlock();
        }
        return response;

    }

    public Message deleteCell(String key) {
        Message response = new Message();
        try {
            writeLock.lock();
            if (database.get(key) != null) {
                database.remove(key);
                response.setType("OK");
            } else {
                response.setType("ERROR");
                response.setKey("No such key");
            }
            serializeDB();
        } finally {
            writeLock.unlock();
        }

        return response;
    }

    private void serializeDB() {
        //use jackson
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult;
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
