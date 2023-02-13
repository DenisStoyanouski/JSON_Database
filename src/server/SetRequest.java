package server;

public class SetRequest implements Request{

    private Database db;

    private String key;

    private String value;

    public SetRequest(Database db, String key, String value) {
        this.db = db;
        this.key = key;
        this.value = value;
    }

    @Override
    public Message execute() {
        return db.setCell(key, value);
    }
}
