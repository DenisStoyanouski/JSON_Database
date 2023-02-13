package server;

public class GetRequest implements Request{
    private Database db;

    private String key;

    public GetRequest(Database db, String key) {
        this.db = db;
        this.key = key;
    }

    @Override
    public Message execute() {
        return db.getCell(key);
    }
}
